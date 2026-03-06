package Services;
import Database.IChannelRepo;
import Database.IUserRepo;


/**
 * Defines rules for granting channel access permissions.
 *
 * Implementations decide which users should be granted access when a new user
 * registers or when a new channel is created.
 */
public interface IChannelAccessRule {
    /**
     * Grants initial access for a newly created user.
     *
     * @param newUsername username that should receive initial access
     * @param channelRepo repository used to grant access
     */
    void grantInitialAccess(String newUsername, IChannelRepo channelRepo);
    
    /**
     * Grants access for a newly created channel according to the rule.
     *
     * @param newChannelName channel name that was created
     * @param creatorUsername username of the channel creator
     * @param userRepo repository used to list users if needed
     * @param channelRepo repository used to grant access
     */
    void grantAccessForNewChannel(String newChannelName, String creatorUsername, IUserRepo userRepo, IChannelRepo channelRepo);
}