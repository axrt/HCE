package mothur.test;

import mothur.SummarySeqOutput;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/13/13
 * Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class SummarySeqOutputTest {
    @Test
    public void testSummarySeqOutput(){
        File f=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/combined HCE/Fasta/13dec2010/seq/HCE1.fasta.sum");
        try {
            SummarySeqOutput summarySeqOutput=SummarySeqOutput.newInstaceFromFile(f);
            System.out.println(summarySeqOutput.getNumberOfSeqs());
            summarySeqOutput.getTable();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
