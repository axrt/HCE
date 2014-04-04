package tax;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alext on 1/29/14.
 */
public class AddSkinSamples {

    @Test
    public void concatinateTaxonomyWithSkinSamples(){
        final File dir=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/skin_added/combined");
        final List<File> files=new ArrayList<File>();
        final List<File> females=new ArrayList<>();
        final List<File> males=new ArrayList<>();
        final List<File> kerat=new ArrayList<>();
        final List<File> skin=new ArrayList<>();
        final File[]allFiles=dir.listFiles();
        for(File f:allFiles){
            if(f.getName().endsWith(".rdp")){
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
                if(f.getName().contains(".S.")){
                    skin.add(f);
                    continue;
                }
            }
        }
        files.addAll(males);
        files.addAll(females);
        files.addAll(kerat);
        files.addAll(skin);
        try {
            final String string=TaxonomyHelper.concatinateRDPFormatToHierarchy(files,0.8).replaceAll(".male.signed.fasta","")
                    .replaceAll(".fasta","").replaceAll(".rdp","");
            FileUtils.writeStringToFile(new File(dir, "full.hier"), string);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }


}
