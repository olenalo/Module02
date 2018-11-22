package com.company;

import java.util.*;

public class CompressionResultBuilder {

    private byte[] inputDataBytes = null;
    private String fileName;

    private Map<Integer, String> codes = new HashMap<>();
    private long[] frequencies = new long[Configs.BYTES_MAX_NUMBER];
    PriorityQueue<Node> nodes = new PriorityQueue<>(Comparator.comparingLong(Node::getWeight));

    private long significantBitsNumber = 0;
    private StringBuilder bitsCash = new StringBuilder();
    private int bitsCashCounter = 0;
    private ArrayList<Byte> bytes = new ArrayList<>();
    private ArrayList<String> bits = new ArrayList<>(); // debug only TODO consider adding to toString()

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

    // TODO return CompressionResultBuilder instance in methods below

    public void buildCodes(String storedPath, Node node, int frequencyIndex) {
        if (node.getLeft() == null && node.getRight() == null) {
            if (Integer.valueOf(node.getValue()).equals(frequencyIndex)) {
                this.codes.put(frequencyIndex, storedPath);
            }
        } else {
            buildCodes(storedPath + "0", node.getLeft(), frequencyIndex);
            buildCodes(storedPath + "1", node.getRight(), frequencyIndex);
        }
    }

    public void serInputDataBytes(String inputFilename) {
        this.inputDataBytes = IOUtils.readFile(inputFilename);
    }

    // Count occurrences of each byte in the initial input data
    public void setFrequencies() {
        for (byte b: inputDataBytes) {
            int i = b & 0xFF;
            frequencies[i]++;
        }
    }

    // Build the priority queue
    public void setNodes() {
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                Node node = new Node();
                node.setWeight(frequencies[i]);
                node.setValue(i);
                nodes.add(node);
            }
        }
    }

    // Updating nodes
    public void buildHuffmanTree() {
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
    }

    public CompressionResult build() {
        return new CompressionResult();
    }

}
