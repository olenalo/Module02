package huffman;

import configs.Bit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static utils.Utils.convertBitsToString;

public class Metadata implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<Integer, Bit[]> decodingTable;
    private long significantBitsNumber;

    public Metadata(Map<Integer, Bit[]> decodingTable) {
        this.decodingTable = decodingTable;
    }

    public Map<Integer, Bit[]> getDecodingTable() {
        return decodingTable;
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
