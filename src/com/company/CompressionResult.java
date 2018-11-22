package com.company;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class CompressionResult {
    public static byte[] inputDataBytes = null;
    // private int[] bytes;
    private String fileName;
    private PriorityQueue<Node> nodes = new PriorityQueue<>(Comparator.comparingLong(Node::getWeight));

    public static CompressionResultBuilder newBuilder() {
        return new CompressionResultBuilder();
    }

}
