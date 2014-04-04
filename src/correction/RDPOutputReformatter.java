package correction;

import org.junit.Test;

import java.io.*;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/5/13
 * Time: 3:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class RDPOutputReformatter {

    public static String[][] readTableFile(File file) throws IOException {

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file));){
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append('\n');
        }
        String[] split = stringBuilder.toString().split("\n");
        String[][] table = new String[split.length][];
        int i = 0;
        for (String s : split) {
            table[i] = s.split("\t");
            i++;
        }
            return table;
        }
    }
    public static void writeTable(String [][] table,File outfile) throws IOException {
        BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(outfile));
        for(int i=0;i< table.length;i++){
            if(table[i]==null){
                continue;
            }
            StringBuilder stringBuilder=new StringBuilder();
            for(int j=0;j<table[i].length;j++){
                stringBuilder.append(table[i][j]);
                stringBuilder.append('\t');
            }
            bufferedWriter.write(stringBuilder.toString());
            bufferedWriter.newLine();
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    @Test
    public void reformatRDPOutput() {


        try {
            String[][] table = readTableFile(new File("/home/alext/Documents/Ocular Project/sequencing/HCE/reseqAnalysis/Fasta/healthy/hmp/mf.hier"));
            //Getting rid of the taxid and the two rank columns

            for (int i = 0; i < table.length; i++) {

                String[] reformattedTabelRow = Arrays.copyOfRange(table[i], 1, table[i].length);
                String[] trimmedRow = new String[reformattedTabelRow.length - 2];
                trimmedRow[0]=reformattedTabelRow[0];
                for(int j=3;j<reformattedTabelRow.length;j++){
                    trimmedRow[j-2]=reformattedTabelRow[j];
                }
                table[i]=trimmedRow;

            }
            for(int i=0;i<table[0].length;i++){
                if (table[0][i].contains("/")) {
                    table[0][i]=table[0][i].substring(table[0][i].lastIndexOf("/")+1).replaceAll("\\.fasta\\.rdp","");
                } else {
                    table[0][i]=table[0][i].replaceAll("\\.fasta\\.rdp","");
                }
            }
            //Correct the ";" to "." and put the ranks in brackets, remove unclassified
            for (int i = 1; i < table.length; i++) {
                //table[i][0]=table[i][0].replaceAll("Root;","Root <root>;");
                table[i][0]=table[i][0].replaceAll("Root;","");
                table[i][0]=table[i][0].replaceAll("norank;","");
                table[i][0]=table[i][0].replaceAll("rootrank.","");
                table[i][0]=table[i][0].replaceAll(";", ".");
                table[i][0]=table[i][0].replaceAll("\"", "");
                table[i][0]=table[i][0].replaceAll("\\.\\.", "");
                table[i][0]=table[i][0].replaceAll("\\.domain", "<domain>");
                table[i][0]=table[i][0].replaceAll("\\.phylum", "<phylum>");
                table[i][0]=table[i][0].replaceAll("\\.subclass", "<subclass>");
                table[i][0]=table[i][0].replaceAll("\\.order", "<order>");
                table[i][0]=table[i][0].replaceAll("\\.suborder", "<suborder>");
                table[i][0]=table[i][0].replaceAll("\\.family", "<family>");
                table[i][0]=table[i][0].replaceAll("\\.subfamily", "<subfamily>");
                table[i][0]=table[i][0].replaceAll("\\.genus", "<genus>");

                if(table[i][0].contains("unclassified")){
                    table[i]=null;
                    continue;
                    /*String[]split=table[i][0].split("\\.");
                    StringBuilder stringBuilder=new StringBuilder();
                    for(int j=0;j<split.length-1;j++){
                       stringBuilder.append(split[j].replaceAll("unclassified_",""));
                       stringBuilder.append('.');
                    }
                    table[i][0]=stringBuilder.toString();*/
                }

                table[i][0]=table[i][0].substring(0,table[i][0].length()-1);
                table[i][0]=table[i][0].replaceAll("\\.class", "<class>");

            }
            table[1]=null;
            writeTable(table,new File("/home/alext/Documents/Ocular Project/sequencing/HCE/reseqAnalysis/Fasta/healthy/hmp/mf.hier"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}
