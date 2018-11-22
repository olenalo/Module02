package com.company;

public class Compressor {

    private String filename;
    private Metadata metadata;

    public Compressor(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public CompressionResult compress() {
        CompressionResultBuilder builder = new CompressionResultBuilder().filename(this.filename);
        // TODO
        return builder.build();
    }

    public void save(byte[] bytes) {
        IOUtils.writeFile(bytes,
                          this.getMetadata(),
                          this.getFilename(),
                          Configs.METADATA_TABLE_FILENAME // TODO consider passing it with user input, here and during decompression
        );
    }

}
