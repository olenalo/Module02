package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Decompressor implements Processor {
    private String filename;
    private byte[] inputBytes;
    private byte[] bytes;
    private Metadata metadata;
    private StringBuilder bitsCash = new StringBuilder();
    private ArrayList<Byte> resultBytes = new ArrayList<>();

    public Decompressor(String initialFileName, String metadataFileName, String filename) {
        IOUtils.checkFilenameExtension(filename);
        IOUtils.checkFilenameExtension(metadataFileName);
        this.inputBytes = IOUtils.readFile(initialFileName);
        this.metadata = IOUtils.readMetadata(metadataFileName);
        this.filename = filename;
    }

    // Get the first matched case
    private static int getKeyByValue(String value, Map<Integer, String> map) {
        for (Map.Entry <Integer, String> entry: map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return -1;
    }

    private static String getBits(byte aByte) {
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < Configs.EIGHT_BITS; j++) {
            byte tmp = (byte) (aByte >> j);
            int expect1 = tmp & 0x01;
            builder.append(expect1);
        }
        return (builder.reverse().toString());
    }

    // TODO pass `Bit` type
    private void addBit(char bit) {
        this.bitsCash.append(bit);
        if (this.metadata.getDecodingTable().containsValue(this.bitsCash.toString())) {
            byte aByte = (byte)getKeyByValue(this.bitsCash.toString(), this.metadata.getDecodingTable()) ;
            if (aByte != -1) this.resultBytes.add(aByte);
            this.bitsCash = new StringBuilder();
        }
    }

    private String[] fetchBits() {
        int[] inputBytes = new int[this.inputBytes.length]; // debug only
        String[] bitsStrings = new String[this.inputBytes.length];
        int bitsToRemoveNumber = (int)(this.inputBytes.length * 8 - metadata.getSignificantBitsNumber());
        System.out.println("bitsToRemoveNumber: " + bitsToRemoveNumber);
        int bytesCounter = 0;
        for(int i = 0; i < this.inputBytes.length; i++) {
            // Remove trailing bits if needed
            int b = this.inputBytes[i] & 0xFF;
            inputBytes[i] = b;  // debug only
            // FIXME for images
            String bits = getBits((byte)b);
            if (bytesCounter == this.inputBytes.length - 1 && bitsToRemoveNumber > 0) {
                bitsStrings[i] = bits.substring(0, bits.length() - bitsToRemoveNumber);
            } else {
                bitsStrings[i] = bits;
            }
            bytesCounter++;
        }
        System.out.println("input bytes: " + Arrays.toString(inputBytes));
        System.out.println("input bits: " + Arrays.toString(bitsStrings));
        return bitsStrings;
    }

    private void decodeBits(String[] bitsStrings) {
        for (String bits: bitsStrings) {
            for (char bit: bits.toCharArray()) {
                this.addBit(bit);
            }
        }
    }

    // TODO consider getting rid of it
    private void convertResult() {
        byte[] decompressionResult = new byte[this.resultBytes.size()];
        for (int i = 0; i < decompressionResult.length; i++) {
            decompressionResult[i] = this.resultBytes.get(i);
        }
        System.out.println("Decompressed content to write: " + Arrays.toString(decompressionResult));
        this.bytes = decompressionResult;
    }

    public Decompressor process() {
        String[] bitsStrings = this.fetchBits();
        this.decodeBits(bitsStrings);
        this.convertResult();
        return this;
    }

    public void save() {
        IOUtils.writeDecompressedFile(
                this.bytes,
                this.filename);
    }

}
