package Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

import Models.Channel;
import Models.User;
import Models.Message;
import Models.MessageFactory;

/**
 * JDBC implementation of user, channel, and message repositories for PostgreSQL.
 *
 * This class uses a single database connection and provides methods for user authentication,
 * channel management, and message persistence and retrieval.
 */
public class PostgresTranslator implements IUserRepo, IChannelRepo, IMessageRepo {
    
    private final String URL = "jdbc:postgresql://localhost:5432/chat_project";
    private final String USER = "postgres";
    private final String PASS = "postgres";
    private Connection conn;

    private final Map<String, MessageFactory> messageRegistry = new HashMap<>();

    /**
     * Registers a factory used to create message objects when reading from the database.
     *
     * @param type message type identifier
     * @param factory factory used to create message instances
     */
    public void registerMessageType(String type, MessageFactory factory) {
        messageRegistry.put(type.toLowerCase(), factory);
    }

    /**
     * Opens a database connection using the configured connection parameters.
     */
    public void connect() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connected to PostgreSQL successfully!");
        } catch (SQLException e) {
            System.err.println("Connection Error: " + e.getMessage());
        }
    }
    
    /**
     * Checks whether a username already exists.
     *
     * @param username username to check
     * @return true if the username exists, otherwise false
     */
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
    
    /**
     * Creates a new user and grants initial channel permissions.
     *
     * @param username new username
     * @param password new password
     * @return true if the user was created, otherwise false
     */
    @Override
    public boolean createUser(String username, String password) {
        if (isUsernameTaken(username)) {
            return false;
        }

        String sql = "INSERT INTO Users(username, password) VALUES(?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            GrantUserPermissionToChannel(username, "General");
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
        
    }
    
    /**
     * Removes the user's active channel association.
     *
     * @param username username to update
     * @param channelName channel name the user is leaving
     */
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
   
    /**
     * Grants permission for a user to access a channel.
     *
     * @param username username to grant access to
     * @param channelName channel to grant access for
     */
    @Override
    public void GrantUserPermissionToChannel(String username, String channelName) {
        String sql = "INSERT INTO UsersInChannel (username, channel) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, channelName);
            stmt.executeUpdate();
            System.out.println("Permission granted: " + username + " -> " + channelName);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { 
                System.out.println("User " + username + " already has access to " + channelName);
            } else {
                System.out.println("Database error during permission grant: " + e.getMessage());
            }
        }
    }

    /**
     * Sets the user's active channel to the given channel name.
     *
     * @param username username to update
     * @param channelName channel name to set as active
     */
    @Override
    public void UserJoinChannel(String username, String channelName) {
        try {
            String deleteOldActive = "DELETE FROM UserInActiveChannel WHERE username = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteOldActive);
            deleteStmt.setString(1, username);
            deleteStmt.executeUpdate();

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

    /**
     * Fetches a channel by name.
     *
     * @param channelname channel name
     * @return channel instance
     */
    @Override
    public Channel GetChannel(String channelname){
        Channel channel = new Channel("Placeholder"); 
        String sql = "SELECT * FROM Channel WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, channelname);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime javaTime = rs.getTimestamp("Created_at").toLocalDateTime();
                    channel = new Channel(rs.getString("name"), javaTime);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting channel: " + e.getMessage());
        }
        return channel;
    }
    
    /**
     * Fetches all channels.
     *
     * @return list of all channels
     */
    @Override
    public LinkedList<Channel> GetAllChannels(){
        LinkedList<Channel> channels = new LinkedList<>();
        String sql = "SELECT name FROM Channel";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                channels.add(GetChannel(rs.getString("name")));                    
            }
        } catch (SQLException e) {
            System.out.println("Error getting channels: " + e.getMessage());
        }
        return channels;
    }
    
    /**
     * Fetches channels that a user has permission to access.
     *
     * @param user username
     * @return list of channels accessible to the user
     */
    @Override
    public LinkedList<Channel> GetAllChannelsWhereUserIn(String user){
        LinkedList<Channel> channels = new LinkedList<>();
        String sql = "SELECT channel AS name FROM UsersInChannel WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, user);
            try(ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    channels.add(GetChannel(rs.getString("name")));                    
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting user channels: " + e.getMessage());
        }
        return channels;
    }

    /**
     * Creates a new channel.
     *
     * @param channelName channel name to create
     */
    @Override
    public void AddChannel(String channelName){
        String sql = "INSERT INTO Channel(name, Created_at) VALUES (?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, channelName);
            stmt.setObject(2, LocalDateTime.now());
            stmt.executeUpdate();
            System.out.println(channelName + " is now created");
        } catch (SQLException e) {
            System.out.println("Error creating channel: " + e.getMessage());
        }
    }

    /**
     * Stores a message.
     *
     * @param userName message author username
     * @param time message timestamp
     * @param channelName channel name
     * @param type message type identifier
     * @param content message content
     */
    @Override
    public void AddMessage(String userName, LocalDateTime time, String channelName, String type, String content){
        String sql = "INSERT INTO Message(username, time, channel, type, content) VALUES (?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.setObject(2, time);
            stmt.setObject(3, channelName);
            stmt.setString(4, type);
            stmt.setString(5, content);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }
    
    /**
     * Fetches all messages in a channel.
     *
     * @param channel channel name
     * @return list of messages
     */
    @Override
    public LinkedList<Message> GetAllMessagesInChannel(String channel){
        LinkedList<Message> messages = new LinkedList<>();
        String sql = "SELECT * FROM message WHERE Channel = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, channel);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("type").toLowerCase();
                    MessageFactory factory = messageRegistry.get(type);
                    
                    if (factory != null) {
                        messages.add(factory.create(
                            rs.getString("username"),
                            rs.getString("content"),
                            rs.getTimestamp("time").toLocalDateTime()
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting messages: " + e.getMessage());
        }
        return messages;
    }
    
    /**
     * Fetches messages newer than a given timestamp in a channel.
     *
     * @param channel channel name
     * @param timestamp lower bound timestamp (exclusive)
     * @return list of messages after the given timestamp
     */
    @Override
    public LinkedList<Message> GetNewMessagesInChannelFromTimeStamp(String channel, LocalDateTime timestamp){
        LinkedList<Message> messages = new LinkedList<>();
        String sql = "SELECT * FROM message WHERE Channel = ? AND time > ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setObject(1, channel);
            stmt.setObject(2, timestamp);
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    String type = rs.getString("type").toLowerCase();
                    MessageFactory factory = messageRegistry.get(type);
                    
                    if (factory != null) {
                        messages.add(factory.create(
                            rs.getString("username"),
                            rs.getString("content"),
                            rs.getTimestamp("time").toLocalDateTime()
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting new messages: " + e.getMessage());
        }
        return messages;
    }
    
    /**
     * Verifies a username and password combination.
     *
     * @param username username to authenticate
     * @param password password to authenticate
     * @return true if credentials are valid, otherwise false
     */
    @Override
    public boolean loginUser(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns all users.
     *
     * @return list of users
     */
    @Override
    public LinkedList<User> GetAllUsers() {
        LinkedList<User> userList = new LinkedList<>();
        String sql = "SELECT username, password FROM Users"; 
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                userList.add(new User(rs.getString("username"), rs.getString("password")));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
        return userList;
    }
}