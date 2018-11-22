package com.company;

import java.util.ArrayList;

public class CompressionResultBuilder {

    private static byte[] inputDataBytes = null;
    private String fileName;
    public static long significantBitsNumber = 0;
    public static StringBuilder bitsCash = new StringBuilder();
    public static int bitsCashCounter = 0;
    private static ArrayList<Byte> bytes = new ArrayList<>();
    private static ArrayList<String> bits = new ArrayList<>(); // debug only TODO consider adding to toString()

    CompressionResultBuilder() {
    }

    public CompressionResultBuilder filename(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public CompressionResultBuilder addBit(char bit, boolean isLastByte) {
        if (bitsCashCounter < Configs.EIGHT_BITS) {
            bitsCash.append(bit);
            bitsCashCounter++;
            significantBitsNumber++;
            if (isLastByte) {
                // Add trailing bits if needed
                int bitsToAddNumber = Configs.EIGHT_BITS - bitsCash.toString().length();
                if (bitsToAddNumber > 0) {
                    for (int i = 0; i < bitsToAddNumber; i++) {
                        bitsCash.append("0");
                    }
                }
                bits.add(bitsCash.toString());
                bytes.add(Integer.valueOf(bitsCash.toString(), 2).byteValue());
                bitsCash = new StringBuilder(); // clean up
            }
        } else {
            bits.add(bitsCash.toString());
            bytes.add(Integer.valueOf(bitsCash.toString(), 2).byteValue());
            bitsCash = new StringBuilder();
            bitsCashCounter = 0;
            addBit(bit, isLastByte);
        }
        return this;
    }

    public CompressionResult build() {
        return null;
    }

}
