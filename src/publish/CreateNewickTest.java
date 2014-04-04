package publish;

import org.junit.Test;

/**
 * Created by alext on 4/3/14.
 */
public class CreateNewickTest {
    //@Test
    public void test(){
        final String delim="\\.";
        final CreateNewick.NewickNodeRestrictor restrictor=  new CreateNewick.NewickNodeRestrictor() {
            @Override
            public boolean restrict(String s) {
                if(s.endsWith("U")){
                    return true;
                }
                return false;
            }
        };
        final CreateNewick.NewickNode newickNodeWithCounts1
                = CreateNewick.NewickNodeWithCounts.newInstanceFromString(
                "Bacteria.Proteobacteria.Alphaproteobacteria.AlphaproteobacteriaU.AlphaproteobacteriaUU.AlphaproteobacteriaUUU\t1.04013978222128"
        ,restrictor,delim);
        final CreateNewick.NewickNode newickNodeWithCounts2
                = CreateNewick.NewickNodeWithCounts.newInstanceFromString(
                "Bacteria.Firmicutes.Bacilli.Bacillales.Staphylococcaceae.Staphylococcus\t4.02212847156309"
                ,restrictor,delim);
        if(newickNodeWithCounts1!=null){
            newickNodeWithCounts2.add(newickNodeWithCounts1);
        }
        System.out.println(newickNodeWithCounts2);
    }

    @Test
    public void test2(){
        final String delim="\\.";
        final CreateNewick.NewickNodeRestrictor restrictor=  new CreateNewick.NewickNodeRestrictor() {
            @Override
            public boolean restrict(String s) {
                if(s.endsWith("U")){
                    return true;
                }
                return false;
            }
        };
        final CreateNewick.NewickNode newickNodeWithCounts1
                = CreateNewick.NewickNodeWithCounts.newInstanceFromString(
                "Bacteria.Proteobacteria.Alphaproteobacteria.Alphaproteobacteria.Alphaproteobacteria.Alphaproteobacteria\t1.04013978222128"
                ,restrictor,delim);
        final CreateNewick.NewickNode newickNodeWithCounts2
                = CreateNewick.NewickNodeWithCounts.newInstanceFromString(
                "Bacteria\t4.04013978222128"
                ,restrictor,delim);
        System.out.println(newickNodeWithCounts1.equals(newickNodeWithCounts2));
        //System.out.println(newickNodeWithCounts1.equals(new CreateNewick.NewickNode("Bacteria")));
        System.out.println();
    }
}
