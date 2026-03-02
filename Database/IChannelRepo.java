package Database;
import java.util.LinkedList;

import Models.Channel;
public interface IChannelRepo {
    void UserJoinChannel(String username, String channelName);
    void UserLeaveChannel(String username, String channelName);
    Channel GetChannel(String channelname);
    LinkedList<Channel> GetAllChannels();
    LinkedList<Channel> GetAllChannelsWhereUserIn(String user);
    void AddChannel(String channelName);
    void GrantUserPermissionToChannel(String username, String channelName);

}
