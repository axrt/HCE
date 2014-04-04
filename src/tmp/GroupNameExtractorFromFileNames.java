package tmp;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 12/10/13
 * Time: 12:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroupNameExtractorFromFileNames {
    @Test
    public void extract() {

        final File dir = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/reseqAnalysis/Fasta/keratitis");
        final String extention = ".fasta";
        final String signed = ".signed";
        final List<File> fastas = new ArrayList<File>();
        final File[] files = dir.listFiles();
        for (File f : files) {
            if (f.getName().endsWith(extention)) {
                fastas.add(f);
            }
        }
        for(File f:fastas){
            if(!f.getName().contains(signed)){
                System.out.println(f.getName().replaceAll(extention,""));
            }
        }
    }

}
