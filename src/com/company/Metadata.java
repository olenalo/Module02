package com.company;

import java.io.Serializable;
import java.util.Map;

public class Metadata implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<Integer, String> decodingTable;   // TODO consider <Byte, String>
    private long significantBitsNumber;

    public Metadata(Map<Integer, String> decodingTable) {
        this.decodingTable = decodingTable;
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
}
