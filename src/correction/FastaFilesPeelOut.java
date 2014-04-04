package correction;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/4/13
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class FastaFilesPeelOut {

    @Test
    public void peelOutFastasByBarcode() {

        try {
            List<String> barcodes = CombinedFNACreator.loadBarcodes(new File("/home/alext/Documents/Ocular Project/sequencing/HCE/october11/barcodes.txt"));
            File[] files = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/reseq/Fasta").listFiles();
            for (String s : barcodes) {
                for (File f : files) {
                    if (f.getName().toUpperCase().split("\\.")[0].equals(s.toUpperCase())){
                        FileUtils.copyFile(f,new File("/home/alext/Documents/Ocular Project/sequencing/HCE/reseq/HCE Fasta/"+f.getName()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}
