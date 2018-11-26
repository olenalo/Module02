package huffman;

import configs.Configs;
import utils.IOUtils;
import utils.ConversionUtils;

import java.util.ArrayList;
import java.util.Arrays;

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
        System.out.println(this.metadata);
        this.filename = filename;
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

    private String[] fetchBits() {
        int[] inputBytes = new int[this.inputBytes.length]; // debug only
        String[] bitsStrings = new String[this.inputBytes.length];
        int bitsToRemoveNumber = (int) (this.inputBytes.length * 8 - metadata.getSignificantBitsNumber());
        System.out.println("bitsToRemoveNumber: " + bitsToRemoveNumber);
        int bytesCounter = 0;
        for (int i = 0; i < this.inputBytes.length; i++) {
            // Ignore trailing bits if needed
            int b = this.inputBytes[i] & 0xFF;
            inputBytes[i] = b;  // debug only
            String bits = getBits((byte) b);
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

    private void addBit(char bit) {
        this.bitsCash.append(bit);
        String bitsString = this.bitsCash.toString();
        if (this.metadata.getConvertedDecodingTable().containsValue(bitsString)) {
            this.resultBytes.add((byte) this.metadata.getKeyByValue(bitsString));
            this.bitsCash.setLength(0);
        }
    }

    private void decodeBits(String[] bitsStrings) {
        for (String bits : bitsStrings) {
            for (char bit : bits.toCharArray()) {
                this.addBit(bit);
            }
        }
    }

    public Decompressor process() {
        String[] bitsStrings = this.fetchBits();
        this.decodeBits(bitsStrings);
        this.bytes = ConversionUtils.convertToByteArray(this.resultBytes);
        System.out.println("Decompressed content to write: " + Arrays.toString(this.bytes));
        return this;
    }

    public void save() {
        IOUtils.writeDecompressedFile(
                this.bytes,
                this.filename);
    }

}
