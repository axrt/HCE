package rdp.test;

import org.junit.Test;
import rdp.RDPOutputHelper;
import rdp.RDPOutputList;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by alext on 3/27/14.
 */
public class RDPOutputHelperTest {

    //@Test
    public void getCountsForTaxonomyTest() {
        try {
            final RDPOutputList rdpOutputs = RDPOutputList.newInstanceFromFileAndParams(
                    new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/kerat.rdp"), ".", 0.8, false);
            final Map<String, Integer> taxaMap = RDPOutputHelper.getCountsForTaxonomy(rdpOutputs, false);
            for (Map.Entry<String, Integer> me : taxaMap.entrySet()) {
                System.out.println(me.getKey() + " :> " + me.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void generateHMPTreesHierTest() {
        try {
            final File dir = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/");
            final File[] files = dir.listFiles();
            final List<RDPOutputList> rdpOutputLists = new LinkedList<>();
            for (File file : files) {
                if (file.isDirectory() | !file.getName().endsWith(".rdp") || file.getName().contains("fullrank")) {
                    continue;
                }
                rdpOutputLists.add(RDPOutputList.newInstanceFromFileAndParams(file, ".", 0.8, false));
            }
            final List<String> output = RDPOutputHelper.generateHMPTreesHier(rdpOutputLists, true, false,null);
            for (String s : output) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void saveTest() {
        try {
            final File dir = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/");
            final File[] files = dir.listFiles();
            final List<RDPOutputList> rdpOutputLists = new LinkedList<>();
            for (File file : files) {
                if (file.isDirectory() |!file.getName().endsWith(".rdp") || file.getName().contains("fullrank")) {
                    continue;
                }
                rdpOutputLists.add(RDPOutputList.newInstanceFromFileAndParams(file, ".", 0.8, false));
            }
            final List<String> output = RDPOutputHelper.generateHMPTreesHier(rdpOutputLists, true, false,null);
            final File outFile = new File(dir, "test.out.txt");
            RDPOutputHelper.save(output, outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void normalizeForTest() {
        try {
            final File dir = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/");
            final File[] files = dir.listFiles();
            final List<RDPOutputList> rdpOutputLists = new LinkedList<>();
            for (File file : files) {
                if (file.isDirectory() |!file.getName().endsWith(".rdp") || file.getName().contains("fullrank")) {
                    continue;
                }
                rdpOutputLists.add(RDPOutputList.newInstanceFromFileAndParams(file, ".", 0.8, false));
            }
            final List<String> output = RDPOutputHelper.generateHMPTreesHier(rdpOutputLists, true, false,"Bacteria");
            final File outFile = new File(dir, "test.norm.out.txt");
            RDPOutputHelper.save(output, outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
