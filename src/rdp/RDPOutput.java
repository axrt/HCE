package rdp;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/13/13
 * Time: 5:37 PM
 * This class allows to get a HMPTree-formatted output from an RDP file
 */
public class RDPOutput implements Iterable<RDPOutput.Level>{
    public static String UNCLASSIFIED_SYMBOL = "U";
    public static Level blanc = new Level("", ranks.norank, 0.0);
    private final String queryAC;
    private final Level rootLevel;
    private final Level domainLevel;
    private final Level phylumLevel;
    private final Level classLevel;
    private final Level orderLevel;
    private final Level familyLevel;
    private final Level genusLevel;
    private final List<Level> levels;
    private RDPOutput(String queryAC, Level rootLevel, Level domainLevel, Level phylumLevel, Level classLevel, Level orderLevel, Level familyLevel, Level genusLevel) {
        this.queryAC = queryAC;
        this.rootLevel = rootLevel;
        this.domainLevel = domainLevel;
        this.phylumLevel = phylumLevel;
        this.classLevel = classLevel;
        this.orderLevel = orderLevel;
        this.familyLevel = familyLevel;
        this.genusLevel = genusLevel;
        this.levels=new ArrayList<>(7);
        this.levels.add(rootLevel);
        this.levels.add(domainLevel);
        this.levels.add(phylumLevel);
        this.levels.add(classLevel);
        this.levels.add(orderLevel);
        this.levels.add(familyLevel);
        this.levels.add(genusLevel);
    }

    @Override
    public Iterator<Level> iterator() {
        return this.levels.listIterator();
    }

    public static RDPOutput newInstanceFromString(String rdpLine) throws Exception {

        String[] split = rdpLine.split("\t");
        String queryAC = split[0];
        Set<String> knownRanks = new HashSet<String>(7);
        knownRanks.add("norank");
        knownRanks.add("domain");
        knownRanks.add("phylum");
        knownRanks.add("class");
        knownRanks.add("order");
        knownRanks.add("family");
        knownRanks.add("genus");

        Level norank = blanc;
        Level domain = blanc;
        Level phylum = blanc;
        Level klass = blanc;
        Level order = blanc;
        Level family = blanc;
        Level genus = blanc;
        for (int i = 2; i < split.length; i += 3) {
            if (knownRanks.contains(split[i + 1])) {
                if (split[i + 1].equals("norank")) {
                    norank = new Level(split[i], ranks.getRankByName(split[i + 1]), Double.parseDouble(split[i + 2]));
                    continue;
                }
                if (split[i + 1].equals("domain")) {
                    domain = new Level(split[i], ranks.getRankByName(split[i + 1]), Double.parseDouble(split[i + 2]));
                    continue;
                }
                if (split[i + 1].equals("phylum")) {
                    phylum = new Level(split[i], ranks.getRankByName(split[i + 1]), Double.parseDouble(split[i + 2]));
                    continue;
                }
                if (split[i + 1].equals("class")) {
                    klass = new Level(split[i], ranks.getRankByName(split[i + 1]), Double.parseDouble(split[i + 2]));
                    continue;
                }
                if (split[i + 1].equals("order")) {
                    order = new Level(split[i], ranks.getRankByName(split[i + 1]), Double.parseDouble(split[i + 2]));
                    continue;
                }
                if (split[i + 1].equals("family")) {
                    family = new Level(split[i], ranks.getRankByName(split[i + 1]), Double.parseDouble(split[i + 2]));
                    continue;
                }
                if (split[i + 1].equals("genus")) {
                    genus = new Level(split[i], ranks.getRankByName(split[i + 1]), Double.parseDouble(split[i + 2]));
                    continue;
                }
            }
        }

        return new RDPOutput(queryAC, norank, domain, phylum, klass, order, family, genus);
    }

