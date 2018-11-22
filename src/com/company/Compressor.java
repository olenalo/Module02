package com.company;

public class Compressor {

    String filename;
    Metadata metadata;

    public Compressor(String filename) {
        this.filename = filename;
    }

    public CompressionResult compress() {
        CompressionResultBuilder builder = new CompressionResultBuilder().filename(this.filename);

        return builder.build();
    }

}
