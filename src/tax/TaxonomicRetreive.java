package tax;

import correction.RDPOutputReformatter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/10/13
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaxonomicRetreive {


    @Test
    public void retreiveTaxaWithChildren(){


        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "tuit", "tuit");

            Statement statement= connection.createStatement();
            statement.execute("Use NCBI");
            ResultSet resultSet = statement.executeQuery("select * from f_level_children_by_parent where name ='Rhizobiales'");
            resultSet.next();
            int taxid=resultSet.getInt(2);
            resultSet=statement.executeQuery("select * from f_level_children_by_parent where parent_taxid ='"+taxid+"'");
            List<Integer> rhizobialesFamilies=new ArrayList<Integer>();
            while(resultSet.next()){
                rhizobialesFamilies.add(resultSet.getInt(2));
            }
            List<String>genera=new ArrayList<String>();
            for(Integer i:rhizobialesFamilies){
                resultSet=statement.executeQuery("select * from f_level_children_by_parent where parent_taxid ='"+i+"'");
                while (resultSet.next()){
                    genera.add(resultSet.getString(3));
                }
            }

            String [][]table= RDPOutputReformatter.readTableFile(new File("/home/alext/Documents/Ocular Project/Nature supplement/nature11234-s2/2011-09-12404C-tables2_pathogens.txt"));
            Set<String> hmpGenera=new HashSet<String>();
            for(String[]s:table){
                hmpGenera.add(s[0].split(" ")[0].toUpperCase());
            }
            for(String s:genera){
                if(hmpGenera.contains(s.toUpperCase())){
                    System.out.println(s);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
