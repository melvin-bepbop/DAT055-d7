import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/chat_project";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";

    private static Connection conn;

    // Connect to Postgres 
    public static void connect() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connected to PostgreSQL successfully!");
        } catch (SQLException e) {
            System.err.println("Connection Error: " + e.getMessage());
        }
    }
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
    public static boolean createUser(String username, String password) {
    if (isUsernameTaken(username)) {
        return false;
    }

    String sql = "INSERT INTO Users(username, password) VALUES(?, ?)";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        
        pstmt.executeUpdate();
        return true;
        
    } catch (SQLException e) {
        return false;
    }
}
    public static void UserLeaveChannel(String username, String channelName) {
        try {
            String deleteSql = "DELETE FROM UserInActiveChannel WHERE username = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
            deleteStmt.setString(1, username);
            deleteStmt.executeUpdate();

            System.out.println(username + " is now removed from their channel");

        } catch (SQLException e) {
            System.out.println("Error leaving channel: " + e.getMessage());
        }
    }
    public static void UserJoinChannel(String username, String channelName) {
        try {
            String insertSql = "INSERT INTO UserInActiveChannel (username, channel) VALUES (?, ?)";
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
public static LinkedList<Channel> GetAllChannelsWhereUserIn(String user){
        LinkedList<Channel> Channels = new LinkedList<>();
        String sql = "SELECT channel AS name FROM UsersInChannel WHERE username = ?";
        try{
            PreparedStatement insertStmt = conn.prepareStatement(sql);
            insertStmt.setString(1, user);

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
                    String type = rs.getString("type");
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
    //Försök logga in en användare
    public static boolean loginUser(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // True om användaren och lösenordet matchar
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            return false;
        }
    }
}