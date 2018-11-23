package huffman;

import configs.Bit;
import configs.Configs;

import java.util.*;

import static utils.Utils.convertBitsToString;

public class CompressionResultBuilder {
    private Metadata metadata;
    private ArrayList<Byte> bytes = new ArrayList<>();
    private ArrayList<Bit> bitsCash = new ArrayList<>();
    private long significantBitsNumber = 0;
    private int bitsCashCounter = 0;
    private ArrayList<String> bits = new ArrayList<>(); // debug only

    private void formByte() {
        String bitsString = convertBitsToString(bitsCash);
        bits.add(bitsString);
        bytes.add(Integer.valueOf(bitsString, 2).byteValue());
        bitsCash.clear();
    }

    public CompressionResultBuilder addBit(Bit bit, boolean isLastByte) {
        if (bitsCashCounter < Configs.EIGHT_BITS) {
            bitsCash.add(bit);
            bitsCashCounter++;
            significantBitsNumber++;
            // TODO `addBit()` should add a singe bit as a method name suggests (add trailing bits somewhere else)
            if (isLastByte) {
                // Add trailing bits if needed
                int bitsToAddNumber = Configs.EIGHT_BITS - convertBitsToString(bitsCash).length();
                if (bitsToAddNumber > 0) {
                    for (int i = 0; i < bitsToAddNumber; i++) {
                        bitsCash.add(Bit.ZERO);
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
                "bits=" + Arrays.toString(bits.toArray());
    }
}
