package mothur;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/13/13
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class SummarySeqOutput {
    private final String name;
    private final String[][] table;

    protected SummarySeqOutput(String name, String[][] table) {
        this.name = name;
        this.table = table;
    }

    public int getMinNBases(){
        return Integer.valueOf(this.table[1][3]);
    }
    public int getMaxNBases(){
        return Integer.valueOf(this.table[7][3]);
    }
    public double getMeanNBases(){
        return Double.valueOf(this.table[8][3]);
    }
    public int getMedianNBases(){
        return Integer.valueOf(this.table[4][3]);
    }
    public int getNumberOfSeqs(){
        return Integer.valueOf(this.table[7][6]);
    }

    public String getName() {
        return name;
    }

    /**
     *
     * @return defecive copy
     */
    public String[][] getTable() {
        String[][] copy=new String[this.table.length][];
        for(int i=0;i<this.table.length;i++){
            copy[i]=new String[this.table[i].length];
            for(int j=0;j<this.table[i].length;j++){
                copy[i][j]=this.table[i][j];
            }
        }
        return copy;
    }

    public static SummarySeqOutput newInstaceFromFile(File file) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder stringBuilder = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        String tableString = stringBuilder.substring(stringBuilder.indexOf("\t\t"), stringBuilder.indexOf("#"));
        String[] split = tableString.split("\n");
        String[][] table = new String[split.length][];
        int i = 0;
        for (String s : split) {
            table[i] = s.split("\t");
            i++;
        }
        SummarySeqOutput summarySeqOutput = new SummarySeqOutput(file.getName(), table);
        return summarySeqOutput;
    }


}
