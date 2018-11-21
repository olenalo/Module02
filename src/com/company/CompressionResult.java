package com.company;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class CompressionResult {
    public static final int BYTES_MAX_NUMBER = 256; // 255 + 1
    private int[] bytes = new int[BYTES_MAX_NUMBER]; // TODO consider: it was final!
    private final String fileName;
    private PriorityQueue<Node> nodes = new PriorityQueue<>(Comparator.comparingLong(Node::getWeight));

    private CompressionResult(int[] bytes, String fileName) {
        this.bytes = bytes;
        this.fileName = fileName;
    }

    public static CompressionResultBuilder newBuilder() {
        return new CompressionResultBuilder();
    }

    public int[] getBytes() {
        return bytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setNodes(PriorityQueue<Node> nodes) {
        this.nodes = nodes;
    }

    public void setBytes(Object inputData) {
        byte[] bytes;
        if (inputData instanceof String) {
            bytes = ((String) inputData).getBytes();
        }
        // TODO other types
        // this.bytes = bytes;

    }

}
