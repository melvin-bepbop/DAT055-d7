package Database;
import java.util.LinkedList;

import Models.Channel;

/**
 * Repository interface for channel-related database operations.
 *
 * Implementations handle channel creation, membership, and access permissions.
 */
public interface IChannelRepo {
    /**
     * Sets the user's active channel.
     *
     * @param username username to update
     * @param channelName channel name to set as active
     */
    void UserJoinChannel(String username, String channelName);

    /**
     * Removes the user's active channel association.
     *
     * @param username username to update
     * @param channelName channel name the user is leaving
     */
    void UserLeaveChannel(String username, String channelName);

    /**
     * Fetches a channel by name.
     *
     * @param channelname channel name
     * @return the channel if found, otherwise an implementation-defined placeholder
     */
    Channel GetChannel(String channelname);

    /**
     * Fetches all channels.
     *
     * @return list of all channels
     */
    LinkedList<Channel> GetAllChannels();

    /**
     * Fetches channels that a user has permission to access.
     *
     * @param user username
     * @return list of channels accessible to the user
     */
    LinkedList<Channel> GetAllChannelsWhereUserIn(String user);

    /**
     * Creates a new channel.
     *
     * @param channelName channel name to create
     */
    void AddChannel(String channelName);

    /**
     * Grants permission for a user to access a channel.
     *
     * @param username username to grant access to
     * @param channelName channel to grant access for
     */
    void GrantUserPermissionToChannel(String username, String channelName);

}