    public static RDPOutput newInstanceFromString(String rdpLine, String delim, double cutoff, boolean addRank) throws Exception {

        String[] split = rdpLine.split("\t");
        String queryAC = split[0];
        Set<String> knownRanks = new HashSet<String>(7);
        knownRanks.add("norank");
        knownRanks.add("domain");
        knownRanks.add("phylum");
        knownRanks.add("class");
        knownRanks.add("order");
        knownRanks.add("family");
        knownRanks.add("genus");

        Level norank = blanc;
        Level domain = blanc;
        Level phylum = blanc;
        Level klass = blanc;
        Level order = blanc;
        Level family = blanc;
        Level genus = blanc;
        for (int i = 2; i < split.length; i += 3) {
            if (knownRanks.contains(split[i + 1])) {
                double confidence = 0;

                if (split[i + 1].equals("norank")) {
                    if (!addRank) {
                        norank = new Level("", "", ranks.getRankByName(split[i + 1]), 0);
                        continue;
                    }
                    confidence = Double.parseDouble(split[i + 2]);
                    if (confidence >= cutoff) {
                        norank = new Level(norank.getTaxa() + delim + split[i], split[i], ranks.getRankByName(split[i + 1]), confidence);
                    } else {
                        norank = new Level(norank.getTaxa() + delim + norank.getNodeTaxa() + UNCLASSIFIED_SYMBOL, norank.getNodeTaxa() + UNCLASSIFIED_SYMBOL, ranks.getRankByName(split[i + 1]), confidence);
                    }
                    continue;
                }
                if (split[i + 1].equals("domain")) {
                    confidence = Double.parseDouble(split[i + 2]);
                    if (confidence >= cutoff) {
                        if (addRank) {
                            domain = new Level(norank.getTaxa() + delim + split[i], split[i], ranks.getRankByName(split[i + 1]), confidence);
                        } else {
                            domain = new Level(split[i], split[i], ranks.getRankByName(split[i + 1]), confidence);
                        }

                    } else {
                        if (addRank) {
                            domain = new Level(norank.getTaxa() + delim + norank.getNodeTaxa() + UNCLASSIFIED_SYMBOL, norank.getNodeTaxa() + UNCLASSIFIED_SYMBOL, ranks.getRankByName(split[i + 1]), confidence);

                        } else {
                            domain = new Level(UNCLASSIFIED_SYMBOL, norank.getNodeTaxa() + UNCLASSIFIED_SYMBOL, ranks.getRankByName(split[i + 1]), confidence);
                        }
                    }
                    continue;
                }
                if (split[i + 1].equals("phylum")) {
                    confidence = Double.parseDouble(split[i + 2]);
                    if (confidence >= cutoff) {
                        phylum = new Level(domain.getTaxa() + delim + split[i], split[i], ranks.getRankByName(split[i + 1]), confidence);
                    } else {
                        phylum = new Level(domain.getTaxa() + delim + domain.getNodeTaxa() + UNCLASSIFIED_SYMBOL, domain.getNodeTaxa() + UNCLASSIFIED_SYMBOL, ranks.getRankByName(split[i + 1]), confidence);
                    }
                    continue;
                }
                if (split[i + 1].equals("class")) {
                    confidence = Double.parseDouble(split[i + 2]);
                    if (confidence >= cutoff) {
                        klass = new Level(phylum.getTaxa() + delim + split[i], split[i], ranks.getRankByName(split[i + 1]), confidence);
                    } else {
                        klass = new Level(phylum.getTaxa() + delim + phylum.getNodeTaxa() + UNCLASSIFIED_SYMBOL, phylum.getNodeTaxa() + UNCLASSIFIED_SYMBOL, ranks.getRankByName(split[i + 1]), confidence);
                    }
                    continue;
                }
                if (split[i + 1].equals("order")) {
                    confidence = Double.parseDouble(split[i + 2]);
                    if (confidence >= cutoff) {
                        order = new Level(klass.getTaxa() + delim + split[i], split[i], ranks.getRankByName(split[i + 1]), confidence);
                    } else {
                        order = new Level(klass.getTaxa() + delim + klass.getNodeTaxa() + UNCLASSIFIED_SYMBOL, klass.getNodeTaxa() + UNCLASSIFIED_SYMBOL, ranks.getRankByName(split[i + 1]), confidence);
                    }
                    continue;
                }
                if (split[i + 1].equals("family")) {
                    confidence = Double.parseDouble(split[i + 2]);
                    if (confidence >= cutoff) {
                        family = new Level(order.getTaxa() + delim + split[i], split[i], ranks.getRankByName(split[i + 1]), confidence);
                    } else {
                        family = new Level(order.getTaxa() + delim + order.getNodeTaxa() + UNCLASSIFIED_SYMBOL, order.getNodeTaxa() + UNCLASSIFIED_SYMBOL, ranks.getRankByName(split[i + 1]), confidence);
                    }
                    continue;
                }
                if (split[i + 1].equals("genus")) {
                    confidence = Double.parseDouble(split[i + 2]);
                    if (confidence >= cutoff) {
                        genus = new Level(family.getTaxa() + delim + split[i], split[i], ranks.getRankByName(split[i + 1]), confidence);
                    } else {
                        genus = new Level(family.getTaxa() + delim + family.getNodeTaxa() + UNCLASSIFIED_SYMBOL, family.getNodeTaxa() + UNCLASSIFIED_SYMBOL, ranks.getRankByName(split[i + 1]), confidence);
                    }
                    continue;
                }
            }
        }

        return new RDPOutput(queryAC, norank, domain, phylum, klass, order, family, genus);
    }

