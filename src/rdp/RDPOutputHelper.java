package rdp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by alext on 3/27/14.
 */
public class RDPOutputHelper {

    public static final String HIER_HEADER = "Taxa";

    public static List<String> generateHMPTreesHier(final List<RDPOutputList> rdpOutputsForSamples, final boolean appendHeader, final boolean keepRoot, final String normString) {

        //Generate maps for all samples (actually, only key sets are going to be used at this time)
        final List<Map<String, Integer>> mapList = new ArrayList<>(rdpOutputsForSamples.size());
        for (RDPOutputList rdpOutputs : rdpOutputsForSamples) {
            mapList.add(getCountsForTaxonomy(rdpOutputs, keepRoot));
        }
        //Generate a full set of taxa names
        final Set<String> taxaNames = new TreeSet<>();
        if(normString!=null){

        }
        for (Map<String, Integer> m : mapList) {
            taxaNames.addAll(m.keySet());
        }
        //Create a List of String that will hold rows
        final List<String> rows = new ArrayList<>(taxaNames.size());
        //Check if a header is required and append if yes
        if (appendHeader) {
            final StringBuilder headerBuilder = new StringBuilder();
            headerBuilder.append(HIER_HEADER);
            headerBuilder.append('\t');
            for (RDPOutputList rdpOutputs : rdpOutputsForSamples) {
                headerBuilder.append(rdpOutputs.getName());
                headerBuilder.append('\t');
            }
            rows.add(headerBuilder.toString().trim());
        }
        //Now create a list of Strings that are further can become rows in a hier file
        if(normString!=null){
            for (String s : taxaNames) {
                final StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(s);
                stringBuilder.append('\t');
                for (Map<String, Integer> m : mapList) {
                    final Map<String, Double> nm=normalizeFor(m,normString);
                    final Double count = nm.get(s);
                    if (count != null) {
                        stringBuilder.append(count);
                    } else {
                        stringBuilder.append(0);
                    }
                    stringBuilder.append('\t');
                }
                rows.add(stringBuilder.toString().trim());
            }
        }else{
            for (String s : taxaNames) {
                final StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(s);
                stringBuilder.append('\t');
                for (Map<String, Integer> m : mapList) {
                    final Integer count = m.get(s);
                    if (count != null) {
                        stringBuilder.append(count);
                    } else {
                        stringBuilder.append(0);
                    }
                    stringBuilder.append('\t');
                }
                rows.add(stringBuilder.toString().trim());
            }
        }

        return rows;
    }

    public static Map<String, Integer> getCountsForTaxonomy(final RDPOutputList rdpOutputs, final boolean keepRoot) {
        final Map<String, Integer> countsMap = new TreeMap<>();
        for (RDPOutput rdpOutput : rdpOutputs) {
            for (RDPOutput.Level level : rdpOutput) {
                if (keepRoot && level.getRank().equals(RDPOutput.ranks.norank)) {
                    continue;
                }
                if (level.getTaxa() == null || level.getTaxa().startsWith(RDPOutput.UNCLASSIFIED_SYMBOL)) {
                    //System.out.println("Problem in "+rdpOutput.getQueryAC()+" at "+level.getTaxa()+", rank "+level.getRank());
                    continue;
                }
                if (!countsMap.containsKey(level.getTaxa())) {
                    countsMap.put(level.getTaxa(), 1);
                } else {
                    countsMap.put(level.getTaxa(), 1 + countsMap.get(level.getTaxa()));
                }
            }
        }
        return countsMap;
    }

    public static File save(final List<String> strings, final File file) throws IOException {
        try (final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (String s : strings) {
                bufferedWriter.write(s);
                bufferedWriter.newLine();
            }
        }
        return file;
    }

    public static Map<String, Double> normalizeFor(final Map<String, Integer> countMap, final String normalization) {
        final Integer norm = countMap.get(normalization);
        final Map<String, Double> norMap = new TreeMap<>();
        for (Map.Entry<String, Integer> me : countMap.entrySet()) {
            norMap.put(me.getKey(), me.getValue() / (double)norm * 100);
        }
        return norMap;
    }
}
