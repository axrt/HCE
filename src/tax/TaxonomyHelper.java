package tax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 11/15/13
 * Time: 1:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaxonomyHelper {

    public static String concatinateRDPFormatToHierarchy(final List<File> files, final double cutoff) throws IOException {


        Set<String> domainLevel = new HashSet<String>();
        Set<String> phylumLevel = new HashSet<String>();
        Set<String> classLevel = new HashSet<String>();
        Set<String> orderLevel = new HashSet<String>();
        Set<String> familyLevel = new HashSet<String>();
        Set<String> genusLevel = new HashSet<String>();

        final List<List<String>> fullReformattedFiles = new ArrayList<List<String>>(files.size());
        for (File f : files) {
            final String[][] table = getTableFromRDPFile(f,cutoff);
            fullReformattedFiles.add(fullFormattedFile(table));
            int i = 2;
            for (String[] s : table) {
                if(s.length<5)break;
                domainLevel.add(s[i]);
                if(s.length<8)break;
                phylumLevel.add(s[i] + "." + s[i + 3]);
                if(s.length<11)break;
                classLevel.add(s[i] + "." + s[i + 3] + "." + s[i + 3 + 3]);
                if(s.length<14)break;
                orderLevel.add(s[i] + "." + s[i + 3] + "." + s[i + 3 + 3] + "." + s[i + 3 + 3 + 3]);
                if(s.length<17)break;
                familyLevel.add(s[i] + "." + s[i + 3] + "." + s[i + 3 + 3] + "." + s[i + 3 + 3 + 3] + "." + s[i + 3 + 3 + 3 + 3]);
                if(s.length<20)break;
                genusLevel.add(s[i] + "." + s[i + 3] + "." + s[i + 3 + 3] + "." + s[i + 3 + 3 + 3] + "." + s[i + 3 + 3 + 3 + 3] + "." + s[i + 3 + 3 + 3 + 3 + 3]);

            }
        }

        final StringBuilder fullFileBuilder = new StringBuilder();
        fullFileBuilder.append("taxonomy\trank\t");
        for (File f : files) {
            fullFileBuilder.append(f.getName());
            fullFileBuilder.append('\t');
        }
        fullFileBuilder.append('\n');

        for (String s : domainLevel) {
            fullFileBuilder.append(s.concat("\tdomain"));
            fullFileBuilder.append('\t');
            for (List<String> ffl : fullReformattedFiles) {
                int counter = 0;
                for (String line : ffl) {
                    if (line.contains(s)) {
                        counter++;
                    }
                }
                fullFileBuilder.append(counter);
                fullFileBuilder.append('\t');
            }
            fullFileBuilder.append('\n');
        }
        for (String s : phylumLevel) {
            fullFileBuilder.append(s.concat("\tphylum"));
            fullFileBuilder.append('\t');
            for (List<String> ffl : fullReformattedFiles) {
                int counter = 0;
                for (String line : ffl) {
                    if (line.contains(s)) {
                        counter++;
                    }
                }
                fullFileBuilder.append(counter);
                fullFileBuilder.append('\t');
            }
            fullFileBuilder.append('\n');
        }
        for (String s : classLevel) {
            fullFileBuilder.append(s.concat("\tclass"));
            fullFileBuilder.append('\t');
            for (List<String> ffl : fullReformattedFiles) {
                int counter = 0;
                for (String line : ffl) {
                    if (line.contains(s)) {
                        counter++;
                    }
                }
                fullFileBuilder.append(counter);
                fullFileBuilder.append('\t');
            }
            fullFileBuilder.append('\n');
        }
        for (String s : orderLevel) {
            fullFileBuilder.append(s.concat("\torder"));
            fullFileBuilder.append('\t');
            for (List<String> ffl : fullReformattedFiles) {
                int counter = 0;
                for (String line : ffl) {
                    if (line.contains(s)) {
                        counter++;
                    }
                }
                fullFileBuilder.append(counter);
                fullFileBuilder.append('\t');
            }
            fullFileBuilder.append('\n');
        }
        for (String s : familyLevel) {
            fullFileBuilder.append(s.concat("\tfamily"));
            fullFileBuilder.append('\t');
            for (List<String> ffl : fullReformattedFiles) {
                int counter = 0;
                for (String line : ffl) {
                    if (line.contains(s)) {
                        counter++;
                    }
                }
                fullFileBuilder.append(counter);
                fullFileBuilder.append('\t');
            }
            fullFileBuilder.append('\n');
        }
        for (String s : genusLevel) {
            fullFileBuilder.append(s.concat("\tgenus"));
            fullFileBuilder.append('\t');
            for (List<String> ffl : fullReformattedFiles) {
                int counter = 0;
                for (String line : ffl) {
                    if (line.contains(s)) {
                        counter++;
                    }
                }
                fullFileBuilder.append(counter);
                fullFileBuilder.append('\t');
            }
            fullFileBuilder.append('\n');
        }

        return fullFileBuilder.toString();
    }

    public static String[][] getTableFromRDPFile(final File file, final double cutoff) throws IOException {

        final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line;
        final StringBuilder stringBuilder = new StringBuilder();
        int counter = 0;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line.toUpperCase().replaceAll("\"",""));
            stringBuilder.append('\n');
            counter++;
        }
        String[][] table = new String[counter][];
        String[] split = stringBuilder.toString().split("\n");
        for (int i = 0; i < counter; i++) {
            table[i] = split[i].split("\t");
            final List<String> cells = new ArrayList<>();
            cells.add(table[i][0]);
            cells.add(table[i][1]);
            for (int j = 2; j < table[i].length; j += 3) {
                if (Double.parseDouble(table[i][j + 2]) >= cutoff) {
                    cells.add(table[i][j]);
                    cells.add(table[i][j + 1]);
                    cells.add(table[i][j + 2]);
                } else break;
            }
            table[i]=new String[cells.size()];
            for(int j=0;j<cells.size();j++){
                table[i][j]=cells.get(j);
            }
        }
        return table;
    }

    public static List<String> fullFormattedFile(final String[][] table) {
        final List<String> strings = new ArrayList<String>();
        for (String[] s : table) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (int i = 2; i < s.length; i += 3) {
                stringBuilder.append(s[i]);
                stringBuilder.append('.');
            }
            stringBuilder.append('\n');
            strings.add(stringBuilder.toString());
        }
        return strings;
    }
}