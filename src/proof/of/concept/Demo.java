package proof.of.concept;

import com.company.Configs;

import java.util.Scanner;

import static com.company.IOUtils.getDecompressionFileName;
import static proof.of.concept.DemoCompression.compress;
import static proof.of.concept.DemoDecompression.decompress;

public class Demo {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the initial file name: ");
        String initialFileName = scanner.nextLine();

        int lastIndex = initialFileName.lastIndexOf(".");
        if (lastIndex == -1) {
            throw new IllegalArgumentException("Filename should contain extension.");
        }
        String inputFileExtension = initialFileName.substring(lastIndex);

        if(inputFileExtension.equals(".hf")){
            decompress(initialFileName, Configs.METADATA_TABLE_FILENAME, getDecompressionFileName());
        } else {
            compress(initialFileName, Configs.METADATA_TABLE_FILENAME, Configs.COMPRESSED_FILENAME);
        }
    }
}
