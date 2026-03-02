package Services;
import java.util.LinkedList;

import Database.IChannelRepo;
import Database.IUserRepo;
import Models.Channel;
import Models.User;

public class ChannelService {
    private IChannelRepo channelRepo;
    private IUserRepo userRepo;
    private IChannelAccessRule accessRule;

public ChannelService(IChannelRepo channelRepo, IUserRepo userRepo, IChannelAccessRule accessRule) {
        this.channelRepo = channelRepo;
        this.userRepo = userRepo;
        this.accessRule = accessRule;
    }
    public void handleNewUserPermissions(String newUsername) {
        accessRule.grantInitialAccess(newUsername, channelRepo);
    }

public void createNewChannel(String newChannelName, User creator) {
        channelRepo.AddChannel(newChannelName);
        channelRepo.UserJoinChannel(creator.getUsername(), newChannelName);
        
        accessRule.grantAccessForNewChannel(newChannelName, creator.getUsername(), userRepo, channelRepo);
    }

    public void updateUserActiveChannel(User user, Channel newChannel) {
        channelRepo.UserJoinChannel(user.getUsername(), newChannel.getChannelName());
    }
    
    public LinkedList<Channel> loadUserChannels(User user) {
        return channelRepo.GetAllChannelsWhereUserIn(user.getUsername());
    }
    public void grantAccessToAllExistingChannels(String newUsername) {
        LinkedList<Channel> allChannels = channelRepo.GetAllChannels();
        for (Channel channel : allChannels) {
            channelRepo.GrantUserPermissionToChannel(newUsername, channel.getChannelName());
        }
    }
}