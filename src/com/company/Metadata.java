package com.company;

import java.io.Serializable;
import java.util.Map;

public class Metadata implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<Integer, String> decodingTable;   // TODO consider <Integer, Bit>
    private long significantBitsNumber;

    public Metadata(Map<Integer, String> decodingTable) {
        this.decodingTable = decodingTable;  // TODO can be null, handle it
    }

    public Map<Integer, String> getDecodingTable() {
        return decodingTable;
    }

    public long getSignificantBitsNumber() {
        return significantBitsNumber;
    }

    public void setSignificantBitsNumber(long significantBitsNumber) {
        this.significantBitsNumber = significantBitsNumber;
    }

    public String getCode(byte aByte) {
        // TODO: consider getting rid of casting here
        return decodingTable.get((int) aByte & 0xFF);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(
                "Metadata: " +
                "\nsignificantBitsNumber - " + significantBitsNumber +
                "; \ndecodingTable - ");
        for (Map.Entry <Integer, String> entry: this.decodingTable.entrySet()) {
            stringBuilder
                    .append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue()).append("; ");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

}
