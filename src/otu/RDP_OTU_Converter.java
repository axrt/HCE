package otu;

import correction.RDPOutputReformatter;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by alext on 3/25/14.
 */
public class RDP_OTU_Converter {

    public static Map<String,Integer> collectSetForCutoff(final String[][]table, final int cutoff){

        final Map<String,Integer> otuMap=new TreeMap<>();

        for(int i=1;i<table[0].length;i++){
            int sum=0;
            for(int j=1;j<table.length;j++){
                sum+=Integer.parseInt(table[j][i]);
            }
            if(sum>=cutoff){
                otuMap.put(table[0][i],sum);
            }
        }

        return otuMap;
    }

    public static Set<String> getClassifiedOtuNames(final String[][]table, final int level, final double cutoff){

        final Set<String> set=new TreeSet<>();
        final int rank = 1+3*level;
        for(int i=0;i<table.length;i++){
            if(Double.parseDouble(table[i][rank])>=cutoff){
                set.add(table[i][0]);
            }
        }

        return set;
    }


    public static void main (String [] args){
        final int OtuCutoff=10;
        final double RdpCutoff=0.8;
        try {
            final String[][]otuTable= RDPOutputReformatter.readTableFile(
                    new File("/home/alext/Documents/Ocular Project/sequencing/HCE/publish/OTU_approach/all.summary.otu.mergeHCE80.txt"));
            final String[][]rdpTable= RDPOutputReformatter.readTableFile(
                    new File("/home/alext/Documents/Ocular Project/sequencing/HCE/publish/OTU_approach/otu.tree.fasta.rdp"));
            final Set<String>clussifiedSet=getClassifiedOtuNames(rdpTable,6, RdpCutoff);
            System.out.println("OTUs classified:"+clussifiedSet.size());
            final Map<String,Integer> otuCountMap=collectSetForCutoff(otuTable,OtuCutoff);
            System.out.println("OTUs that have more than "+OtuCutoff+" reads: "+otuCountMap.size());
            System.out.println("The following were unclassified:");
            final Set<String> unclassified=new TreeSet<>();
            for(String s:otuCountMap.keySet()){
                if(!clussifiedSet.contains(s)){
                   unclassified.add(s);
                }
            }
            System.out.println("Were unclassified:"+unclassified.size());
            for(String s:unclassified){
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
