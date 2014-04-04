package connect;

import correction.RDPOutputReformatter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: alext
 * Date: 10/8/13
 * Time: 9:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class PatientAssigner {

    @Test
    public void addPatients() {

        try {
            String[][] table = RDPOutputReformatter.readTableFile(new File("/home/alext/Documents/Ocular Project/sequencing/HCE/combined HCE/double_patient"));

            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "hce", "hce@@616");
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            for (int i = 0; i < table.length; i++) {

                String[] s = table[i];
                ResultSet resultSet = statement.executeQuery("SELECT * FROM `hcedb`.`samples` where name='" + s[0] + "'");
                int id_patients = 0;
                int id_samples = 0;
                if (resultSet.next()) {
                    id_samples = resultSet.getInt(1);
                    statement.execute("INSERT INTO `hcedb`.`patients`\n" +
                            "(`comment`)\n" +
                            "VALUES\n" +
                            "(\n" +
                            "\'Auto generated\'\n" +
                            ");\n", Statement.RETURN_GENERATED_KEYS);
                    ResultSet keySet = statement.getGeneratedKeys();
                    keySet.next();
                    id_patients = keySet.getInt(1);
                    statement.execute("INSERT INTO `hcedb`.`patient_samples_link`\n" +

                            "(`id_patients`,\n" +
                            "`id_samples`)\n" +
                            "VALUES\n" +
                            "(\n" +
                            id_patients +
                            ",\n" +
                            id_samples +
                            ");\n");
                }

                i++;
                s = table[i];
                resultSet = statement.executeQuery("SELECT * FROM `hcedb`.`samples` where name='" + s[0] + "'");
                if (resultSet.next()) {
                    id_samples = resultSet.getInt(1);
                }
                statement.execute("INSERT INTO `hcedb`.`patient_samples_link`\n" +

                        "(`id_patients`,\n" +
                        "`id_samples`)\n" +
                        "VALUES\n" +
                        "(\n" +
                        id_patients +
                        ",\n" +
                        id_samples +
                        ");\n");

            }
            connection.commit();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}