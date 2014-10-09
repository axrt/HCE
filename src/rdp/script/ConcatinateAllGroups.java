package rdp.script;

import rdp.RDPOutputHelper;
import rdp.RDPOutputList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by alext on 3/27/14.
 */
public class ConcatinateAllGroups {

    public static void main (String[]args){

        //final File femaleDir=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/female");
        //final File maleDir=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/male");
        //final File keratDir=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/final/fasta/forOTU/keratitis");
        //final File clDir=new File("/home/alext/Documents/Ocular Project/sequencing/CL/cl_fasta");
        final File pairedDir=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/refined_groups");
        final List<File> fileList=new ArrayList<>();
        //fileList.addAll(Arrays.asList(femaleDir.listFiles()));
        //fileList.addAll(Arrays.asList(maleDir.listFiles()));
        //fileList.addAll(Arrays.asList(keratDir.listFiles()));
        //fileList.addAll(Arrays.asList(clDir.listFiles()));
        fileList.addAll(Arrays.asList(pairedDir.listFiles()));
        final List<File>rdpFiles=new ArrayList<>();
        for(File f:fileList){
           if(f.getName().endsWith(".fasta.tuit")){
               rdpFiles.add(f);
           }
        }
        final List<RDPOutputList> rdpOutputLists = new LinkedList<>();
        for(File f:rdpFiles){
            try {
                rdpOutputLists.add(RDPOutputList.newInstanceFromFileAndParams(f, ".", 0.8, false));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        final List<String> output = RDPOutputHelper.generateHMPTreesHier(rdpOutputLists, true, false, "Bacteria");
        final File outHier=new File(pairedDir.getParent(),"full.hmptree.tuit.hier");
        try {
            RDPOutputHelper.save(output, outHier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
