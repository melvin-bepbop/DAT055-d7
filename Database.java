import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

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
    public static boolean createUser(String username, String password) {
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
    public static void UserLeaveChannel(String username, String channelName) {
        try {
            // 1. Remove them from WHATEVER channel they are in right now
            // (If they aren't in one, this line just does nothing. No error!)
            String deleteSql = "DELETE FROM UserInActiveChannel WHERE user = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
            deleteStmt.setString(1, username);
            deleteStmt.executeUpdate();

            System.out.println(username + " is now removed from their channel");

        } catch (SQLException e) {
            System.out.println("Error leaving channel: " + e.getMessage());
        }
    }
    //add user to chennel list 
    public static void UserJoinChannel(String username, String channelName) {
        try {


            // Add them to the new channel
            String insertSql = "INSERT INTO UserInActiveChannel (user, channel) VALUES (?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, username);
            insertStmt.setString(2, channelName);
            insertStmt.executeUpdate();

            System.out.println(username + " is now in " + channelName);

        } catch (SQLException e) {
            System.out.println("Error joining channel: " + e.getMessage());
        }
    }
    public static Channel GetChannel(String channelname){
        Channel channel = new Channel();
        String insertSql = "SELECT * FROM Channel WHERE name = ?";
        try {
            // Get the channel
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, channelname);
            System.out.println("Got channel: "+ channelname);
            try (ResultSet rs = insertStmt.executeQuery()) {
                while (rs.next()) {
                    java.sql.Timestamp sqlTime = rs.getTimestamp("Created_at");
                    LocalDateTime javaTime = sqlTime.toLocalDateTime();
                    String channelName = rs.getString("name");
                    channel = new Channel(channelName, javaTime);
                }
            } catch (SQLException e) {
                System.out.println("Error getting info from channel: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error getting channel: " + e.getMessage());
        }
        return channel;
    }
    public static LinkedList<Channel> GetAllChannels(){
        LinkedList<Channel> Channels = new LinkedList<>();
        String sql = "Select name from Channel";
        try{
            PreparedStatement insertStmt = conn.prepareStatement(sql);
            try(ResultSet rs = insertStmt.executeQuery()){
                while (rs.next()) {
                    Channels.add(GetChannel(rs.getString("name")));                    
                }
            } catch (SQLException e) {
                System.out.println("Error getting info from a channel: " + e.getMessage());
            }
            

        } catch (SQLException e) {
            System.out.println("Error getting channels: " + e.getMessage());
        }
        return Channels;
    }
    public static void AddChannel(String channelName){
        try {
            String sql = "INSERT INTO Channel(name, Created_at) VALUES (?,?)";
            PreparedStatement insertStmt = conn.prepareStatement(sql);
            insertStmt.setString(1, channelName);
            insertStmt.setObject(2, LocalDateTime.now());
            insertStmt.executeUpdate();

            System.out.println(channelName + " is now in  created");

        } catch (SQLException e) {
            System.out.println("Error creating channel: " + e.getMessage());
        }
    }

    public static void AddMessage(String userName, LocalDateTime time, String ChannelName, String type, String Content){
        try {
            String sql = "INSERT INTO Message(username, time, channel, type, content) VALUES (?,?,?,?,?)";
            PreparedStatement insertStmt = conn.prepareStatement(sql);
            insertStmt.setString(1, userName);
            insertStmt.setObject(2, time);
            insertStmt.setObject(3, ChannelName);
            insertStmt.setObject(4, type);
            insertStmt.setObject(5, Content);


            insertStmt.executeUpdate();
            System.out.println("Message is now in  created");


        } catch (SQLException e) {
            System.out.println("Error creating channel: " + e.getMessage());
        }
    }
    /*
    username TEXT References Users(username),
    time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    channel  TEXT References Channel(name), 
    type TEXT CHECK (type in ('text', 'image')),
    content TEXT NOT NULL,
    */
    public static LinkedList<message> GetAllMessagesInChannel(String channel){
        LinkedList<message> messages = new LinkedList<>();
        String sql = "Select * from message where Channel = ?";
        try{
            PreparedStatement insertStmt = conn.prepareStatement(sql);
            insertStmt.setObject(1,channel);
            try(ResultSet rs = insertStmt.executeQuery()){
                while (rs.next()) {
                    String user = rs.getString("username");
                    java.sql.Timestamp sqlTime = rs.getTimestamp("time");
                    LocalDateTime javaTime = sqlTime.toLocalDateTime();                    
                    String type = rs.getString("channel");
                    String content = rs.getString("content");
                    messages.add(new message(user, content, type, javaTime));               
                }
            } catch (SQLException e) {
                System.out.println("Error getting info from a message: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error getting messages in: " + channel + " : " + e.getMessage());
        }
        return messages;
    }
    public static LinkedList<message> GetNewMessagesInChannelFromTimeStamp(String channel, LocalDateTime Timestamp){
        LinkedList<message> messages = new LinkedList<>();
        String sql = "Select * from message where Channel = ? AND time > ?";
        try{
            PreparedStatement insertStmt = conn.prepareStatement(sql);
            insertStmt.setObject(1,channel);
            insertStmt.setObject(2,Timestamp);
            try(ResultSet rs = insertStmt.executeQuery()){
                while (rs.next()) {
                    String user = rs.getString("username");
                    java.sql.Timestamp sqlTime = rs.getTimestamp("time");
                    LocalDateTime javaTime = sqlTime.toLocalDateTime();                    
                    String type = rs.getString("channel");
                    String content = rs.getString("content");
                    messages.add(new message(user, content, type, javaTime));               
                }
            } catch (SQLException e) {
                System.out.println("Error getting info from a message: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error getting messages in: " + channel + " : " + e.getMessage());
        }
        return messages;
    }

}