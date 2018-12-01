import huffman.Compressor;
import huffman.Decompressor;
import huffman.Processor;

import java.util.Scanner;

import static configs.Configs.COMPRESSED_FILENAME;
import static configs.Configs.COMPRESSED_FILE_EXTENSION;
import static configs.Configs.METADATA_TABLE_FILENAME;
import static utils.IOUtils.checkFilenameExtension;
import static utils.IOUtils.getDecompressionFileName;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the initial file name: ");
        String initialFileName = scanner.nextLine();
        String inputFileExtension = checkFilenameExtension(initialFileName);
        Processor processor;
        if (COMPRESSED_FILE_EXTENSION.equals(inputFileExtension)) {
            String decompressionFileName = getDecompressionFileName();
            processor = new Decompressor(initialFileName, METADATA_TABLE_FILENAME, decompressionFileName);
        } else {
            processor = new Compressor(initialFileName, COMPRESSED_FILENAME);
        }
        processor.process().save();
    }

}
