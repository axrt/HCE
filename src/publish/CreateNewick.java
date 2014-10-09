package publish;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * For a given mle file subset creates a newick formatted tree
 */
public class CreateNewick {
    public interface NewickNodeRestrictor {
        boolean restrict(String s);
    }

    public static class NewickNode implements Comparable<NewickNode> {

        public interface NodeFromtatter {
            String format(final NewickNode newickNode);
        }

        protected final String taxa;
        protected final int level;
        protected final Set<NewickNode> children;

        protected NewickNode(final String taxa, final int level) {
            this.taxa = taxa;
            this.level = level;
            this.children = new TreeSet<>();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof NewickNode) {
                final NewickNode newickNode = (NewickNode) obj;
                if (this.taxa.equals(newickNode.taxa)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            return this.taxa.hashCode();
        }

        public void add(final NewickNode newickNode) {
            if (this.equals(newickNode)) {
                for (NewickNode nwn : newickNode.children) {
                    if (!this.children.contains(nwn)) {
                        this.children.add(nwn);
                    } else {
                        for (NewickNode tnwn : this.children) {
                            if (tnwn.equals(nwn)) {
                                tnwn.add(nwn);
                                break;
                            }
                        }
                    }
                }
            }
        }

        public String getTaxaLables(final String separator) {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.taxa);
            if (this.children.isEmpty()) {
                return stringBuilder.toString();
            }
            for (NewickNode nwn : this.children) {
                stringBuilder.append(separator);
                stringBuilder.append(nwn.getTaxaLables(separator));
            }
            return stringBuilder.toString();
        }

        public String getNodeLables() {
            if (this.children.isEmpty()) {
                return "";
            }
            final StringBuilder stringBuilder = new StringBuilder();
            for (NewickNode nwn : this.children) {
                stringBuilder.append(this.taxa);
                stringBuilder.append('|');
                stringBuilder.append(nwn.taxa);
                stringBuilder.append('\t');
                stringBuilder.append(nwn.taxa);
                stringBuilder.append('\n');
                stringBuilder.append(nwn.getNodeLables());
            }
            return stringBuilder.toString();
        }

        @Override
        public String toString() {

            final StringBuilder stringBuilder = new StringBuilder();
            if (children.isEmpty()) {
                stringBuilder.append(this.taxa);
                return stringBuilder.toString();
            }
            stringBuilder.append('(');
            int count = 0;
            for (NewickNode nxn : this.children) {
                stringBuilder.append(nxn.toString());
                if (count < this.children.size() - 1) {
                    stringBuilder.append(',');
                }
                count++;
            }
            stringBuilder.append(')');
            stringBuilder.append(this.taxa);
            return stringBuilder.toString();
        }

        public String format(final NodeFromtatter formatter) {
            return formatter.format(this);
        }

        public String toString(String prefix, String tabulator) {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append(this.taxa);
            stringBuilder.append('\n');
            prefix = prefix.concat(tabulator);
            for (NewickNode nwn : this.children) {
                stringBuilder.append(tabulator);
                stringBuilder.append(nwn.toString(prefix, tabulator));
            }
            return stringBuilder.toString();
        }

        @Override
        public int compareTo(NewickNode o) {
            return this.taxa.compareTo(o.taxa);
        }
    }

    public static class NewickNodeWithCounts extends NewickNode {
        public static final NodeFromtatter NEXUS_DEFAULT = new NodeFromtatter() {
            @Override
            public String format(final NewickNode newickNode) {

                final StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("#NEXUS\n");
                stringBuilder.append("BEGIN TAXA;\n");
                stringBuilder.append("TAXLABELS");
                stringBuilder.append(' ');
                stringBuilder.append(newickNode.getTaxaLables(" "));
                stringBuilder.append('\n');
                stringBuilder.append("END;\n");
                if (newickNode instanceof NewickNodeWithCounts) {
                    stringBuilder.append("BEGIN DATA;\n");
                    stringBuilder.append(((NewickNodeWithCounts) newickNode).getLeavesCountData());
                    stringBuilder.append(";\n");
                    stringBuilder.append("END;\n");
                }
                stringBuilder.append("BEGIN TREES;\n");
                stringBuilder.append("TREE ");
                stringBuilder.append("tree = ");
                stringBuilder.append(newickNode.toString());
                stringBuilder.append(";\n");
                stringBuilder.append("END;");
                return stringBuilder.toString();
            }
        };
        private final double count;

        private NewickNodeWithCounts(final String taxa, final int level, final double count) {
            super(taxa, level);
            this.count = count;
        }

        @Override
        public String format(final NodeFromtatter formatter) {
            return formatter.format((NewickNodeWithCounts) this);
        }

        public String getLeavesCountData() {
            if (this.children.isEmpty()) {
                return this.taxa.concat("\t").concat(String.valueOf(this.count));
            } else {
                final StringBuilder stringBuilder = new StringBuilder();
                for (NewickNode newickNode : this.children) {
                    if (newickNode instanceof NewickNodeWithCounts) {
                        stringBuilder.append(
                                ((NewickNodeWithCounts) newickNode).getLeavesCountData()
                        );
                        stringBuilder.append('\n');
                    }
                }
                return stringBuilder.toString().trim();
            }
        }

