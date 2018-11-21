package com.company;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class IOUtils {

    public static byte[] readFile(String filename) {
        byte[] content = null;
        int i;
        try (FileInputStream fis = new FileInputStream(new File(filename))) {
            content = new byte[fis.available()];
            int counter = 0;
            // Read till the end of the stream
            while((i = fis.read())!=-1) {
                content[counter] = (byte)i;
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static Metadata readMetadata(String dictPath) {
        Metadata metadata = null;
        try (FileInputStream fis = new FileInputStream(dictPath)) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            metadata = (Metadata) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return metadata;
    }

    public static void writeFile(byte[] content,
                                 Metadata metadata,
                                 String compressedFileName,
                                 String treeFileName) {
        try (FileOutputStream compressedFos = new FileOutputStream(new File(compressedFileName));
             FileOutputStream treeFos = new FileOutputStream(new File(treeFileName))) {

            ObjectOutputStream oos = new ObjectOutputStream(treeFos);
            oos.writeObject(metadata);
            oos.close();
            compressedFos.write(content);
            // Метод write берет первый байт из числа и записывает.
            // Чтобы записать 32-битное число, нам нужно записать каждый его бит отдельно.
            /*
            //
            compressedFos.write((byte)initialSize >> 0);
            compressedFos.write((byte)initialSize >> 8);
            compressedFos.write((byte)initialSize >> 16);
            compressedFos.write((byte)initialSize >> 24); // TODO consider
            for (int b : content) {
                compressedFos.write(b);
            }
            */
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeDecompressedFile(byte[] content, String filePath) {
        try (FileOutputStream dfos = new FileOutputStream(filePath)) {
            dfos.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }




}
