import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestDataEdits {
    //from Databeses portal connection
    // Set this to e.g. "portal" if you have created a database named portal
    // Leave it blank to use the default database of your database user
    static final String DBNAME = "";
    // For connecting to the portal database on your local machine
    static final String DATABASE = "jdbc:postgresql://localhost/"+DBNAME;
    static final String USERNAME = "postgres";
    static final String PASSWORD = "postgres";

    public static void main(String[] args) {
        String sql = "INSERT INTO Users (username, password) VALUES ('user1', password1)";
        try (Connection connection = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
            PreparedStatement prepStat = connection.prepareStatement(sql)) {
            
            // Här sätter vi värdena för våra ? (index börjar på 1)
            prepStat.setString(1, "user1");
            prepStat.setString(2, "password123");

            // För INSERT, UPDATE och DELETE använder vi executeUpdate()
            int raderPaverkade = prepStat.executeUpdate();
            
            if (raderPaverkade > 0) {
                System.out.println("En ny användare har lagts till");
            }

        } catch (SQLException e) {
            System.err.println("Kunde inte spara data: " + e.getMessage());
        }
    }
}
