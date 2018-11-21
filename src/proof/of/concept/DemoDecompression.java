package proof.of.concept;

import com.company.IOUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DemoDecompression {

    private static String getBits(byte aByte)
    {
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < 8; j++) {
            byte tmp = (byte) (aByte >> j);
            int expect1 = tmp & 0x01;
            builder.append(expect1);
        }
        return (builder.reverse().toString());
    }


    public static void main(String[] args) {
        HashMap<Integer, String> map = IOUtils.readTree("dictionary.table.txt");
        for (Map.Entry <Integer, String> entry: map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        byte[] decompressedBytes = IOUtils.readFile("compressedResult.txt.hr");
        int significantBitesNumber = decompressedBytes[0];
        System.out.println("Read significantBitesNumber: " + significantBitesNumber );
        int[] bytes = new int[decompressedBytes.length];
        for(int i = 0; i < decompressedBytes.length; i++) {
            bytes[i] = decompressedBytes[i] & 0xFF;
        }
        System.out.println(Arrays.toString(bytes));
        IOUtils.writeDecompressedFile(
                new byte[]{97, 98, 114, 97, 99, 97, 100, 97, 98, 114, 97},
                "decompressedResult.txt");
    }
}
