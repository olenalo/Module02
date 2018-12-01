package huffman;

import configs.Bit;

import java.util.*;

import static configs.Bit.ZERO;
import static configs.Configs.EIGHT_BITS;
import static utils.ConversionUtils.convertBitsToString;

public class CompressionResultBuilder {
    private Metadata metadata;
    private List<Byte> bytes = new ArrayList<>();
    private List<Bit> bitsBuffer = new ArrayList<>();
    private long significantBitsNumber = 0;
    // private List<String> bits = new ArrayList<>(); // debug only

    private byte formByte() {
        // bits.add(convertBitsToString(bitsBuffer));
        return Integer.valueOf(convertBitsToString(this.bitsBuffer), 2).byteValue();
    }

    public CompressionResultBuilder addBit(Bit bit) {
        if (this.bitsBuffer.size() < EIGHT_BITS) {
            this.bitsBuffer.add(bit);
            this.significantBitsNumber++;
        } else {
            this.bytes.add(this.formByte());
            this.bitsBuffer.clear();
            addBit(bit);
        }
        return this;
    }

    /**
     * Add trailing bits if needed.
     */
    public void addTrailingBits() {
        int bitsToAddNumber = EIGHT_BITS - convertBitsToString(this.bitsBuffer).length();
        if (bitsToAddNumber > 0) {
            for (int i = 0; i < bitsToAddNumber; i++) {
                this.bitsBuffer.add(ZERO);
            }
        }
        this.bytes.add(this.formByte());
    }

    public CompressionResultBuilder collectBits(byte[] inputDataBytes) {
        for (byte inputDataByte : inputDataBytes) {
            Bit[] bits = this.metadata.getCode(inputDataByte);
            for (Bit bit : bits) {
                this.addBit(bit);
            }
        }
        this.addTrailingBits();
        this.bitsBuffer.clear();
        // TODO: consider refactoring (metadata update; object state change)
        this.metadata.setSignificantBitsNumber(this.significantBitsNumber);
        return this;
    }

    public CompressionResultBuilder setMetadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public CompressionResult build() {
        System.out.println(this);
        return new CompressionResult(this.bytes, this.metadata);
    }

    @Override
    public String toString() {
        return "CompressionResultBuilder: \n" +
                "metadata=" + this.metadata +
                "bytes=" + Arrays.toString(this.bytes.toArray()); // + "\n" +
        // "bits=" + Arrays.toString(bits.toArray());
    }
}
