package proof.of.concept;

import com.company.Configs;
import com.company.IOUtils;
import com.company.Metadata;
import com.company.Node;

import java.util.*;

public class DemoCompression {

    public static byte[] inputDataBytes = null;
    public static Map<Integer, String> codes = new HashMap<>();
    public static long significantBitsNumber = 0;

    public static StringBuilder bitsCash = new StringBuilder();
    public static int bitsCashCounter = 0;
    public static ArrayList<Byte> resultBytes = new ArrayList<>();
    public static ArrayList<String> resultBits = new ArrayList<>();  // TODO remove; debug only

    // TODO refactor e.g. introduce a flag `lastBits` or so
    public static void addBit(char bit, int inputBytesIndex, int bitsIndex, String currentByteCodes) {
        if (bitsCashCounter < Configs.EIGHT_BITS) {
            bitsCash.append(bit);
            bitsCashCounter++;
            significantBitsNumber++;
            // Add the last bunch of bits if needed
            if (inputBytesIndex == inputDataBytes.length - 1 && bitsIndex == currentByteCodes.length() - 1) {
                int bitsToAddNumber = Configs.EIGHT_BITS - bitsCash.toString().length();
                // Add trailing zero bits if needed
                if (bitsToAddNumber > 0) {
                    for (int i = 0; i < bitsToAddNumber; i++) {
                        bitsCash.append("0");
                    }
                }
                resultBits.add(bitsCash.toString());
                resultBytes.add(Integer.valueOf(bitsCash.toString(), 2).byteValue());
                // bitsCash = new StringBuilder();
            }
        } else {
            resultBits.add(bitsCash.toString()); // debug only
            resultBytes.add(Integer.valueOf(bitsCash.toString(), 2).byteValue());
            bitsCash = new StringBuilder();
            bitsCashCounter = 0;
            addBit(bit, inputBytesIndex, bitsIndex, currentByteCodes);
        }
    }

    public static void buildCodes(String storedPath, Node node, int frequencyIndex) {
        if (node.getLeft() == null && node.getRight() == null) {
            if (Integer.valueOf(node.getValue()).equals(frequencyIndex)) {
                codes.put(frequencyIndex, storedPath);
            }
        } else {
            buildCodes(storedPath + "0", node.getLeft(), frequencyIndex);
            buildCodes(storedPath + "1", node.getRight(), frequencyIndex);
        }
    }

    public static void compress(String inputFilename, String metadataFilename, String compressedFileName) {
        // TODO add filenames' extensions checks

        inputDataBytes = IOUtils.readFile(inputFilename);
        System.out.println("Read input bytes: " + Arrays.toString(inputDataBytes));
        long[] frequencies = new long[Configs.BYTES_MAX_NUMBER];

        // Count occurrences of each byte in the initial input data
        for (byte b: inputDataBytes) {
            int i = b & 0xFF;
            frequencies[i]++;
        }

        // Build the priority queue
        PriorityQueue<Node> nodes = new PriorityQueue<>(Comparator.comparingLong(Node::getWeight));
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                Node node = new Node();
                node.setWeight(frequencies[i]);
                node.setValue(i);
                nodes.add(node);
            }
        }

        // Build a Huffman tree
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

        // Build codes
        Node root = nodes.peek();
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                buildCodes("", root, i);
            }
        }
        Metadata metadata = new Metadata(codes);
        for (Map.Entry <Integer, String> entry: metadata.getDecodingTable().entrySet()) {
            System.out.print(entry.getKey() + ": " + entry.getValue() + "; ");
        }
        System.out.println("\n-------");

        // Compress
        // Collect bits
        for (int i = 0; i < inputDataBytes.length; i++) {
            byte inputDataByte = inputDataBytes[i];
            String byteCodes = codes.get((int) inputDataByte & 0xFF);
            char[] charArray = byteCodes.toCharArray();
            for (int j = 0; j < charArray.length; j++) {
                char bit = charArray[j];
                addBit(bit, i, j, byteCodes);
            }
        }

        // TODO try to avoid the next loop
        byte[] compressionResult = new byte[resultBytes.size()]; // resultBytes.toArray(new Byte[0]);
        for (int i = 0; i < compressionResult.length; i++) {
            compressionResult[i] = resultBytes.get(i);
        }
        metadata.setSignificantBitsNumber(significantBitsNumber);

        System.out.println("significantBitsNumber: " + metadata.getSignificantBitsNumber());
        System.out.println("compressionResult: " + Arrays.toString(compressionResult));

        IOUtils.writeFile(compressionResult,
                          metadata,
                          compressedFileName,
                          metadataFilename
        );
    }
}
