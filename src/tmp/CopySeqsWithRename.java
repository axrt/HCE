package tmp;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 12/12/13
 * Time: 11:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class CopySeqsWithRename {

    @Test
    public void copy() {

        final File dir=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/reseqAnalysis/Fasta/keratitis");
        final File destination=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/publish");
        final String group=".kerat";
        final String extention=".fasta";

        final File[]files=dir.listFiles();
        final List<File>fastas=new ArrayList<File>();
        for(File f:files){
            if(f.getName().endsWith(extention)&!f.getName().contains("signed")){
                fastas.add(f);
            }
        }
        try{
        for(File f:fastas){
            final String newName=f.getName().replaceAll(extention,group.concat(extention));
            FileUtils.copyFile(f,new File(destination,newName));
        }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
