package correction;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/7/13
 * Time: 11:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class TableRenamer {

    @Test
    public void renameTable(){

        try {

            String[][]table=RDPOutputReformatter.readTableFile(new File("/home/alext/Documents/Ocular Project/sequencing/HCE/25Mar2013/hce fasta/seqs.hier"));
            String[][]lookupNames=RDPOutputReformatter.readTableFile(new File("/home/alext/Documents/Ocular Project/sequencing/HCE/25Mar2013/hce fasta/seqs.hier"));
            Map<String,String>lookupMap=getMapFromLookupTable(lookupNames);
            for(int i=4;i<table[0].length;i++){
                table[0][i]=lookupMap.get(table[0][i]);
            }
            RDPOutputReformatter.writeTable(table,new File("/home/alext/Documents/Ocular Project/sequencing/HCE/12Aug2013/Fasta/seqs.hier.names"));


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public static Map<String,String> getMapFromLookupTable(String[][]lookuptable){
        Map<String,String>namesMap=new HashMap<String, String>(lookuptable.length);
        for(String []s:lookuptable){
            namesMap.put((s[1]+".fasta"),s[0]);
        }
        return namesMap;
    }
}
