package com.company;

import java.util.*;

public class Compressor implements Processor {
    private String filename;
    private byte[] inputDataBytes;
    private CompressionResult compressionResult;

    public Compressor(String initialFileName, String filename) {
        IOUtils.checkFilenameExtension(initialFileName);
        IOUtils.checkFilenameExtension(filename);
        this.filename = filename;
        this.inputDataBytes = IOUtils.readFile(initialFileName);
    }

    private Map<Integer, String> buildCodes(String storedPath,
                                           Node node,
                                           int frequencyIndex,
                                           Map<Integer, String> codes) {
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
    private long[] defineFrequencies() {
        long[] frequencies = new long[Configs.BYTES_MAX_NUMBER];
        for (byte b: this.inputDataBytes) {
            int i = b & 0xFF;
            frequencies[i]++;
        }
        return frequencies;
    }

    private PriorityQueue<Node> createNodes(long[] frequencies) {
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

    private PriorityQueue<Node> buildHuffmanTree(long[] frequencies) {
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

    private Map<Integer, String> buildHuffmanCodes(Node root, long[] frequencies) {
        Map<Integer, String> codes = new HashMap<>();
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                codes = buildCodes("", root, i, codes);
            }
        }
        return codes;
    }

    private void collectBits(Metadata metadata, CompressionResultBuilder builder) {
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
    }

    public Compressor process() {
        CompressionResultBuilder builder = new CompressionResultBuilder();
        long[] frequencies = this.defineFrequencies();
        PriorityQueue<Node> nodes = this.buildHuffmanTree(frequencies);
        Map<Integer, String> codes = this.buildHuffmanCodes(nodes.peek(), frequencies);
        Metadata metadata = new Metadata(codes);
        this.collectBits(metadata, builder);
        this.compressionResult = builder
                .setMetadata(metadata)
                .build();
        return this;
    }

    public void save() {
        IOUtils.writeFile(this.compressionResult.getBytes(),
                this.compressionResult.getMetadata(),
                this.filename,
                Configs.METADATA_TABLE_FILENAME
        );
    }

}
