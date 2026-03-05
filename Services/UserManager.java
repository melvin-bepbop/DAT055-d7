package Services;
/*import java.util.LinkedList;

import Database.IChannelRepo;
import Models.Channel;
import Models.User;

public class UserManager {

    private IChannelRepo channelRepo;

    public UserManager(IChannelRepo channelRepo) {
        this.channelRepo = channelRepo;
    }

    public LinkedList<Channel> getAllowedChannels(User user) {
        return channelRepo.GetAllChannelsWhereUserIn(user.getUsername());
    }

    public void grantAccess(User user, Channel channel) {
        channelRepo.UserJoinChannel(user.getUsername(), channel.getChannelName());
    }

    public void revokeAccess(User user, Channel channel) {
        channelRepo.UserLeaveChannel(user.getUsername(), channel.getChannelName());
    }
}*/