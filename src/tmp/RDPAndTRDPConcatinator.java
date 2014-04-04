package tmp;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 12/13/13
 * Time: 11:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class RDPAndTRDPConcatinator {

    @Test
    public void concat() {
        try {
            final File dir = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/publish");
            final File[] files = dir.listFiles();
            for (File f : files) {
                if (f.getName().endsWith("fasta.rdp")) {

                    final StringBuilder stringBuilder = new StringBuilder();
                    final Set<String> tuitIdentifiedACs = new HashSet<>();
                    final String[] rows = FileUtils.readFileToString(new File(f.getParent(), f.getName().replaceAll(".rdp", "") + ".uclass.tuit.rdp"))
                            //.toUpperCase()
                            .split("\n");
                    final List<String> tuitClassified = new ArrayList<>();
                    for (final String s : rows) {
                        if (s.equals("")) continue;
                        tuitIdentifiedACs.add(s.split("\t\t")[0]);
                        tuitClassified.add(s);
                    }
                    final String[] rdpRows = FileUtils.readFileToString(f)
                            //.toUpperCase()
                            .replaceAll("-", "")
                            .replaceAll("\"","")
                            .replaceAll(" 1","")
                            .replaceAll(" 2","")
                            .replaceAll(" 3","")
                            .split("\n");
                    final List<String> rdpClassified = new ArrayList<>();
                    for (final String s : rdpRows) {
                        if (s.equals("")) continue;
                        if (!tuitIdentifiedACs.contains(s.split("\t\t")[0].toUpperCase())) {
                            stringBuilder.append(s);
                            stringBuilder.append('\n');
                        } else {
                            rdpClassified.add(s);
                        }
                    }

                    //understand who's better, tuit or rdp?
                    for (int i = 0; i < tuitClassified.size(); i++) {
                            final String[] tuitSplit = tuitClassified.get(i).split("\t\t")[1].split("\t");
                            final String[] rdpSplit = rdpClassified.get(i).split("\t\t")[1].split("\t");
                            boolean useTuit=true;
                            try{
                            if(tuitSplit.length>rdpSplit.length){
                                stringBuilder.append(tuitClassified.get(i));
                                stringBuilder.append('\n');
                                continue;
                            }
                            for (int j = 2; j < tuitSplit.length; j += 3) {
                                if (Double.parseDouble(tuitSplit[j]) < 0.8) {
                                    if (Double.parseDouble(rdpSplit[j]) >= 0.8) {
                                        stringBuilder.append(rdpClassified.get(i));
                                        stringBuilder.append('\n');
                                        useTuit=false;
                                        break;
                                    } else {
                                       useTuit=true;
                                        break;
                                    }

                                }
                            }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(useTuit){
                                stringBuilder.append(tuitClassified.get(i));
                                stringBuilder.append('\n');
                            }

                    }


                    FileUtils.writeStringToFile(

                            new File(f.getParent(), f.getName() + ".uclass.tuit.rdp.mtrdp"),
                            stringBuilder.toString().trim());


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
