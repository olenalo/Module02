package utils;

import huffman.Metadata;

import java.io.*;
import java.util.Scanner;

public class IOUtils {

    public static String checkFilenameExtension(String filename) {
        int lastIndex = filename.lastIndexOf(".");
        if (lastIndex == -1) {
            throw new IllegalArgumentException("Filename should contain the extension.");
        }
        return filename.substring(lastIndex);
    }

    public static String getDecompressionFileName() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter the decompressed file name: ");
            return scanner.next();
        }
    }
    public static byte[] readFile(String filename) {
        byte[] content = null;
        try (FileInputStream fis = new FileInputStream(new File(filename))) {
            int i;
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
