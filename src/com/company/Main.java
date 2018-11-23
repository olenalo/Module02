package com.company;

import java.util.Scanner;

import static com.company.IOUtils.getDecompressionFileName;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the initial file name: ");
        String initialFileName = scanner.nextLine();
        String inputFileExtension = IOUtils.checkFilenameExtension(initialFileName);

        if(inputFileExtension.equals(Configs.COMPRESSED_FILE_EXTENSION)){
            String decompressionFileName = getDecompressionFileName();
            Decompressor decompressor = new Decompressor(IOUtils.readFile(initialFileName),
                                                         IOUtils.readMetadata(Configs.METADATA_TABLE_FILENAME),
                                                         decompressionFileName);
            decompressor.decompress().save();
        } else {
            Compressor compressor = new Compressor(IOUtils.readFile(initialFileName),
                                                   Configs.COMPRESSED_FILENAME);
            compressor.compress().save();
        }
    }

}
