package Models;
import java.util.LinkedList;

/**
 * Holds client session data.
 *
 * Stores active user, active channel, accessible channels, and cached
 * message history per channel.
 */
public class ClientSession {
    private User activeUser;
    private Channel activeChannel;
    private AccesibleChannels accesibleChannels;
    private LinkedList<MessagesInChannel> msgHistoryInChannels;

    /**
     * Creates a new client session.
     *
     * @param user active user
     * @param startChannel initial active channel
     * @param initialChannels channels the user can access
     */
    public ClientSession(User user, Channel startChannel, AccesibleChannels initialChannels) {
        this.activeUser = user;
        this.activeChannel = startChannel;
        this.accesibleChannels = initialChannels;
        this.msgHistoryInChannels = new LinkedList<>();
    }

    /**
     * Returns the active user.
     *
     * @return active user
     */
    public User getActiveUser() { return activeUser; }

    /**
     * Returns the active channel.
     *
     * @return active channel
     */
    public Channel getActiveChannel() { return activeChannel; }

    /**
     * Returns the container of accessible channels.
     *
     * @return AccesibleChannels instance
     */
    public AccesibleChannels getAccesibleChannels() { return accesibleChannels; }

    /**
     * Returns cached message history per channel.
     *
     * @return list of MessagesInChannel objects
     */
    public LinkedList<MessagesInChannel> getMsgHistoryInChannels() { return msgHistoryInChannels; }

    /**
     * Changes the active channel in the session.
     *
     * @param newChannel new active channel
     */
    public void changeChannel(Channel newChannel) {
        this.activeChannel = newChannel; 
    }
    /**
     * Retrieves history messages for the active channel.
     *
     * @return list of messages, or an empty list if no history exists
     */
    public LinkedList<Message> getHistoryForActiveChannel() {
        for (MessagesInChannel folder : msgHistoryInChannels) {
            if (folder.getChannel().getChannelName().equals(activeChannel.getChannelName())) {
                return folder.getAllCachedMessages(); 
            }
        }
        return new LinkedList<>(); 
    }
    /**
     * Adds cached history for a channel.
     *
     * @param msgInChnl container holding the channel history
     */
    public void addChannelHistory(MessagesInChannel msgInChnl){
       msgHistoryInChannels.add(msgInChnl); 
    }
}