package Models;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

/**
 * Holds client session data.
 *
 * Stores active user, active channel, accessible channels, and cached
 * message history per channel.
 */
public class ClientSession implements ISessionModel {
    private User activeUser;
    private Channel activeChannel;
    private AccesibleChannels accesibleChannels;
    private LinkedList<MessagesInChannel> msgHistoryInChannels;
    private final PropertyChangeSupport support;

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
        this.support = new PropertyChangeSupport(this);
    }
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
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
        Channel oldChannel = this.activeChannel;
        this.activeChannel = newChannel; 
        
        support.firePropertyChange("activeChannelChanged", oldChannel, newChannel);
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
       if (msgInChnl.getChannel().getChannelName().equals(activeChannel.getChannelName())) {
           support.firePropertyChange("chatHistoryUpdated", null, getHistoryForActiveChannel());
       }
    }
    /**
     * Adds a newly created/joined channel to the accessible list and notifies the View.
     */
    public void addAccessibleChannel(Channel channel) {
        this.accesibleChannels.addChannel(channel);
        support.firePropertyChange("newChannelAdded", null, channel);
    }
    /**
     * Appends new messages to an existing channel's history and notifies observers.
     * Moving this logic here keeps the Controller out of the Model's internal lists.
     *
     * @param channel the channel to update
     * @param msgs the new messages to append
     */
    public void appendMessagesToChannel(Channel channel, LinkedList<Message> msgs) {
        for (MessagesInChannel folder : msgHistoryInChannels) {
            if (folder.getChannel().getChannelName().equals(channel.getChannelName())) {
                folder.addMessages(msgs);
                
                
                if (channel.getChannelName().equals(activeChannel.getChannelName())) {
                    support.firePropertyChange("chatHistoryUpdated", null, getHistoryForActiveChannel());
                }
                return;
            }
        }
    }
}