package utils;

import java.util.ArrayList;

public class Utils {

    public static byte[] convertToByteArray(ArrayList<Byte> bytes) {
        byte[] compressionResult = new byte[bytes.size()];
        for (int i = 0; i < compressionResult.length; i++) {
            compressionResult[i] = bytes.get(i);
        }
        return compressionResult;
    }

}
