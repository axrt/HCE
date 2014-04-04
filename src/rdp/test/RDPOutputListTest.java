package rdp.test;

import org.junit.Test;
import rdp.RDPOutputList;

import java.io.File;
import java.io.IOException;

/**
 * Created by alext on 3/26/14.
 */
public class RDPOutputListTest {


    @Test
    public void test()
    {
        try {
            final RDPOutputList rdpOutputs=RDPOutputList.newInstanceFromFileAndParams(
                    new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/kerat.rdp"),".",0.8,false);
            System.out.println("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
