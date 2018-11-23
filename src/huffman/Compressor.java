package huffman;

import configs.Bit;
import configs.Configs;
import utils.IOUtils;

import java.util.*;

import static utils.Utils.convertToBitArray;

public class Compressor implements Processor {
    private String filename;
    private byte[] inputDataBytes;
    private CompressionResult compressionResult;

    public Compressor(String initialFileName, String filename) {
        IOUtils.checkFilenameExtension(initialFileName);
        IOUtils.checkFilenameExtension(filename);
        this.filename = filename;
        this.inputDataBytes = IOUtils.readFile(initialFileName);
        System.out.println("inputDataBytes: " + Arrays.toString(this.inputDataBytes));
    }

    private Map<Integer, Bit[]> buildCodes(ArrayList<Bit> storedBits,
                                           Node node,
                                           int frequencyIndex,
                                           Map<Integer, Bit[]> codes) {
        if (node.getLeft() == null && node.getRight() == null) {
            if (Integer.valueOf(node.getValue()).equals(frequencyIndex)) {
                codes.put(frequencyIndex, convertToBitArray(storedBits));
            }
        } else {
            ArrayList<Bit> storedBitsToLeft = (ArrayList<Bit>) storedBits.clone();
            storedBitsToLeft.add(Bit.ZERO);
            ArrayList<Bit> storedBitsToRight = (ArrayList<Bit>) storedBits.clone();
            storedBitsToRight.add(Bit.ONE);
            buildCodes(storedBitsToLeft, node.getLeft(), frequencyIndex, codes);
            buildCodes(storedBitsToRight, node.getRight(), frequencyIndex, codes);
        }
        return codes;
    }

    /**
     * Count occurrences of each byte in the initial input data.
     *
     * @return frequencies with their indexes corresponding
     * to the input data's bytes.
     */
    private long[] defineFrequencies() {
        long[] frequencies = new long[Configs.BYTES_MAX_NUMBER];
        for (byte b : this.inputDataBytes) {
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
            // We only need `value` in initial nodes (not here, in the "merged" ones)
            nodes.add(newNode);
        }
        return nodes;
    }

    private Map<Integer, Bit[]> buildHuffmanCodes(Node root, long[] frequencies) {
        Map<Integer, Bit[]> codes = new HashMap<>();
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                codes.putAll(buildCodes(new ArrayList<>(), root, i, codes));
            }
        }
        return codes;
    }

    private void collectBits(Metadata metadata, CompressionResultBuilder builder) {
        for (int i = 0; i < this.inputDataBytes.length; i++) {
            Bit[] bits = metadata.getCode(this.inputDataBytes[i]);
            for (int j = 0; j < bits.length; j++) {
                Bit bit = bits[j];
                boolean isLastByte = false;
                if (i == this.inputDataBytes.length - 1 && j == bits.length - 1) {
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
        Map<Integer, Bit[]> codes = this.buildHuffmanCodes(nodes.peek(), frequencies);
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
