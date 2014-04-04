package publish;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 12/12/13
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class RDPUnclassifiedHunter {
    @Test
    public void hunt() {

        final File dir = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/publish");
        final File[] files = dir.listFiles();
        final String rdpExtention = ".rdp";
        final String fastaExtention = ".fasta";
        final String uclass = ".uclass";
        final List<File> rdps = new ArrayList<File>();
        final double cutoff = 0.8;
        for (File f : files) {
            if (f.getName().endsWith(fastaExtention.concat(rdpExtention))) {
                rdps.add(f);
            }
        }
        try {
            for (File f : rdps) {
                final StringBuilder stringBuilder = new StringBuilder();
                final List<String> rdpRows = Arrays.asList(FileUtils.readFileToString(f).split("\n"));
                final List<String> fastaRows=Arrays.asList(FileUtils.readFileToString(new File(dir,f.getName().replaceAll(rdpExtention,""))).split("\n"));


                for (int i=0;i<rdpRows.size();i++) {
                    final String s=rdpRows.get(i);
                    final String[] split = s.split("\t");
                    if (Double.parseDouble(split[split.length - 4]) < cutoff) {
                        stringBuilder.append(fastaRows.get(i*2));
                        stringBuilder.append('\n');
                        stringBuilder.append(fastaRows.get(i*2+1));
                        stringBuilder.append('\n');
                        continue;
                    }
                    if (Double.parseDouble(split[split.length - 1]) < cutoff) {
                        stringBuilder.append(fastaRows.get(i*2));
                        stringBuilder.append('\n');
                        stringBuilder.append(fastaRows.get(i*2+1));
                        stringBuilder.append('\n');
                    }
                }
                FileUtils.writeStringToFile(new File(dir,f.getName().replace(rdpExtention,uclass)),stringBuilder.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}