    public String getQueryAC() {
        return queryAC;
    }

    public Level getRootLevel() {
        return rootLevel;
    }

    public Level getDomainLevel() {
        return domainLevel;
    }

    public Level getPhylumLevel() {
        return phylumLevel;
    }

    public Level getClassLevel() {
        return classLevel;
    }

    public Level getOrderLevel() {
        return orderLevel;
    }

    public Level getFamilyLevel() {
        return familyLevel;
    }

    public Level getGenusLevel() {
        return genusLevel;
    }

    public Level getLevelAtRank(ranks rank) {
        switch (rank) {
            case norank:
                return this.getRootLevel();
            case domain:
                return this.getDomainLevel();
            case phylum:
                return this.getPhylumLevel();
            case klass:
                return this.getClassLevel();
            case order:
                return this.getOrderLevel();
            case family:
                return this.getFamilyLevel();
            case genus:
                return this.getGenusLevel();
            default:
                return null;
        }
    }

    public enum ranks {
        norank("norank"),
        domain("domain"),
        phylum("phylum"),
        klass("class"),
        order("order"),
        family("family"),
        genus("genus");
        private static Map<String, ranks> map = new HashMap<String, ranks>(7);

        static {
            map.put("norank", norank);
            map.put("domain", domain);
            map.put("phylum", phylum);
            map.put("class", klass);
            map.put("order", order);
            map.put("family", family);
            map.put("genus", genus);
        }

        private final String name;

        private ranks(String name) {
            this.name = name;
        }

        public static ranks getRankByName(String name) {
            return map.get(name);
        }

        public String getName() {
            return name;
        }
    }

    public static class Level {

        private final String taxa;
        private String nodeTaxa;
        private final ranks rank;
        private final double bootstrap;

        public Level(String taxa, ranks rank, double bootstrap) {
            this.taxa = taxa;
            this.rank = rank;
            this.bootstrap = bootstrap;
        }

        public Level(String taxa, String nodeTaxa, ranks rank, double bootstrap) {
            this.taxa = taxa;
            this.nodeTaxa = nodeTaxa;
            this.rank = rank;
            this.bootstrap = bootstrap;
        }

        public String getTaxa() {
            return taxa;
        }

        public String getNodeTaxa() {
            return nodeTaxa;
        }

        public ranks getRank() {
            return rank;
        }

        public double getBootstrap() {
            return bootstrap;
        }
    }

}
