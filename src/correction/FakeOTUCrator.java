package correction;

import org.junit.Test;

import java.io.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/4/13
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class FakeOTUCrator {
    @Test
    public void createFakeOTU(){

        try {
            createFakeOTUFromFastaFile(new File("/home/alext/Documents/Ocular Project/sequencing/HCE/correct_fna/seqs.fna"),new File("/home/alext/Documents/Ocular Project/sequencing/HCE/correct_fna/table.txt"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }
    public static void createFakeOTUFromFastaFile(File fasta, File fakeTable) throws IOException {

        List<CombinedFNACreator.FNArecord> fnArecords=CombinedFNACreator.readAndParseFNAFile(fasta);
        StringBuilder stringBuilder=new StringBuilder();
        int i=0;
        for(CombinedFNACreator.FNArecord fnArecord:fnArecords){
            stringBuilder.append(i);
            stringBuilder.append('\t');
            stringBuilder.append(fnArecord.getAc().substring(1).split(" ")[0]);
            stringBuilder.append('\n');
                    i++;
        }
        BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(fakeTable));
        bufferedWriter.write(stringBuilder.toString());
        bufferedWriter.flush();
        bufferedWriter.close();
    }

}
