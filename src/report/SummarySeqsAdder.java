package report;

import correction.RDPOutputReformatter;
import mothur.SummarySeqOutput;
import org.junit.Test;
import rdp.RDPOutput;
import rdp.RDPOutputList;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/13/13
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class SummarySeqsAdder {

    public static List<SummarySeqOutput> summariesFromFiles(File[] inputFiles) throws IOException {
        List<SummarySeqOutput> summarySeqOutputs = new ArrayList<SummarySeqOutput>(inputFiles.length);
        for (File f : inputFiles) {
            if (f.exists()) {
                summarySeqOutputs.add(SummarySeqOutput.newInstaceFromFile(f));
            } else {
                System.out.println("No such file for " + f.getName() + " in " + f.getParent());
            }
        }
        return summarySeqOutputs;
    }
    public static List<RDPOutputList> rdpOutputListsFromFiles(File[] inputFiles) throws IOException{
        List<RDPOutputList> rdpOutputLists=new ArrayList<RDPOutputList>(inputFiles.length);
        for(File f:inputFiles){
            if(f.exists()){
                if (f.exists()) {
                    rdpOutputLists.add(RDPOutputList.newInstanceFromFile(f));
                } else {
                    System.out.println("No such file for " + f.getName() + " in " + f.getParent());
                }
            }
        }
        return rdpOutputLists;
    }

    @Test
    public void addSeqsInfo() {
        try {
            String[][] table = RDPOutputReformatter.readTableFile(new File("/home/alext/Documents/Ocular Project/sequencing/HCE/report/Patient_info_samples_TRimmed_Val_Oct2013.txt"));
            List<String> names = new ArrayList<String>();
            int numberOfSamples=79;
            List<String>[] addedTable= new ArrayList[numberOfSamples];
            Map<String, Integer> postionMap=new HashMap<String,Integer>();
            addedTable[0]=new ArrayList<String>(table[0].length);
            for(int j=0;j<table[0].length;j++){
                addedTable[0].add(table[0][j]);
            }
            for(int i=0;i<6;i++){
                addedTable[0].add("");
            }
            addedTable[0].add("Number of quality Sequences");
            addedTable[0].add("Min Sequence length");
            addedTable[0].add("Mean Sequence length");
            addedTable[0].add("Median Sequence length");
            addedTable[0].add("Max Sequence length");

            for (int i = 1; i < numberOfSamples; i++) {
                String name="";
                if (table[i][2].trim().equalsIgnoreCase("Yes")&&table[i][3].trim().equalsIgnoreCase("Yes")) {

                    names.add( (name=
                            table[i][0].trim().replaceAll("444", "").replaceAll("UM", "").replaceAll("\\.", "").replaceAll("-", "").replaceAll("_", ""))
                    );

                }
                postionMap.put(name,i);
                addedTable[i]=new ArrayList<String>(table[i].length);
                for(int j=0;j<table[i].length;j++){
                    addedTable[i].add(table[i][j]);
                }
            }
            //add the .fasta.sum
            File[]files=new File[names.size()];
            for(int i=0;i<names.size();i++){
                 files[i]=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/report/sum/".concat(names.get(i)).concat(".fasta.sum"));
            }
            //get the summaries
            List<SummarySeqOutput> summarySeqOutputs=summariesFromFiles(files);
            //And add the required info
            for(SummarySeqOutput summarySeqOutput:summarySeqOutputs){
                int pos=postionMap.get(summarySeqOutput.getName().split("\\.")[0]);
                //Number of quality seqs
                addedTable[pos].add(String.valueOf(summarySeqOutput.getNumberOfSeqs()));
                //min bases
                addedTable[pos].add(String.valueOf(summarySeqOutput.getMinNBases()));
                //mean bases
                addedTable[pos].add(String.valueOf(summarySeqOutput.getMeanNBases()));
                //median bases
                addedTable[pos].add(String.valueOf(summarySeqOutput.getMedianNBases()));
                //max bases
                addedTable[pos].add(String.valueOf(summarySeqOutput.getMaxNBases()));

            }

            //create a list of RDP files
            for(int i=0;i<names.size();i++){
                files[i]=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/report/rdp/".concat(names.get(i)).concat(".fasta.rdp"));
            }
            List<RDPOutputList> rdpOutputLists=rdpOutputListsFromFiles(files);
            for(RDPOutputList rdpOutputs:rdpOutputLists){
                int pos=postionMap.get(rdpOutputs.getName().split("\\.")[0]);
                //domain
                addedTable[pos].add(String.valueOf(rdpOutputs.getRatioAtRank(RDPOutput.ranks.domain,0.8)));
                //phylum
                addedTable[pos].add(String.valueOf(rdpOutputs.getRatioAtRank(RDPOutput.ranks.phylum,0.8)));
                //class
                addedTable[pos].add(String.valueOf(rdpOutputs.getRatioAtRank(RDPOutput.ranks.klass,0.8)));
                //order
                addedTable[pos].add(String.valueOf(rdpOutputs.getRatioAtRank(RDPOutput.ranks.order,0.8)));
                //family
                addedTable[pos].add(String.valueOf(rdpOutputs.getRatioAtRank(RDPOutput.ranks.family,0.8)));
                //genus
                addedTable[pos].add(String.valueOf(rdpOutputs.getRatioAtRank(RDPOutput.ranks.genus,0.8)));
            }



            //rebuild the lists to a table
            String[][]output=new String[numberOfSamples][];
            for(int i=0;i<output.length;i++){
                output[i]=new String[addedTable[i].size()];
                for(int j=0;j<addedTable[i].size();j++){
                    output[i][j]=addedTable[i].get(j);
                }
            }
            //save result
            RDPOutputReformatter.writeTable(output,new File("/home/alext/Documents/Ocular Project/sequencing/HCE/report/table.txt"));

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
