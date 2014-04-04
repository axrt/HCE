package publish;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This is to extract species for a given subset of an mle tree, which contains the most abundant genera
 */
public class ExtractSpecies {

    public static void main(String[] args) {

        final File female_full = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/female/female.tuit.full");
        final File male_full = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/male/male.tuit.full");
        final File keratitis_full = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/keratitis/kerat.tuit.full");
        final File healthy_full = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/healthy.tuit.full");

        final File female_top = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/female.top.genus");
        final File male_top = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/male.top.genus");
        final File healthy_top = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/healthy.top.genus");
        final File kerat_top = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/kerat.top.genus");
        try {

            saveTopSpecies(kerat_top,keratitis_full,new File(kerat_top.getParent(),kerat_top.getName().concat(".spec")));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void saveTopSpecies(final File driver, final File tuitOutput,final File output) throws IOException{
        final List<String> topList=collectTop(driver);

        final List<Map<String,Integer>>countmaps=new ArrayList<>(topList.size());
        for(String s:topList){
            countmaps.add(countSpeciesForGenus(tuitOutput,s));
        }
        final List<Map<String,Double>>noramlizedMaps=new ArrayList<>(topList.size());
        for(Map<String,Integer> map:countmaps){
            noramlizedMaps.add(normalizeCountMap(map));
        }
        try(final BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(output))){
            int i=0;
            for(Map<String,Double>map:noramlizedMaps){
                bufferedWriter.write(concatinateLinesForTaxa(topList.get(i),map));
                bufferedWriter.newLine();
               i++;
            }
        }
    }

    public static List<String> collectTop(final File file) throws IOException {

        final List<String> topTaxa;

        try (final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));) {
            topTaxa = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String[] split = line.split("\t");
                final String[] taxaSplit = split[0].split("\\.");
                topTaxa.add(taxaSplit[taxaSplit.length - 1]);
            }
        }
        return topTaxa;
    }

    public static Map<String,Integer> countSpeciesForGenus(final File file, final String taxa) throws IOException{

        final Map<String,Integer> map;
        try (final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));) {
            map=new LinkedHashMap<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String[] split = line.split("\t");
                if(split[17].equals(taxa)){
                   if(!map.containsKey(split[20])){
                       map.put(split[20],1);
                   }else{
                       map.put(split[20],map.get(split[20])+1);
                   }
                }
            }
        }

        return map;
    }

    public static Map<String, Double> normalizeCountMap(final Map<String,Integer>countMap){
        int total=0;
        for(Map.Entry<String,Integer> e:countMap.entrySet()){
            total+=e.getValue();
        }
        final Map<String,Double> normalizedMap=new TreeMap<>();
        for(Map.Entry<String,Integer> e:countMap.entrySet()){
            normalizedMap.put(e.getKey(),(double)e.getValue()/(double)total);
        }
        return normalizedMap;
    }

    public static String concatinateLinesForTaxa(final String taxa, final Map<String,? extends Number> map){
        final StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(taxa);
        stringBuilder.append("\t\t");
        stringBuilder.append("\n");
        for(Map.Entry<String,? extends Number> e:map.entrySet()){
            stringBuilder.append("\t");
            stringBuilder.append(e.getKey());
            stringBuilder.append("\t");
            stringBuilder.append(e.getValue());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString().trim();
    }
}
