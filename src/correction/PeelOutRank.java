package correction;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/5/13
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class PeelOutRank {

    public static void writeOutputForRank(String[][] hierTable, String rank, File output) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(output));
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < hierTable[0].length; i++) {
            if (hierTable[0][i].contains("/")) {
                stringBuilder.append(hierTable[0][i].substring(hierTable[0][i].lastIndexOf("/")+1).replaceAll("\\.fasta\\.rdp",""));
            } else {
                stringBuilder.append(hierTable[0][i].replaceAll("\\.fasta\\.rdp",""));
            }
            stringBuilder.append('\t');
        }
        bufferedWriter.write(stringBuilder.toString());
        bufferedWriter.newLine();
        for (int i = 1; i < hierTable.length; i++) {
            String[] s = hierTable[i];
            if (s[3].contains(rank)) {
                stringBuilder = new StringBuilder();
                for (String cell : s) {
                    stringBuilder.append(cell);
                    stringBuilder.append('\t');
                }
                bufferedWriter.write(stringBuilder.toString());
                bufferedWriter.newLine();

            }
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    @Test
    public void peelOutRank() {
        try {
            String[][] table = RDPOutputReformatter.readTableFile(new File("/home/alext/Documents/Ocular Project/sequencing/HCE/publish/full.hier"));
            writeOutputForRank(table, "phylum", new File("/home/alext/Documents/Ocular Project/sequencing/HCE/publish/full.phylum.hier"));

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
