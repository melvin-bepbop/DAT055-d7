package Services;
import java.util.LinkedList;

import Database.IChannelRepo;
import Database.IUserRepo;
import Models.Channel;
import Models.User;

/**
 * Provides channel-related application logic.
 *
 * This service coordinates channel creation, permissions, and active channel updates
 * using repository interfaces and an access rule strategy.
 */
public class ChannelService {
    private IChannelRepo channelRepo;
    private IUserRepo userRepo;
    private IChannelAccessRule accessRule;

    /**
     * Creates a channel service.
     *
     * @param channelRepo repository used for channel operations
     * @param userRepo repository used for user operations
     * @param accessRule rule implementation for permission management
     */
public ChannelService(IChannelRepo channelRepo, IUserRepo userRepo, IChannelAccessRule accessRule) {
        this.channelRepo = channelRepo;
        this.userRepo = userRepo;
        this.accessRule = accessRule;
    }

    /**
     * Applies initial channel permissions for a newly created user.
     *
     * @param newUsername username that should receive initial access
     */
    public void handleNewUserPermissions(String newUsername) {
        accessRule.grantInitialAccess(newUsername, channelRepo);
    }

    /**
     * Creates a new channel and updates permissions for users according to the access rule.
     *
     * @param newChannelName channel name to create
     * @param creator user creating the channel
     */
public void createNewChannel(String newChannelName, User creator) {
        channelRepo.AddChannel(newChannelName);
        channelRepo.UserJoinChannel(creator.getUsername(), newChannelName);
        
        accessRule.grantAccessForNewChannel(newChannelName, creator.getUsername(), userRepo, channelRepo);
    }

    /**
     * Sets the user's active channel in the data store.
     *
     * @param user user to update
     * @param newChannel new active channel
     */
    public void updateUserActiveChannel(User user, Channel newChannel) {
        channelRepo.UserJoinChannel(user.getUsername(), newChannel.getChannelName());
    }
    
    /**
     * Loads channels that the given user is allowed to access.
     *
     * @param user user to load channels for
     * @return list of accessible channels
     */
    public LinkedList<Channel> loadUserChannels(User user) {
        return channelRepo.GetAllChannelsWhereUserIn(user.getUsername());
    }

    /**
     * Grants a user access to all existing channels.
     *
     * @param newUsername username to grant permissions for
     */
    public void grantAccessToAllExistingChannels(String newUsername) {
        LinkedList<Channel> allChannels = channelRepo.GetAllChannels();
        for (Channel channel : allChannels) {
            channelRepo.GrantUserPermissionToChannel(newUsername, channel.getChannelName());
        }
    }
}