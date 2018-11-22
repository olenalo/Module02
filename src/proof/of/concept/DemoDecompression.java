package proof.of.concept;

import com.company.Configs;
import com.company.IOUtils;
import com.company.Metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class DemoDecompression {

    public static Metadata metadata;
    public static StringBuilder bitsCash = new StringBuilder();
    public static ArrayList<Byte> resultBytes = new ArrayList<>();

    private static String getBits(byte aByte) {
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < Configs.EIGHT_BITS; j++) {
            byte tmp = (byte) (aByte >> j);
            int expect1 = tmp & 0x01;
            builder.append(expect1);
        }
        return (builder.reverse().toString());
    }

    public static void addBit(char bit) {
        bitsCash.append(bit);
        if (metadata.getDecodingTable().containsValue(bitsCash.toString())) {
            byte aByte = (byte)getKeyByValue(bitsCash.toString(), metadata.getDecodingTable()) ;
            if (aByte != -1) resultBytes.add(aByte);
            bitsCash = new StringBuilder();
        }
    }

    // Get the first matched case
    public static int getKeyByValue(String value, Map<Integer, String> map) {
        for (Map.Entry <Integer, String> entry: map.entrySet()) {  // TODO add static import
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public static void decompress(String initialFileName, String metadataFileName, String decompressedFileName) {
        // TODO add filenames' extensions checks

        // Read the compressed content
        metadata = IOUtils.readMetadata(metadataFileName);
        for (Map.Entry <Integer, String> entry: metadata.getDecodingTable().entrySet()) {
            System.out.print(entry.getKey() + ": " + entry.getValue() + "; ");
        }

        // Read the metadata
        byte[] decompressedBytes = IOUtils.readFile(initialFileName);
        System.out.println("\nRead significantBitesNumber: " + metadata.getSignificantBitsNumber());
        System.out.println("Read decompressedBytes length (bits): " + decompressedBytes.length * 8);

        // Fetch bits to decode
        int[] bytes = new int[decompressedBytes.length]; // debug only
        String[] bitsStrings = new String[decompressedBytes.length];
        int bitsToRemoveNumber = (int)(decompressedBytes.length * 8 - metadata.getSignificantBitsNumber());
        System.out.println("bitsToRemoveNumber: " + bitsToRemoveNumber);
        int bytesCounter = 0;
        for(int i = 0; i < decompressedBytes.length; i++) {
            // Remove trailing bits if needed
            int b = decompressedBytes[i] & 0xFF;
            bytes[i] = b;  // debug only
            // FIXME for images
            String bits = getBits((byte)b);
            if (bytesCounter == decompressedBytes.length - 1 && bitsToRemoveNumber > 0) {
                bitsStrings[i] = bits.substring(0, bits.length() - bitsToRemoveNumber);
            } else {
                bitsStrings[i] = bits;
            }
            bytesCounter++;
        }
        System.out.println("Bytes int: " + Arrays.toString(bytes));
        System.out.println("bitsStrings: " + Arrays.toString(bitsStrings));
        // Decode the bits
        // FIXME for images
        for (String bits: bitsStrings) {
            for (char bit: bits.toCharArray()) {
                addBit(bit);
            }
        }
        // TODO try to avoid the next loop
        byte[] decompressionResult = new byte[resultBytes.size()];
        for (int i = 0; i < decompressionResult.length; i++) {
            decompressionResult[i] = resultBytes.get(i);
        }
        System.out.println("Decompressed content to write: " + Arrays.toString(decompressionResult));

        // Write the decompression result
        IOUtils.writeDecompressedFile(
                decompressionResult,
                decompressedFileName);
    }

}
