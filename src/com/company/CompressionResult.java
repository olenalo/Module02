package com.company;

import java.util.ArrayList;

public class CompressionResult {
    private byte[] bytes;
    private String filename;
    private Metadata metadata;

    public CompressionResult(ArrayList<Byte> bytes,
                             String filename,
                             Metadata metadata) {
        this.filename = filename;
        this.metadata = metadata;
        // Convert to the expected format
        byte[] compressionResult = new byte[bytes.size()];
        for (int i = 0; i < compressionResult.length; i++) {
            compressionResult[i] = bytes.get(i);
        }
        this.bytes = compressionResult;
    }

    public void save() {
        // TODO add filename extension check
        IOUtils.writeFile(this.bytes,
                          this.metadata,
                          this.filename,
                          Configs.METADATA_TABLE_FILENAME // TODO consider passing it with user input, here and for decompression
        );
    }
}
