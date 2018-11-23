package proof.of.concept;

import com.company.Configs;
import com.company.IOUtils;

import java.util.Scanner;

import static com.company.IOUtils.getDecompressionFileName;
import static proof.of.concept.DemoCompression.compress;
import static proof.of.concept.DemoDecompression.decompress;

public class Demo {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the initial file name: ");
        String initialFileName = scanner.nextLine();
        String inputFileExtension = IOUtils.checkFilenameExtension(initialFileName);

        if(inputFileExtension.equals(Configs.COMPRESSED_FILE_EXTENSION)){
            decompress(initialFileName, Configs.METADATA_TABLE_FILENAME, getDecompressionFileName());
        } else {
            compress(initialFileName, Configs.METADATA_TABLE_FILENAME, Configs.COMPRESSED_FILENAME);
        }
    }
}
