package tmp;

import format.fasta.Fasta;
import org.junit.Test;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 12/9/13
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClassificationRemover {
    @Test
    public void remove() {
        try {
            final File file = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/QUIIME_approach/Fasta/full.fasta");
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            final BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(new File(file.getParent(), "full.clean.fasta")));

            final StringBuilder stringBuilder=new StringBuilder();
            String line;
            while((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
            final String[]split=stringBuilder.toString().split(Fasta.fastaStart);
            for(int i=1;i<split.length;i++){
                final String s=split[i];
                final String[]subsplit=s.split("\n");
                bufferedWriter.write(Fasta.fastaStart);
                bufferedWriter.write(subsplit[0].split("\t")[0]);
                bufferedWriter.newLine();
                bufferedWriter.write(subsplit[1]);
                bufferedWriter.newLine();
            }
            bufferedReader.close();
            bufferedWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
