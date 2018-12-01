package huffman;

import configs.Bit;

import java.util.*;

import static configs.Bit.ZERO;
import static configs.Configs.EIGHT_BITS;
import static utils.ConversionUtils.convertBitsToString;

public class CompressionResultBuilder {
    private Metadata metadata;
    private ArrayList<Byte> bytes = new ArrayList<>();
    private ArrayList<Bit> bitsBuffer = new ArrayList<>();
    private long significantBitsNumber = 0;
    private int bitsBufferCounter = 0;
    private ArrayList<String> bits = new ArrayList<>(); // debug only

    private void formByte() {
        String bitsString = convertBitsToString(bitsBuffer);
        bits.add(bitsString);
        bytes.add(Integer.valueOf(bitsString, 2).byteValue());
        bitsBuffer.clear();
    }

    public CompressionResultBuilder addBit(Bit bit) {
        if (bitsBufferCounter < EIGHT_BITS) {
            bitsBuffer.add(bit);
            bitsBufferCounter++;
            significantBitsNumber++;
        } else {
            this.formByte();
            bitsBufferCounter = 0;
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
        this.formByte();
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
                "bits=" + Arrays.toString(bits.toArray());
    }
}
