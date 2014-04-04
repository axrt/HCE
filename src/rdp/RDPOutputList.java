package rdp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/13/13
 * Time: 6:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class RDPOutputList extends ArrayList<RDPOutput> {

    protected final String name;

    public RDPOutputList(int initialCapacity, String name) {
        super(initialCapacity);
        this.name = name;
    }

    public RDPOutputList(String name) {
        this.name = name;
    }

    public RDPOutputList(Collection<? extends RDPOutput> c, String name) {
        super(c);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getRatioAtRank(RDPOutput.ranks rank, double cutoff) {
        int sum = 0;
        for (RDPOutput rdpOutput : this) {
            if (rdpOutput.getLevelAtRank(rank).getBootstrap() >= cutoff) {
                sum++;
            }
        }
        return ((double) sum / this.size());
    }

    public static RDPOutputList newInstanceFromFile(File file) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));) {
            String line;
            RDPOutputList list = new RDPOutputList(file.getName());
            while ((line = bufferedReader.readLine()) != null) {
                    line=line.replaceAll("\"","");
                try {
                    list.add(RDPOutput.newInstanceFromString(line));
                } catch (Exception e) {
                    System.out.println("Problem in " + file.getName());
                    //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            return list;
        }
    }

    public static RDPOutputList newInstanceFromFileAndParams(File file,String delim, double cutoff, boolean addBlank) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));) {
            String line;
            RDPOutputList list = new RDPOutputList(file.getName());
            while ((line = bufferedReader.readLine()) != null) {
                line=line.replaceAll("\"","").replaceAll(" ","_").replaceAll("/","_");
                try {
                    list.add(RDPOutput.newInstanceFromString(line,delim,cutoff,addBlank));
                } catch (Exception e) {
                    System.out.println("Problem in " + file.getName());
                    //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            return list;
        }
    }
}
