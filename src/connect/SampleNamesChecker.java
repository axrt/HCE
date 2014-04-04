package connect;

import correction.RDPOutputReformatter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.io.Util;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/8/13
 * Time: 6:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class SampleNamesChecker {

    @Test
    public void checkSampleNames(){

        try {
            String[][] table= RDPOutputReformatter.readTableFile(new File("/home/alext/Documents/Ocular Project/sequencing/HCE/12Aug2013/lookup.txt"));

            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306","hce","hce@@616");
            Statement statement = connection.createStatement();
            File originFolder=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/12Aug2013/Fasta");
            File outFolder=new File("/home/alext/Documents/Ocular Project/sequencing/HCE/combined HCE/Fasta/oct2011/reseq");
            for(String[]s:table){
                ResultSet resultSet=statement.executeQuery("SELECT\n" +
                        "`samples`.`name`\n" +
                        "FROM `hcedb`.`samples` where name='"+s[1]+"'");
                if(resultSet.next()&&s[1].equals(resultSet.getString(1))){
                   //System.out.println(s[1]+ " is ok");
                    FileUtils.copyFile(new File(originFolder,s[0]+".fasta"),new File(outFolder,resultSet.getString(1)+".fasta"));
                }else{
                    System.out.println(s[1]+ " is not ok!!!");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }




}
