package com.company;

import java.util.Scanner;

import static com.company.IOUtils.getDecompressionFileName;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the initial file name: ");
        String initialFileName = scanner.nextLine();

        int lastIndex = initialFileName.lastIndexOf(".");
        if (lastIndex == -1) {
            throw new IllegalArgumentException("Filename should contain the extension.");
        }
        String inputFileExtension = initialFileName.substring(lastIndex);

        if(inputFileExtension.equals(".hf")){
            String decompressionFileName = getDecompressionFileName();
            Decompressor decompressor = new Decompressor(IOUtils.readFile(initialFileName),
                                                         IOUtils.readMetadata(Configs.METADATA_TABLE_FILENAME),
                                                         decompressionFileName);
            decompressor.decompress();
            decompressor.save();
        } else {
            CompressionResult result = new Compressor(
                    IOUtils.readFile(initialFileName),
                    Configs.COMPRESSED_FILENAME) // TODO consider moving to user input
                    .compress();
            result.save(); // TODO compressor should do it
        }
    }

}
