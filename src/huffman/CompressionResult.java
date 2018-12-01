package huffman;

import java.util.Arrays;
import java.util.List;

import static utils.ConversionUtils.convertToByteArray;

public class CompressionResult {
    private byte[] bytes;
    private Metadata metadata;

    public CompressionResult(List<Byte> bytes, Metadata metadata) {
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
        // TODO discuss images compression
        return "CompressionResult: \n" +
                "metadata=" + metadata +
                "bytes=" + Arrays.toString(bytes);
    }
}
