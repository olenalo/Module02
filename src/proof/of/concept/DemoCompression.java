package proof.of.concept;

import com.company.IOUtils;
import com.company.Metadata;
import com.company.Node;

import java.util.*;

public class DemoCompression {

    public static byte[] inputDataBytes = null;
    public static Map<Integer, String> codes = new HashMap<>();
    public static long significantBitsNumber = 0;

    public static StringBuilder bitsCash = new StringBuilder(); // new char[8];  // TODO shouldn't it be an array?
    public static int bitsCashCounter = 0;
    public static ArrayList<Byte> resultBytes = new ArrayList<>();
    // public static byte[] resultBytes = new byte[inputDataBytes.length];

    public static void addBit(char bit, int inputIndex) {
        if (bitsCashCounter < 8) {
            bitsCash.append(bit);
            bitsCashCounter++;
            significantBitsNumber++;
            if (inputIndex == inputDataBytes.length - 1) {
                for (int i = 0; i < 8 - bitsCash.length(); i++) {
                    bitsCash.append("0");
                }
                resultBytes.add(Integer.valueOf(bitsCash.toString(), 2).byteValue());
            }
        } else {
            resultBytes.add(Integer.valueOf(bitsCash.toString(), 2).byteValue());
            bitsCash = new StringBuilder();
            bitsCashCounter = 0;
            addBit(bit, inputIndex);
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

    public static void main(String[] args) {

        // Proof of concept
        inputDataBytes = IOUtils.readFile("ada.png"); // ada.png inputDataTest.txt  inputData.txt
        System.out.println("Read input bytes: " + Arrays.toString(inputDataBytes));
        long[] frequencies = new long[256];

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
        for (Map.Entry <Integer, String> entry: metadata.getDecodingTable().entrySet()) { // TODO consider <Byte, String>
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("-------");

        // Compress
        for (int i = 0; i < inputDataBytes.length; i++) {
            String byteCodes = codes.get((int)inputDataBytes[i] & 0xFF);
            if (byteCodes != null) {  // TODO test image compression
                for (char bit : byteCodes.toCharArray()) {
                    addBit(bit, i);
                }
            }
        }
        // TODO try to avoid the next conversion
        byte[] compressionResult = new byte[resultBytes.size()]; // resultBytes.toArray(new Byte[0]);
        for (int i = 0; i < compressionResult.length; i++) {
            compressionResult[i] = resultBytes.get(i);
        }
        metadata.setSignificantBitsNumber(significantBitsNumber);

        System.out.println("significantBitsNumber: " + metadata.getSignificantBitsNumber());
        System.out.println(Arrays.toString(compressionResult));

        IOUtils.writeFile(compressionResult,
                          metadata,
                          "compressedResult.txt.hr",
                          "metadata.table.txt"
        );
    }

}
