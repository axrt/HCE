package rdp;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 12/12/13
 * Time: 1:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConvertTUITToRDP {
    public static String convertTUITFormatToRDPFormat(final File tuitFile) throws IOException {

        final String[] rows = FileUtils.readFileToString(tuitFile).split("\n");
        final StringBuilder stringBuilder = new StringBuilder();
        for (String s : rows) {

            if (s.contains("<-not identified->")) continue;
            if (s.equals("")) continue;
            final String[] acSplit = s.split("\t");
            final String[] acSubsplit = acSplit[0].split(" ");
            final String[] taxonomySplit = acSplit[1].split(" -> ");
            final String unclassified = "unclassified";

            stringBuilder.append(acSubsplit[0]);
            stringBuilder.append("\t\t");
            int counter=0;
            for (int i = 0; i < taxonomySplit.length; i++) {
                final String t = taxonomySplit[i];

                if (t.contains("root {no rank}")) {
                    //stringBuilder.append("Root\trootrank\t1.0\t");
                    counter++;
                    continue;
                }
                if (t.contains("{superkingdom}")) {
                    stringBuilder.append(t.split(" \\{")[0]);
                    stringBuilder.append('\t');
                    stringBuilder.append("domain\t1.0\t");
                    counter++;
                    continue;
                }
                if (t.contains("{phylum}")) {
                    stringBuilder.append(t.split(" \\{")[0]);
                    stringBuilder.append('\t');
                    stringBuilder.append("phylum\t1.0\t");
                    counter++;
                    continue;
                }
                if (t.contains("{class}")) {
                    stringBuilder.append(t.split(" \\{")[0]);
                    stringBuilder.append('\t');
                    stringBuilder.append("class\t1.0\t");
                    counter++;
                    continue;
                }
                if (t.contains("{order}")) {
                    stringBuilder.append(t.split(" \\{")[0]);
                    stringBuilder.append('\t');
                    stringBuilder.append("order\t1.0\t");
                    counter++;
                    continue;
                }
                if (t.contains("{family}")) {
                    stringBuilder.append(t.split(" \\{")[0]);
                    stringBuilder.append('\t');
                    stringBuilder.append("family\t1.0\t");
                    counter++;
                    continue;
                }
                if (t.contains("{genus}")) {
                    stringBuilder.append(t.split(" \\{")[0]);
                    stringBuilder.append('\t');
                    stringBuilder.append("genus\t1.0\t");
                    counter++;
                    continue;
                }
            }
            switch (counter){
                case 0:{
                    //stringBuilder.append("Root\tunclassified\t0.0\t");
                    stringBuilder.append(unclassified.concat("\tdomain\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\tphylum\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\tclass\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\torder\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\tfamily\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\tgenus\t0.0\t"));
                    break;
                }
                case 1:{
                    stringBuilder.append(unclassified.concat("\tdomain\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\tphylum\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\tclass\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\torder\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\tfamily\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\tgenus\t0.0\t"));
                    break;
                }
                case 2:{
                    stringBuilder.append(unclassified.concat("\tphylum\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\tclass\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\torder\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\tfamily\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\tgenus\t0.0\t"));
                    break;
                }
                case 3:{
                    stringBuilder.append(unclassified.concat("\tclass\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\torder\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\tfamily\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\tgenus\t0.0\t"));
                    break;
                }
                case 4:{
                    stringBuilder.append(unclassified.concat("\torder\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\tfamily\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\tgenus\t0.0\t"));
                    break;
                }
                case 5:{
                    stringBuilder.append(unclassified.concat("\tfamily\t0.0\t"));
                    stringBuilder.append(unclassified.concat("\tgenus\t0.0\t"));
                    break;
                }
                case 6:{
                    stringBuilder.append(unclassified.concat("\tgenus\t0.0\t"));
                    break;
                }
            }
            stringBuilder.append('\n');
        }

        return stringBuilder.toString().trim();
    }

    @Test
    public void convert() {
        final File dir = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/publish");
        final File[] files = dir.listFiles();
        for (File f : files) {
            if (f.getName().endsWith(".tuit")) {
                try {
                    FileUtils.writeStringToFile(new File(dir, f.getName().concat(".rdp")), convertTUITFormatToRDPFormat(f));
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
        }
    }
}
