package report.groups;

import correction.RDPOutputReformatter;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/14/13
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class GroupExtractor {

    @Test
    public void sortGroups() {

        File femalesOutputDir = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/report/Fasta/female");
        File malesOutputDir = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/report/Fasta/male");
        File inputDir = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/report/Fasta");
        File driverTable = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/report/table.txt");
        //Check consistency
        // 1.prepare a list of samples
        List<String> names = new ArrayList<String>();
        try {

            String[][] table = RDPOutputReformatter.readTableFile(driverTable);
            for (int i = 2; i < table.length; i++) {
                String[] s = table[i];
                if (s[2].equalsIgnoreCase("YES")) {
                    names.add(s[0].replaceAll("444.UM.", "").replaceAll("\\.", "").replaceAll("-", ""));
                }
            }
            //2.create a list of files
            List<File> filesToCheckForConsistency = new ArrayList<File>(names.size());
            Map<String, File> nameFileMap = new HashMap<String, File>(names.size());

            for (String s : names) {
                File f = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/report/Fasta/" + s + ".fasta");

                //3. now check if all of these files actually exist
                if (!f.exists()) {
                    System.out.println("No file for " + f.getName());
                } else {
                    filesToCheckForConsistency.add(f);
                    nameFileMap.put(s, f);
                }
            }


            // Now that is all checked, go create two list of files
            List<File> males = new ArrayList<File>();
            List<File> females = new ArrayList<File>();
            //Go through the driving table to see who is male and who's female
            for (int i = 2; i < table.length - 6; i++) {
                String[] s = table[i];
                if (s[2].equalsIgnoreCase("YES") && (s[7].toUpperCase().contains("HEALTHY") || s[7].toUpperCase().contains("MYOPIA"))) {
                    if (s[4].startsWith("M")) {
                        if (s[4].toUpperCase().contains("OD")) {
                            if (table[i + 1][4].toUpperCase().contains("OS")) {
                                File f = new File(inputDir, nameFileMap.get(
                                        s[0].replaceAll("444.UM.", "").replaceAll("\\.", "").replaceAll("-", "")).getName().split("\\.")[0]
                                        + "_" + table[i + 1][0].replaceAll("444.UM.", "").replaceAll("\\.", "").replaceAll("-", "")
                                        + ".fasta");
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(FileUtils.readFileToString(nameFileMap.get(s[0].replaceAll("444.UM.", "").replaceAll("\\.", "").replaceAll("-", ""))));
                                try {
                                    stringBuilder.append(FileUtils.readFileToString(nameFileMap.get(table[i + 1][0].replaceAll("444.UM.", "").replaceAll("\\.", "").replaceAll("-", ""))));
                                    FileUtils.write(f, stringBuilder.toString());
                                    i++;
                                    males.add(f);
                                } catch (NullPointerException e) {
                                    males.add(nameFileMap.get(s[0].replaceAll("444.UM.", "").replaceAll("\\.", "").replaceAll("-", "")));
                                }

                            }
                        } else {
                            males.add(nameFileMap.get(s[0].replaceAll("444.UM.", "").replaceAll("\\.", "").replaceAll("-", "")));
                        }

                    } else {
                        if (s[4].toUpperCase().contains("OD")) {
                            if (table[i + 1][4].toUpperCase().contains("OS")) {
                                File f = new File(inputDir, nameFileMap.get(
                                        s[0].replaceAll("444.UM.", "").replaceAll("\\.", "").replaceAll("-", "")).getName().split("\\.")[0]
                                        + "_" + table[i + 1][0].replaceAll("444.UM.", "").replaceAll("\\.", "").replaceAll("-", "")
                                        + ".fasta");
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(FileUtils.readFileToString(nameFileMap.get(s[0].replaceAll("444.UM.", "").replaceAll("\\.", "").replaceAll("-", ""))));
                                try {
                                    stringBuilder.append(FileUtils.readFileToString(nameFileMap.get(table[i + 1][0].replaceAll("444.UM.", "").replaceAll("\\.", "").replaceAll("-", ""))));
                                    FileUtils.write(f, stringBuilder.toString());
                                    i++;
                                    females.add(f);
                                } catch (NullPointerException e) {
                                    females.add(nameFileMap.get(s[0].replaceAll("444.UM.", "").replaceAll("\\.", "").replaceAll("-", "")));
                                }


                            }
                        } else {
                            females.add(nameFileMap.get(s[0].replaceAll("444.UM.", "").replaceAll("\\.", "").replaceAll("-", "")));
                        }
                    }
                }
            }
            //Control for sum
            System.out.println(males.size() + females.size());
            System.out.println(males);
            //sort by folders
            for (File f : males) {
                try{
                FileUtils.copyFile(f,new File(malesOutputDir,f.getName()));
                }catch (Exception e){
                    System.out.println(f);
                }

            }
            for (File f : females) {

                FileUtils.copyFile(f,new File(femalesOutputDir,f.getName()));

            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

}
