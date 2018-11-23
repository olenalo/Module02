package huffman;

import configs.Bit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static utils.ConversionUtils.convertBitsToString;

public class Metadata implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<Integer, Bit[]> decodingTable;
    private long significantBitsNumber;

    public static String getStringBit(Bit bit) {
        String b = null;
        if (bit == Bit.ZERO) {
            b = "0";
        } else if (bit == Bit.ONE) {
            b = "1";
        }
        return b;
    }

    public Metadata(Map<Integer, Bit[]> decodingTable) {
        this.decodingTable = decodingTable;
    }

    public Map<Integer, Bit[]> getDecodingTable() {
        return decodingTable;
    }

    public Map<Integer, String> getConvertedDecodingTable() {
        Map<Integer, String> convertedTable = new HashMap<>();
        for (Map.Entry<Integer, Bit[]> entry : this.decodingTable.entrySet()) {
            convertedTable.put(
                    entry.getKey(),
                    convertBitsToString(new ArrayList<>(Arrays.asList(entry.getValue()))));
        }
        return convertedTable;
    }

    public long getSignificantBitsNumber() {
        return significantBitsNumber;
    }

    public void setSignificantBitsNumber(long significantBitsNumber) {
        this.significantBitsNumber = significantBitsNumber;
    }

    public Bit[] getCode(byte aByte) {
        return decodingTable.get((int) aByte & 0xFF);
    }

    /**
     * Get the first matched key with a given value.
     *
     * @param value value to find in a map.
     * @return first matched key associated to a given value
     * or -1 if none found.
     */
    public int getKeyByValue(String value) {
        for (Map.Entry<Integer, Bit[]> entry : this.getDecodingTable().entrySet()) {
            if (value.equals(convertBitsToString(new ArrayList<>(Arrays.asList(entry.getValue()))))) {
                return entry.getKey();
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(
                "Metadata: " +
                        "\nsignificantBitsNumber - " + significantBitsNumber +
                        "; \ndecodingTable - ");
        for (Map.Entry<Integer, Bit[]> entry : this.decodingTable.entrySet()) {
            stringBuilder
                    .append(entry.getKey())
                    .append(": ")
                    .append(convertBitsToString(new ArrayList<>(Arrays.asList(entry.getValue()))))
                    .append("; ");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

}
