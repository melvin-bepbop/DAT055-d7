package Services;
import java.util.LinkedList;

import Database.IChannelRepo;
import Database.IUserRepo;
import Models.Channel;
import Models.User;

public class GlobalAccessRule implements IChannelAccessRule {
    
    @Override
    public void grantInitialAccess(String newUsername, IChannelRepo channelRepo) {
        LinkedList<Channel> allChannels = channelRepo.GetAllChannels();
        for (Channel channel : allChannels) {
            channelRepo.GrantUserPermissionToChannel(newUsername, channel.getChannelName());
        }
    }

    @Override
    public void grantAccessForNewChannel(String newChannelName, String creatorUsername, IUserRepo userRepo, IChannelRepo channelRepo) {
        LinkedList<User> allUsers = userRepo.GetAllUsers(); 
        for (User user : allUsers) {
            channelRepo.GrantUserPermissionToChannel(user.getUsername(), newChannelName);
        }
    }
}