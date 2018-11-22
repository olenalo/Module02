package com.company;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class CompressionResult {
    private byte[] bytes;  // compression result
    private String fileName;
    private Metadata metadata;

    public static CompressionResultBuilder newBuilder() {
        return new CompressionResultBuilder();
    }

    public byte[] getBytes() {
        return bytes;
    }
}
