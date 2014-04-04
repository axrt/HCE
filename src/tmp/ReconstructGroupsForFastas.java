package tmp;

import format.fasta.Fasta;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 12/9/13
 * Time: 3:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReconstructGroupsForFastas {
    @Test
    public void reconstruct() {

        try {
            //Collect the fasta files from directory
            final File dir = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/reseqAnalysis/Fasta/healthy");
            final File[] files = dir.listFiles();
            final List<File> fastas = new ArrayList<File>();
            for (File f : files) {
                if (f.getName().endsWith(".fasta")) {
                    fastas.add(f);
                }
            }
            //For all fastas reconstruct the groups
            final StringBuilder groupsBuilder = new StringBuilder();
            for (File f : fastas) {
                //Open file
                final String group = f.getName().replaceAll("\\.fasta", "");
                final String fileContent = FileUtils.readFileToString(f);
                final String[] split = fileContent.split(Fasta.fastaStart);
                for (int i = 1; i < split.length; i++) {
                    groupsBuilder.append(split[i].split("\n")[0].split("\t")[0]);
                    groupsBuilder.append('\t');
                    groupsBuilder.append(group);
                    groupsBuilder.append('\n');
                }
            }

            FileUtils.writeStringToFile(new File(dir, "hce.groups"), groupsBuilder.toString());

        } catch (Exception e) {

        }


    }
}