        public NewickNodeWithCounts cutSelfToThresholdAtLevel(final int level, final double threshold) {
            if (this.level == level) {
                if (this.count < threshold) {
                    return null;
                }
            } else {
                int count = 0;
                for (NewickNode nwm : this.children) {
                    if (((NewickNodeWithCounts) nwm).cutSelfToThresholdAtLevel(level, threshold) != null) {
                        count++;
                    }
                }
                if (count == 0) {
                    return null;
                }
            }
            final NewickNodeWithCounts newickNodeWithCounts = new NewickNodeWithCounts(this.taxa, this.level, this.count);
            for (NewickNode nwn : this.children) {
                final NewickNodeWithCounts child = ((NewickNodeWithCounts) nwn).cutSelfToThresholdAtLevel(level, threshold);
                if (child != null) {
                    newickNodeWithCounts.children.add(child);
                }
            }
            return newickNodeWithCounts;
        }

        @Override
        public String toString(String prefix, String tabulator) {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append(this.taxa);
            stringBuilder.append(':');
            stringBuilder.append(this.count);
            stringBuilder.append('\n');
            prefix = prefix.concat(tabulator);
            for (NewickNode nwn : this.children) {
                stringBuilder.append(tabulator);
                stringBuilder.append(nwn.toString(prefix, tabulator));
            }
            return stringBuilder.toString();
        }

        public static NewickNode newInstanceFromString(final String hmpTreeLine, final NewickNodeRestrictor restrictor, final String delim) {
            final String[] columnSplit = hmpTreeLine.split("\t");
            if (columnSplit.length < 2) {
                throw new IllegalArgumentException("Inconsistent tree!");
            }
            final String[] split = columnSplit[0].split(delim);
            final double count = Double.parseDouble(columnSplit[1]);
            for (String s : split) {
                if (restrictor.restrict(s)) {
                    return null;
                }
            }
            if (split.length < 2) {

                return new NewickNodeWithCounts(split[split.length - 1], split.length - 1, count);

            }
            final NewickNode toReturn = new NewickNode(split[0], 0);
            if (split.length == 2) {
                final NewickNodeWithCounts newickNodeWithCounts = new NewickNodeWithCounts(split[split.length - 1], 1, count);
                toReturn.children.add(newickNodeWithCounts);
                return toReturn;
            }
            final List<NewickNode> nodes = new ArrayList<>();
            for (int i = 1; i < split.length - 1; i++) {
                nodes.add(new NewickNode(split[i], i));
            }
            for (int i = 1; i < nodes.size(); i++) {
                nodes.get(i - 1).children.add(nodes.get(i));
            }

            toReturn.children.add(nodes.get(0));
            final NewickNodeWithCounts newickNodeWithCounts = new NewickNodeWithCounts(split[split.length - 1], split.length - 1, count);
            nodes.get(nodes.size() - 1).children.add(newickNodeWithCounts);
            return toReturn;
        }
    }

    public static class NewickNodeWithGroupCounts extends NewickNodeWithCounts {

        private double[] count;

