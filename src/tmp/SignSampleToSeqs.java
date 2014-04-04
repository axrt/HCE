package tmp;

import format.fasta.Fasta;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 12/9/13
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class SignSampleToSeqs {


    public static String sign(final File rawFile, final String extention) throws IOException {

        //Read file
        final String content = FileUtils.readFileToString(rawFile);
        final String[] split = content.split(Fasta.fastaStart);
        final StringBuilder stringBuilder = new StringBuilder();
        final String sampleName = rawFile.getName().replaceAll(extention, "");
        for (int i = 1; i < split.length; i++) {
            final String[]subsplit=split[i].split("\n");
            stringBuilder.append(Fasta.fastaStart);
            stringBuilder.append(sampleName);
            stringBuilder.append('_');
            stringBuilder.append(i);
            stringBuilder.append(' ');
            stringBuilder.append(subsplit[0].split("\t")[0]);
            stringBuilder.append('\n');
            stringBuilder.append(subsplit[1]);
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    @Test
    public void sign() {
        try {
            //Read file
            final File dir = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/publish");
            final File[] files = dir.listFiles();
            final List<File> fastas = new ArrayList<File>();
            final String extention = ".fasta";
            for (File f : files) {
                if (f.getName().endsWith(extention)) {
                    fastas.add(f);
                }
            }
            for (File f : fastas) {
                FileUtils.writeStringToFile(new File(dir, f.getName().replaceAll(extention, ".signed" + extention)), sign(f, extention));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
