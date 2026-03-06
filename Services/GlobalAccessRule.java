package Services;
import java.util.LinkedList;

import Database.IChannelRepo;
import Database.IUserRepo;
import Models.Channel;
import Models.User;

/**
 * Grants global channel access permissions.
 *
 * This rule gives every user access to every channel. New users get access to all
 * existing channels, and new channels are granted to all existing users.
 */
public class GlobalAccessRule implements IChannelAccessRule {
    
    /**
     * Grants a new user access to all existing channels.
     *
     * @param newUsername username that should receive initial access
     * @param channelRepo repository used to fetch channels and grant access
     */
    @Override
    public void grantInitialAccess(String newUsername, IChannelRepo channelRepo) {
        LinkedList<Channel> allChannels = channelRepo.GetAllChannels();
        for (Channel channel : allChannels) {
            channelRepo.GrantUserPermissionToChannel(newUsername, channel.getChannelName());
        }
    }

    /**
     * Grants all existing users access to a newly created channel.
     *
     * @param newChannelName channel name that was created
     * @param creatorUsername username of the channel creator
     * @param userRepo repository used to fetch users
     * @param channelRepo repository used to grant access
     */
    @Override
    public void grantAccessForNewChannel(String newChannelName, String creatorUsername, IUserRepo userRepo, IChannelRepo channelRepo) {
        LinkedList<User> allUsers = userRepo.GetAllUsers(); 
        for (User user : allUsers) {
            channelRepo.GrantUserPermissionToChannel(user.getUsername(), newChannelName);
        }
    }
}