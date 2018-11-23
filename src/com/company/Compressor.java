package com.company;

import java.util.*;

public class Compressor {

    private String filename;
    private byte[] inputDataBytes; // TODO shouldn't it be of `int[]` type?

    public Compressor(byte[] bytes, String filename) {
        // TODO check filename extension
        this.filename = filename;
        this.inputDataBytes = bytes;
    }

    public Map<Integer, String> buildCodes(String storedPath,
                                           Node node,
                                           int frequencyIndex,
                                           Map<Integer, String> codes) { // TODO consider removing `codes` param
        if (node.getLeft() == null && node.getRight() == null) {
            if (Integer.valueOf(node.getValue()).equals(frequencyIndex)) {
                codes.put(frequencyIndex, storedPath);
            }
        } else {
            buildCodes(storedPath + "0", node.getLeft(), frequencyIndex, codes);
            buildCodes(storedPath + "1", node.getRight(), frequencyIndex, codes);
        }
        return codes;
    }

    // Count occurrences of each byte in the initial input data
    public long[] defineFrequencies() {
        long[] frequencies = new long[Configs.BYTES_MAX_NUMBER];
        for (byte b: this.inputDataBytes) {
            int i = b & 0xFF;
            frequencies[i]++;
        }
        return frequencies;
    }

    public PriorityQueue<Node> createNodes(long[] frequencies) {
        PriorityQueue<Node> nodes = new PriorityQueue<>(Comparator.comparingLong(Node::getWeight));
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                Node node = new Node();
                node.setWeight(frequencies[i]);
                node.setValue(i);
                nodes.add(node);
            }
        }
        return nodes;
    }

    public PriorityQueue<Node> buildHuffmanTree(long[] frequencies) {
        PriorityQueue<Node> nodes = createNodes(frequencies);
        while (nodes.size() > 1) {
            Node node1 = nodes.poll();
            Node node2 = nodes.poll();
            Node newNode = new Node();
            newNode.setLeft(node1);
            newNode.setRight(node2);
            newNode.setWeight(node1.getWeight() + node2.getWeight());
            // We only need `value` in initial nodes (not in the "merged" ones)
            nodes.add(newNode);
        }
        return nodes;
    }

    public Map<Integer, String> buildHuffmanCodes(Node root, long[] frequencies) {
        Map<Integer, String> codes = new HashMap<>();
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                codes = buildCodes("", root, i, codes); // TODO we are assigning each time...
            }
        }
        return codes;
    }

    public CompressionResult compress() {
        CompressionResultBuilder builder = new CompressionResultBuilder().setFilename(this.filename);
        long[] frequencies = this.defineFrequencies();
        PriorityQueue<Node> nodes = this.buildHuffmanTree(frequencies);
        Map<Integer, String> codes = this.buildHuffmanCodes(nodes.peek(), frequencies);
        Metadata metadata = new Metadata(codes);

        // Collect bits
        for (int i = 0; i < this.inputDataBytes.length; i++) {
            String byteCodes = metadata.getCode(this.inputDataBytes[i]);
            char[] charArray = byteCodes.toCharArray(); // TODO store bits in String[] or Bit[], not in `String`
            for (int j = 0; j < charArray.length; j++) {
                char bit = charArray[j];
                boolean isLastByte = false;
                if (i == this.inputDataBytes.length - 1 && j == byteCodes.length() - 1) {
                    isLastByte = true;
                }
                builder.addBit(bit, isLastByte);
            }
        }
        return builder.setMetadata(metadata).build();
    }

}
