import java.util.LinkedList;
import java.time.LocalDateTime;

public interface IDatabase {
    // Connection
    void connect();

    // User & Login
    boolean isUsernameTaken(String username);
    boolean createUser(String username, String password);
    boolean loginUser(String username, String password); 
    LinkedList<User> GetAllUsers();

    // Channel Management
    void UserJoinChannel(String username, String channelName);
    void UserLeaveChannel(String username, String channelName);
    Channel GetChannel(String channelname);
    LinkedList<Channel> GetAllChannels();
    LinkedList<Channel> GetAllChannelsWhereUserIn(String user);
    void AddChannel(String channelName);
    void GrantUserPermissionToChannel(String username, String channelName);

    // Message Management
    void AddMessage(String userName, LocalDateTime time, String channelName, String type, String content);
    LinkedList<message> GetAllMessagesInChannel(String channel);
    LinkedList<message> GetNewMessagesInChannelFromTimeStamp(String channel, LocalDateTime Timestamp);
}