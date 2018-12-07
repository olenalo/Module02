package huffman;

import configs.Bit;

import java.util.*;

import static configs.Bit.ZERO;
import static configs.Configs.EIGHT_BITS;
import static utils.ConversionUtils.convertBitsToString;

public class CompressionResultBuilder { // TODO make inner class
    private Metadata metadata;
    private List<Byte> bytes = new ArrayList<>();
    private List<Bit> bitsBuffer = new ArrayList<>();
    private long significantBitsNumber = 0;
    // private List<String> bits = new ArrayList<>(); // debug only

    private byte formByte() {
        // bits.add(convertBitsToString(bitsBuffer));
        return Integer.valueOf(convertBitsToString(bitsBuffer), 2).byteValue();
    }

    public CompressionResultBuilder addBit(Bit bit) {
        if (bitsBuffer.size() < EIGHT_BITS) {
            bitsBuffer.add(bit);
            significantBitsNumber++;
        } else {
            bytes.add(this.formByte());
            bitsBuffer.clear();
            addBit(bit);
        }
        return this;
    }

    /**
     * Add trailing bits if needed.
     */
    public void addTrailingBits() {
        int bitsToAddNumber = EIGHT_BITS - convertBitsToString(bitsBuffer).length();
        if (bitsToAddNumber > 0) {
            for (int i = 0; i < bitsToAddNumber; i++) {
                bitsBuffer.add(ZERO);
            }
        }
        bytes.add(this.formByte());
    }

    public CompressionResultBuilder collectBits(byte[] inputDataBytes) {
        for (byte inputDataByte : inputDataBytes) {
            Bit[] bits = metadata.getCode(inputDataByte);
            for (Bit bit : bits) {
                this.addBit(bit);
            }
        }
        this.addTrailingBits();
        bitsBuffer.clear();
        // TODO: consider refactoring (metadata update; object state change)
        metadata.setSignificantBitsNumber(significantBitsNumber);
        return this;
    }

    public CompressionResultBuilder setMetadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    public CompressionResult build() {
        System.out.println(this);
        return new CompressionResult(bytes, metadata);
    }

    @Override
    public String toString() {
        return "CompressionResultBuilder: \n" +
                "metadata=" + metadata +
                "bytes=" + Arrays.toString(bytes.toArray()); // + "\n" +
        // "bits=" + Arrays.toString(bits.toArray());
    }
}
