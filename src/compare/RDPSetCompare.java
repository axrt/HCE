package compare;

import base.RDPOutputSet;
import correction.RDPOutputReformatter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/6/13
 * Time: 3:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class RDPSetCompare {

    @Test
    public void compareTwoDatasets(){

        try {
            //read both files as txt table
            String[][]seq= RDPOutputReformatter.readTableFile(new File("/home/alext/Documents/Ocular Project/sequencing/HCE/12Aug2013/12Aug2013.txt"));
            String[][]reseq= RDPOutputReformatter.readTableFile(new File("/home/alext/Documents/Ocular Project/sequencing/HCE/october11/seqs.hier.txt"));
            Map<String,RDPOutputSet> seqMap=assembleRDPOutputListFromTable(seq);
            Map<String,RDPOutputSet> reseqMap=assembleRDPOutputListFromTable(reseq);
            //assemble a crossing list
            List<String>taxonCrossingList=new ArrayList<String>();
            Set<String> seqTaxaSet=getTaxaSetFromTable(seq);
            Set<String> reseqTaxaSet=getTaxaSetFromTable(reseq);

            for(String s:seqTaxaSet){
                if(reseqTaxaSet.contains(s)&&!s.equals("name")){
                    taxonCrossingList.add(s);
                }
            }
            //go through the crossing list and assemble a new table, matching the names
            List<String>matchingNames=new ArrayList<String>();
            for(String s:seqMap.keySet()){
                if(reseqMap.containsKey(s)){
                   matchingNames.add(s);
                }
            }
            Set<String>matchingNamesSet=new HashSet<String>(matchingNames);
            System.out.println("seq");
            printOutNonmatchingNames(matchingNamesSet,seqMap);
            System.out.println("reseq");
            printOutNonmatchingNames(matchingNamesSet,reseqMap);

            String[][]outTable=new String[taxonCrossingList.size()+1][matchingNames.size()*2+1];
            outTable[0][0]="";
            for(int i=1;i<outTable.length;i++){
                outTable[i][0]=taxonCrossingList.get(i-1);
            }

            int k=1;
            for(String s:matchingNames){
                RDPOutputSet seqSet=seqMap.get(s);
                RDPOutputSet reseqSet=reseqMap.get(s);

                outTable[0][k]=seqSet.getName();
                outTable[0][k+1]=seqSet.getName();
                int j=1;
                for(String tax:taxonCrossingList){
                    outTable[j][k]=seqSet.get(tax);
                    outTable[j][k+1]=reseqSet.get(tax);
                    j++;
                }
                k+=2;
            }
            RDPOutputReformatter.writeTable(outTable,new File("/home/alext/Documents/Ocular Project/sequencing/HCE/comparison.txt"));

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



        //go through the crossing list and assemble a new table, matching the names

    }

    public static Map<String,RDPOutputSet> assembleRDPOutputListFromTable(String[][]table){
        Map<String,RDPOutputSet> setMap=new HashMap<String, RDPOutputSet>(table[0].length);
        for(int i=1;i<table[0].length;i++){
            RDPOutputSet rdpOutputSet=new RDPOutputSet(table.length,table[0][i]);
            for(int j=1;j<table.length;j++){
                rdpOutputSet.put(table[j][0],table[j][i]);
            }
            setMap.put(rdpOutputSet.getName(),rdpOutputSet);
        }

        return setMap;
    }
    public static Set<String> getTaxaSetFromTable(String[][]table){
        Set<String> taxaSet=new HashSet<String>();
        for(String[]s:table){
            taxaSet.add(s[0]);
        }
        return taxaSet;
    }
    public static void printOutNonmatchingNames(Set<String>matchingNames, Map<String,RDPOutputSet> setMap){
        for(String s:setMap.keySet()){
            if(!matchingNames.contains(s)){
                System.out.println(s);
            }
        }
    }
}
