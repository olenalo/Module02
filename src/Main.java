import configs.Configs;
import huffman.Compressor;
import huffman.Decompressor;
import huffman.Processor;
import utils.IOUtils;

import java.util.Scanner;

import static utils.IOUtils.getDecompressionFileName;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the initial file name: ");
        String initialFileName = scanner.nextLine();
        String inputFileExtension = IOUtils.checkFilenameExtension(initialFileName);
        Processor processor;
        if (inputFileExtension.equals(Configs.COMPRESSED_FILE_EXTENSION)) {
            String decompressionFileName = getDecompressionFileName();
            processor = new Decompressor(initialFileName, Configs.METADATA_TABLE_FILENAME, decompressionFileName);
        } else {
            processor = new Compressor(initialFileName, Configs.COMPRESSED_FILENAME);
        }
        processor.process().save();
    }

}
