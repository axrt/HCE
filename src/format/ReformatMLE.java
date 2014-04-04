package format;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alext on 4/2/14.
 */
public class ReformatMLE {


    public static void main(String[] arg) {

        final File f0 = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/kerat.genus.mle");
        final File f1 = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/healthy.genus.mle");
        final File f2 = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/male.genus.mle");
        final File f3 = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/female.genus.mle");
        final List<File> files = new ArrayList<>(4);
        files.add(f0);
        files.add(f1);
        files.add(f2);
        files.add(f3);
        for (File f : files) {
            try (final BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
                 final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(f.getParent(), f.getName().concat(".formatted"))));
            ) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    final String[] split = line.split("\t");
                    final String[] taxaSplit = split[0].split("\\.");
                    if (!taxaSplit[taxaSplit.length - 1].endsWith("U")) {
                        bufferedWriter.write(line);
                        bufferedWriter.newLine();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
