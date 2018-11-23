package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class CompressionResult {
    private byte[] bytes;
    private Metadata metadata;

    public CompressionResult(ArrayList<Byte> bytes,
                             Metadata metadata) {
        this.metadata = metadata;
        // Convert to the expected format
        byte[] compressionResult = new byte[bytes.size()];
        for (int i = 0; i < compressionResult.length; i++) {
            compressionResult[i] = bytes.get(i);
        }
        this.bytes = compressionResult;
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
