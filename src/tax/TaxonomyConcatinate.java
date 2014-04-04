package tax;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 12/16/13
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaxonomyConcatinate {
    @Test
    public void concatinate(){
        final File dir=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/publish");
        final List<File> files=new ArrayList<File>();
        final List<File> females=new ArrayList<>();
        final List<File> males=new ArrayList<>();
        final List<File> kerat=new ArrayList<>();
        final File[]allFiles=dir.listFiles();
        for(File f:allFiles){
            if(f.getName().endsWith(".trdp")){
                if(f.getName().contains("female")){
                    females.add(f);
                    continue;
                }
                if(f.getName().contains("male")){
                    males.add(f);
                    continue;
                }
                if(f.getName().contains("kerat")){
                    kerat.add(f);
                    continue;
                }
            }
        }
        files.addAll(males);
        files.addAll(females);
        files.addAll(kerat);
        try {
            final String string=TaxonomyHelper.concatinateRDPFormatToHierarchy(files,0.8).replaceAll(".signed.fasta.rdp.uclass.tuit.rdp.trdp","");
            FileUtils.writeStringToFile(new File(dir, "full.hier"), string);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
