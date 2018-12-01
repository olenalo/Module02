package huffman;

import java.util.ArrayList;
import java.util.Arrays;

import static utils.ConversionUtils.convertToByteArray;

public class CompressionResult {
    private byte[] bytes;
    private Metadata metadata;

    public CompressionResult(ArrayList<Byte> bytes, Metadata metadata) {
        this.metadata = metadata;
        this.bytes = convertToByteArray(bytes);
        // System.out.println(this);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "CompressionResult: \n" +
                "metadata=" + metadata +
                "bytes=" + Arrays.toString(bytes);
    }
}
