package huffman;

import configs.Configs;

import java.util.*;

public class CompressionResultBuilder {
    private Metadata metadata;
    private ArrayList<Byte> bytes = new ArrayList<>();  // TODO consider storing in byte[] right away
    private ArrayList<String> bits = new ArrayList<>(); // debug only
    private long significantBitsNumber = 0;
    private StringBuilder bitsCash = new StringBuilder();
    private int bitsCashCounter = 0;

    private void formByte() {
        bits.add(bitsCash.toString());
        bytes.add(Integer.valueOf(bitsCash.toString(), 2).byteValue());
        bitsCash.setLength(0);
    }

    // TODO pass `Bit` type
    public CompressionResultBuilder addBit(char bit, boolean isLastByte) {
        if (bitsCashCounter < Configs.EIGHT_BITS) {
            bitsCash.append(bit);
            bitsCashCounter++;
            significantBitsNumber++;
            if (isLastByte) {
                // Add trailing bits if needed
                int bitsToAddNumber = Configs.EIGHT_BITS - bitsCash.toString().length();
                if (bitsToAddNumber > 0) {
                    for (int i = 0; i < bitsToAddNumber; i++) {
                        bitsCash.append("0");
                    }
                }
                this.formByte();
            }
        } else {
            this.formByte();
            bitsCashCounter = 0;
            addBit(bit, isLastByte);
        }
        return this;
    }

    public CompressionResultBuilder setMetadata(Metadata metadata) {
        this.metadata = metadata;
        this.metadata.setSignificantBitsNumber(this.significantBitsNumber);
        return this;
    }

    public CompressionResult build() {
        System.out.println(this);
        return new CompressionResult(this.bytes, this.metadata);
    }

    @Override
    public String toString() {
        return "CompressionResultBuilder: \n" +
                "metadata=" + metadata +
                "bytes=" + Arrays.toString(bytes.toArray()) + "\n" +
                "bits=" +  Arrays.toString(bits.toArray());
    }
}
