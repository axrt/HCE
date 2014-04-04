package correction;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/22/13
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReseqHandler {

    public static String normalizeFileName(String idioticName) {
        return idioticName.replaceAll("444.UM.", "").replaceAll("\\.", "").replaceAll("-", "").replaceAll("_", "");
    }

    public static boolean checkIfFileExists(File file) {
        if (file.exists()) {
            return true;
        } else {
            System.out.println("File " + file.getAbsoluteFile() + " Does not exist");
            return false;
        }
    }

    @Test
    public void handleReseqFastas() {

        File drivingTable = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/report/Patient_info_samples_TRimmed_Val_Oct2013.txt");
        File fastaFolder = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/reseqAnalysis/Fasta/");
        File healthyFolder=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/reseqAnalysis/Fasta/healthy/");
        File maleHealthyFolder=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/reseqAnalysis/Fasta/healthy/male");
        File femaleHealthyFolder=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/reseqAnalysis/Fasta/healthy/female");
        File keratitisFolder=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/reseqAnalysis/Fasta/keratitis/");
        File maleKeratitisFolder=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/reseqAnalysis/Fasta/keratitis/male");
        File femaleKeratitisFolder=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/reseqAnalysis/Fasta/keratitis/female");
        file:///home/alext/Documents/Ocular Project/sequencing/HCE/reseqAnalysis/Fasta/keratitis/male
        try {

            String[][] table = RDPOutputReformatter.readTableFile(drivingTable);
            int start = 2;
            int end = 79;
            List<File> filesThatShouldBeThere = new ArrayList<File>(end - start + 1);
            for (int i = start; i < end; i++) {
                if (table[i][3].equalsIgnoreCase("YES")) {
                    filesThatShouldBeThere.add(new File(fastaFolder, normalizeFileName(table[i][0]) + ".fasta"));
                }
            }

            //Check if the files actually exist and correct those which do not match
            for (File f : filesThatShouldBeThere) {
                checkIfFileExists(f);
            }
            //Split for healthy

            for (int i = start; i < end; i++) {
                if (table[i][3].equalsIgnoreCase("YES")) {
                    if(table[i][7].toUpperCase().contains("HEALTHY")||table[i][7].toUpperCase().contains("MYOPIA")){
                        FileUtils.copyFile(new File(fastaFolder, normalizeFileName(table[i][0]) + ".fasta"),new File(healthyFolder,normalizeFileName(table[i][0]) +".HEALTHY"+ ".fasta"));
                        if(table[i][4].startsWith("M")){
                             FileUtils.copyFile(new File(fastaFolder, normalizeFileName(table[i][0]) + ".fasta"),new File(maleHealthyFolder,normalizeFileName(table[i][0]) +".HEALTHY"+".M."+table[i][5]+ ".fasta"));
                        }else{
                            FileUtils.copyFile(new File(fastaFolder, normalizeFileName(table[i][0]) + ".fasta"),new File(femaleHealthyFolder,normalizeFileName(table[i][0]) +".HEALTHY"+".F."+table[i][5]+ ".fasta"));
                        }
                    } else if(table[i][7].toUpperCase().contains("KERATITIS")){
                        FileUtils.copyFile(new File(fastaFolder, normalizeFileName(table[i][0]) + ".fasta"),new File(keratitisFolder,normalizeFileName(table[i][0]) +".KERATITIS"+ ".fasta"));
                        if(table[i][4].startsWith("M")){
                            FileUtils.copyFile(new File(fastaFolder, normalizeFileName(table[i][0]) + ".fasta"),new File(maleKeratitisFolder,normalizeFileName(table[i][0]) +".KERATITIS"+".M."+table[i][5]+ ".fasta"));
                        }else{
                            FileUtils.copyFile(new File(fastaFolder, normalizeFileName(table[i][0]) + ".fasta"),new File(femaleKeratitisFolder,normalizeFileName(table[i][0]) +".KERATITIS"+".F."+table[i][5]+ ".fasta"));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


}