        protected NewickNodeWithGroupCounts(String taxa, int level, double[] count) {
            super(taxa, level, count[0]);
            this.count = count;
        }
        @Override
        public String getLeavesCountData() {
            if (this.children.isEmpty()) {
                final StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append(this.taxa);
                for(int i=0;i<this.count.length;i++){
                    stringBuilder.append('\t');
                    stringBuilder.append(this.count[i]);
                }
                return stringBuilder.toString();
            } else {
                final StringBuilder stringBuilder = new StringBuilder();
                for (NewickNode newickNode : this.children) {
                    if (newickNode instanceof NewickNodeWithCounts) {
                        stringBuilder.append(
                                ((NewickNodeWithCounts) newickNode).getLeavesCountData()
                        );
                        stringBuilder.append('\n');
                    }
                }
                return stringBuilder.toString().trim();
            }
        }
        public static NewickNode newInstanceFromString(final String hmpTreeLine, final NewickNodeRestrictor restrictor, final String delim) {
            final String[] columnSplit = hmpTreeLine.split("\t");
            if (columnSplit.length < 2) {
                throw new IllegalArgumentException("Inconsistent tree!");
            }
            final String[] split = columnSplit[0].split(delim);
            final double[] count = new double[columnSplit.length-1];

            for (int i = 1; i < columnSplit.length; i++) {
                count[i-1] = Double.parseDouble(columnSplit[i]);
            }
            for (String s : split) {
                if (restrictor.restrict(s)) {
                    return null;
                }
            }
            if (split.length < 2) {

                return new NewickNodeWithGroupCounts(split[split.length - 1], split.length - 1, count);

            }
            final NewickNode toReturn = new NewickNode(split[0], 0);
            if (split.length == 2) {
                final NewickNodeWithCounts newickNodeWithCounts = new NewickNodeWithGroupCounts(split[split.length - 1], 1, count);
                toReturn.children.add(newickNodeWithCounts);
                return toReturn;
            }
            final List<NewickNode> nodes = new ArrayList<>();
            for (int i = 1; i < split.length - 1; i++) {
                nodes.add(new NewickNode(split[i], i));
            }
            for (int i = 1; i < nodes.size(); i++) {
                nodes.get(i - 1).children.add(nodes.get(i));
            }

            toReturn.children.add(nodes.get(0));
            final NewickNodeWithCounts newickNodeWithCounts = new NewickNodeWithGroupCounts(split[split.length - 1], split.length - 1, count);
            nodes.get(nodes.size() - 1).children.add(newickNodeWithCounts);
            return toReturn;
        }
        @Override
        public String toString(String prefix, String tabulator) {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(prefix);
            stringBuilder.append(this.taxa);
            for(int i =0;i<this.count.length;i++){
                stringBuilder.append(':');
                stringBuilder.append(this.count[i]);
            }
            stringBuilder.append('\n');
            prefix = prefix.concat(tabulator);
            for (NewickNode nwn : this.children) {
                stringBuilder.append(tabulator);
                stringBuilder.append(nwn.toString(prefix, tabulator));
            }
            return stringBuilder.toString();
        }
        @Override
        public NewickNodeWithCounts cutSelfToThresholdAtLevel(final int level, final double threshold) {

            if (this.level == level) {
                boolean keep=false;
                for(int i=0;i<this.count.length;i++){
                    if (this.count[i] >= threshold) {
                        keep=true;
                        break;
                    }
                }
                if(!keep){
                    return null;
                }

            } else {
                int count = 0;
                for (NewickNode nwm : this.children) {
                    if (((NewickNodeWithCounts) nwm).cutSelfToThresholdAtLevel(level, threshold) != null) {
                        count++;
                    }
                }
                if (count == 0) {
                    return null;
                }
            }
            final NewickNodeWithCounts newickNodeWithCounts = new NewickNodeWithGroupCounts(this.taxa, this.level, this.count);
            for (NewickNode nwn : this.children) {
                final NewickNodeWithCounts child = ((NewickNodeWithCounts) nwn).cutSelfToThresholdAtLevel(level, threshold);
                if (child != null) {
                    newickNodeWithCounts.children.add(child);
                }
            }
            return newickNodeWithCounts;
        }
    }

    public static void main(String[] args) {
        final String delim = "\\.";
        final CreateNewick.NewickNodeRestrictor restrictor = new CreateNewick.NewickNodeRestrictor() {
            @Override
            public boolean restrict(String s) {
                if (s.endsWith("U")) {
                    return true;
                }
                return false;
            }
        };
        final File file = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/tree/data.mle");
        final File treeFile = new File("/home/alext/Documents/Ocular Project/sequencing/HCE/tree/data.tre");
        final File countDataFile= new File("/home/alext/Documents/Ocular Project/sequencing/HCE/tree/data.new.dat");
        final String countHeader="LABELS\tMales\tFemales\tKeratitis\n" +
                "COLORS\t#003366\t#CC66FF\t#990000\n";
        try {
            NewickNode newickNode = loadFromGroupFile(file, restrictor, delim);
            NewickNodeWithCounts newickNodeWithCounts = ((NewickNodeWithCounts) newickNode).cutSelfToThresholdAtLevel(5, 0.1);
            //System.out.println(newickNodeWithCounts.toString("","\t"));
            System.out.println(newickNodeWithCounts.toString());
            System.out.println(newickNodeWithCounts.getLeavesCountData());
            FileUtils.writeStringToFile(treeFile,newickNodeWithCounts.toString());
            FileUtils.writeStringToFile(countDataFile,countHeader.concat(newickNodeWithCounts.getLeavesCountData()));
            //System.out.println(newickNodeWithCounts.getNodeLables());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NewickNode loadFromFile(final File file, final NewickNodeRestrictor restrictor, final String delim) throws IOException {
        final NewickNode primeNode;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

            String line;
            line = bufferedReader.readLine();

            if (line != null) {
                primeNode = NewickNodeWithCounts.newInstanceFromString(line, restrictor, delim);
            } else {
                return null;
            }
            while ((line = bufferedReader.readLine()) != null) {
                final NewickNode nextNode = NewickNodeWithCounts.newInstanceFromString(line, restrictor, delim);
                if (nextNode != null) {
                    primeNode.add(nextNode);
                }
            }
        }
        return primeNode;
    }

    public static NewickNode loadFromGroupFile(final File file, final NewickNodeRestrictor restrictor, final String delim) throws IOException {
        final NewickNode primeNode;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

            String line;
            line = bufferedReader.readLine();

            if (line != null) {
                primeNode = NewickNodeWithGroupCounts.newInstanceFromString(line, restrictor, delim);
            } else {
                return null;
            }
            while ((line = bufferedReader.readLine()) != null) {
                final NewickNode nextNode = NewickNodeWithGroupCounts.newInstanceFromString(line, restrictor, delim);
                if (nextNode != null) {
                    primeNode.add(nextNode);
                }
            }
        }
        return primeNode;
    }
}
