package rdp.test;

import org.junit.Test;
import rdp.RDPOutput;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/13/13
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class RDPOutputTest {

    @Test
    public void testRDPOutput(){
        try {
            BufferedReader bufferedReader=new BufferedReader(new FileReader(new File("/home/alext/Documents/Ocular Project/sequencing/HCE/report/rdp/HCE11.fasta.rdp")));
            String line;
            List<String>lines=new ArrayList<String>();
            while((line=bufferedReader.readLine())!=null){
                  lines.add(line);
            }

            RDPOutput rdpOutput=RDPOutput.newInstanceFromString(lines.get(0));
            System.out.print("");


        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
