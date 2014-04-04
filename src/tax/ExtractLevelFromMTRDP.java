package tax;

import org.junit.Test;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 1/14/14
 * Time: 6:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtractLevelFromMTRDP {

    @Test
    public void extract() {

        final File taxFile = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/publish/full.hier");
        final String taxalevel = "genus";
        try (
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(taxFile));
                final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(taxFile.getParent(), taxFile.getName().split("\\.")[0].concat(".".concat(taxalevel).concat(".hier")))));) {
            String line;
            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (count == 0) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                    count++;
                    continue;
                }
                count++;
                final String[] split = line.split("\t");
                final StringBuilder stringBuilder = new StringBuilder();
                if (split[1].equals(taxalevel)) {
                    final String[] subsplit = split[0].split("\\.");
                    stringBuilder.append(subsplit[subsplit.length - 1]);
                    stringBuilder.append('\t');
                    for (int i = 1; i < split.length; i++) {
                        stringBuilder.append(split[i]);
                        stringBuilder.append('\t');
                    }
                    bufferedWriter.write(stringBuilder.toString().trim());
                    bufferedWriter.newLine();

                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}
