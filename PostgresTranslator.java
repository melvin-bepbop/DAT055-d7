import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;


public class PostgresTranslator implements IDatabase {
    
    private final String URL = "jdbc:postgresql://localhost:5432/chat_project";
    private final String USER = "postgres";
    private final String PASS = "postgres";

   
    private Connection conn;

    // Connect to Postgres 
    @Override
    public void connect() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connected to PostgreSQL successfully!");
        } catch (SQLException e) {
            System.err.println("Connection Error: " + e.getMessage());
        }
    }
    
    @Override
    public boolean isUsernameTaken(String username) {
        String sql = "SELECT username FROM Users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); 
        } catch (SQLException e) {
            return true; 
        }
    }
    
    @Override
    public boolean createUser(String username, String password) {
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
    
    @Override
    public void UserLeaveChannel(String username, String channelName) {
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
   
    @Override
public void GrantUserPermissionToChannel(String username, String channelName) {
    // This query only hits the "Membership" table, not the "Active" table
    String sql = "INSERT INTO UsersInChannel (username, channel) VALUES (?, ?)";
    
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, username);
        stmt.setString(2, channelName);
        stmt.executeUpdate();
        
        // Quietly log success
        System.out.println("Permission granted: " + username + " -> " + channelName);
        
    } catch (SQLException e) {
        // If the user already has access, we don't want the app to crash or show errors
        if (e.getSQLState().equals("23505")) { 
            System.out.println("User " + username + " already has access to " + channelName);
        } else {
            System.out.println("Database error during permission grant: " + e.getMessage());
        }
    }
}
    @Override
public void UserJoinChannel(String username, String channelName) {
    try {
        // 1. Clear their OLD active channel
        String deleteOldActive = "DELETE FROM UserInActiveChannel WHERE username = ?";
        PreparedStatement deleteStmt = conn.prepareStatement(deleteOldActive);
        deleteStmt.setString(1, username);
        deleteStmt.executeUpdate();

        // 2. Insert the NEW active channel
        String insertSql = "INSERT INTO UserInActiveChannel (username, channel) VALUES (?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
        insertStmt.setString(1, username);
        insertStmt.setString(2, channelName);
        insertStmt.executeUpdate();

        System.out.println(username + " is now looking at " + channelName);

    } catch (SQLException e) {
        System.out.println("Error updating active channel: " + e.getMessage());
    }
}
    @Override
    public Channel GetChannel(String channelname){
        Channel channel = new Channel("Placeholder"); 
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
    
    @Override
    public LinkedList<Channel> GetAllChannels(){
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
    
    @Override
    public LinkedList<Channel> GetAllChannelsWhereUserIn(String user){
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

    @Override
    public void AddChannel(String channelName){
        try {
            String sql = "INSERT INTO Channel(name, Created_at) VALUES (?,?)";
            PreparedStatement insertStmt = conn.prepareStatement(sql);
            insertStmt.setString(1, channelName);
            insertStmt.setObject(2, LocalDateTime.now());
            insertStmt.executeUpdate();

            System.out.println(channelName + " is now created");

        } catch (SQLException e) {
            System.out.println("Error creating channel: " + e.getMessage());
        }
    }

    @Override
    public void AddMessage(String userName, LocalDateTime time, String ChannelName, String type, String Content){
        try {
            String sql = "INSERT INTO Message(username, time, channel, type, content) VALUES (?,?,?,?,?)";
            PreparedStatement insertStmt = conn.prepareStatement(sql);
            insertStmt.setString(1, userName);
            insertStmt.setObject(2, time);
            insertStmt.setObject(3, ChannelName);
            insertStmt.setObject(4, type);
            insertStmt.setObject(5, Content);

            insertStmt.executeUpdate();
            System.out.println("Message is now saved to database");

        } catch (SQLException e) {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }
    
    @Override
    public LinkedList<message> GetAllMessagesInChannel(String channel){
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
    
    @Override
    public LinkedList<message> GetNewMessagesInChannelFromTimeStamp(String channel, LocalDateTime Timestamp){
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
    
    @Override
    public boolean loginUser(String username, String password) {
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
@Override
    public LinkedList<User> GetAllUsers() {
        LinkedList<User> userList = new LinkedList<>();
        // Simple SQL to grab everyone in the database
        String sql = "SELECT username, password FROM Users"; 

        try {
            // Assuming your connection variable is named 'connection' or 'conn'
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String fetchedUsername = rs.getString("username");
                String fetchedPassword = rs.getString("password");
                
                // Create a clean User object (our POJO!) and add it to the list
                User foundUser = new User(fetchedUsername, fetchedPassword);
                userList.add(foundUser);
            }
        } catch (Exception e) {
            System.out.println("Error fetching all users: " + e.getMessage());
        }

        return userList;
    }
}