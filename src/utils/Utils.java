package utils;

import configs.Bit;

import java.util.ArrayList;

public class Utils {

    /**
     * Convert to an array of the primitive `byte` type.
     *
     * @param list list to convert.
     * @return array of primitive type.
     */
    public static byte[] convertToByteArray(ArrayList<Byte> list) {
        byte[] array = new byte[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static Bit[] convertToBitArray(ArrayList<Bit> list) {
        Bit[] array = new Bit[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    // TODO consider moving to `Metadata`
    /**
     * Convert bits to string of 0 and 1.
     *
     * @param bits list of bits.
     * @return string of bits represented as 0 and 1.
     */
    public static String convertBitsToString(ArrayList<Bit> bits) {
        StringBuilder bitsString = new StringBuilder();
        for (Bit bit : bits) {
            if (bit == Bit.ZERO) {
                bitsString.append("0");
            } else if (bit == Bit.ONE) {
                bitsString.append("1");
            }
        }
        return bitsString.toString();
    }

}
