import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    // Replace 'your_db', 'your_user', and 'your_pass' with your actual Postgres credentials
    private static final String URL = "jdbc:postgresql://localhost:5432/chat_project";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";

    private static Connection conn;

    // Connect to the Postgres Database
    public static void connect() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connected to PostgreSQL successfully!");
        } catch (SQLException e) {
            System.err.println("Connection Error: " + e.getMessage());
        }
    }

    // CHECK FOR UNIQUE USERNAME
    public static boolean isUsernameTaken(String username) {
        String sql = "SELECT username FROM Users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // If rs.next() is true, the user exists
        } catch (SQLException e) {
            return true; 
        }
    }
//add user to user list
    public static boolean addUser(String username, String password) {
    if (isUsernameTaken(username)) {
        return false; // Tells the constructor: "Stop! Name is taken."
    }

    // 2. The SQL attempt
    String sql = "INSERT INTO Users(username, password) VALUES(?, ?)";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        
        pstmt.executeUpdate();
        return true; // Tells the constructor: "Success! Go ahead."
        
    } catch (SQLException e) {
        // We don't print here to avoid "double errors"
        // We just return false because the save failed
        return false;
    }
}
//remove user from channel member list
public static void leaveChannel(String username, String channelName) {
    try {
        // 1. Remove them from WHATEVER channel they are in right now
        // (If they aren't in one, this line just does nothing. No error!)
        String deleteSql = "DELETE FROM UsersInChannel WHERE username = ?";
        PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
        deleteStmt.setString(1, username);
        deleteStmt.executeUpdate();

        System.out.println(username + " is now removed from their channel");

    } catch (SQLException e) {
        System.out.println("Error leaving channel: " + e.getMessage());
    }
}
//add user to chennel list 
public static void joinChannel(String username, String channelName) {
    try {


        // Add them to the new channel
        String insertSql = "INSERT INTO UsersInChannel (username, channel_name) VALUES (?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
        insertStmt.setString(1, username);
        insertStmt.setString(2, channelName);
        insertStmt.executeUpdate();

        System.out.println(username + " is now in " + channelName);

    } catch (SQLException e) {
        System.out.println("Error joining channel: " + e.getMessage());
    }
}

}