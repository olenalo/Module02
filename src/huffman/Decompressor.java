package huffman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static configs.Configs.EIGHT_BITS;
import static utils.ConversionUtils.convertToByteArray;
import static utils.IOUtils.*;

public class Decompressor implements Processor {
    private String filename;
    private byte[] inputBytes;
    private byte[] bytes;
    private Metadata metadata;
    private StringBuilder bitsBuffer = new StringBuilder();
    private ArrayList<Byte> resultBytes = new ArrayList<>();

    public Decompressor(String initialFileName, String metadataFileName, String filename) {
        checkFilenameExtension(filename);
        checkFilenameExtension(metadataFileName);
        this.inputBytes = readFile(initialFileName);
        this.metadata = readMetadata(metadataFileName);
        System.out.println(this.metadata);
        this.filename = filename;
    }

    private static String getBits(byte aByte) {
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < EIGHT_BITS; j++) {
            byte tmp = (byte) (aByte >> j);
            int expect1 = tmp & 0x01;
            builder.append(expect1);
        }
        return builder.reverse().toString();
    }

    private String[] fetchBits() {
        int[] inputBytes = new int[this.inputBytes.length]; // debug only
        String[] bitsStrings = new String[this.inputBytes.length];
        int bitsToRemoveNumber = (int) (this.inputBytes.length * 8 - this.metadata.getSignificantBitsNumber());
        System.out.println("bitsToRemoveNumber: " + bitsToRemoveNumber);
        int bytesCounter = 0;
        for (int i = 0; i < this.inputBytes.length; i++) {
            // Ignore trailing bits if needed
            int b = this.inputBytes[i] & 0xFF;
            inputBytes[i] = b;  // debug only
            String bits = getBits((byte) b);
            // TODO bitsToRemoveNo might not be needed
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
        bitsBuffer.append(bit);
        String bitsString = bitsBuffer.toString();
        if (metadata.getConvertedDecodingTable().containsValue(bitsString)) {
            try {
                resultBytes.add((byte) metadata.getKeyByValue(bitsString));
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
            bitsBuffer.setLength(0);
        }
    }

    private void decodeBits(String[] bitsStrings) {
        // TODO: why String[], not simply String?..
        for (String bits : bitsStrings) {
            for (char bit : bits.toCharArray()) {
                this.addBit(bit);
            }
        }
    }

    public Decompressor process() {
        String[] bitsStrings = this.fetchBits();
        this.decodeBits(bitsStrings);
        this.bytes = convertToByteArray(this.resultBytes);
        System.out.println("Decompressed content to write: " + Arrays.toString(this.bytes));
        return this;
    }

    public void save() {
        writeDecompressedFile(
                this.bytes,
                this.filename);
    }

}
