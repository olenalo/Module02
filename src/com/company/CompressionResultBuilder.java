package com.company;

public class CompressionResultBuilder {

    private String fileName;
    private int[] bits;
    private int[] bytes;

    CompressionResultBuilder() {

    }

    public CompressionResultBuilder setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public CompressionResultBuilder addBit(Bit bit) {
        // TODO add check (bits -> bytes)
        return this;
    }

    public CompressionResult build() {
        return null;
    }

}
