package correction;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/4/13
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class CombinedFNACreator {

    @Test
    public void combineFNA() {
       //load the barcode list
        try {


            List<String> barcodes=loadBarcodes(new File("/home/alext/Documents/Ocular Project/sequencing/HCE/barcodes.txt"));
            for(String s:barcodes){
                List<FNArecord>fnArecords=readAndParseFNAFile(new File("/home/alext/Documents/Ocular Project/sequencing/HCE/Fasta/"+s+".fsa"));
                fnArecords=appendStringToFNAAc(s.toUpperCase(),fnArecords);
                saveFNAFile(fnArecords,new File("/home/alext/Documents/Ocular Project/sequencing/HCE/correct_fna/"+s.toUpperCase()+".fna"));
            }




        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public static List<FNArecord> appendStringToFNAAc(String append,List<FNArecord> fnArecords){
        int i=0;
        for(FNArecord f:fnArecords){
           f.appendToAc(append+"_"+i);
           i++;
       }
       return fnArecords;
    }

    public static List<FNArecord> readAndParseFNAFile(File file) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line.trim());
            stringBuilder.append('\n');
        }

        String[] split = stringBuilder.toString().split(">");
        List<FNArecord> fnArecords = new ArrayList<FNArecord>(split.length - 1);
        for (int i = 1; i < split.length; i++) {
            String[] subSplit = split[i].split("\n");
            fnArecords.add(new FNArecord(">" + subSplit[0], subSplit[1]));
        }
        bufferedReader.close();
        return fnArecords;
    }

    public static void saveFNAFile(List<FNArecord> fnaRecords, File file) throws IOException {
        BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(file));
        for(FNArecord f:fnaRecords){
            bufferedWriter.write(f.toString());
            bufferedWriter.newLine();
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    public static List<String> loadBarcodes(File file)throws IOException{

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line.trim());
            stringBuilder.append('\n');
        }
        List<String> barcodes=new ArrayList<String>();
        String[]split=stringBuilder.toString().split("\n");
        for(String s:split){
            barcodes.add(s);
        }
        bufferedReader.close();
        return barcodes;
    }

    public static class FNArecord {
        private String ac;
        private String sequence;

        public FNArecord(String ac, String sequence) {
            this.ac = ac;
            this.sequence = sequence;
        }

        public String getAc() {
            return ac;
        }

        public String getSequence() {
            return sequence;
        }

        public void appendToAc(String append) {
            this.ac = '>'+append + " " + ac.substring(1);
        }

        @Override
        public String toString() {
            return this.ac + '\n' + this.sequence;
        }
    }
}
