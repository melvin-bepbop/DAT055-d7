package Models;
import java.util.LinkedList;
import java.time.LocalDateTime;

/**
 * Holds cached message history for a channel.
 *
 * Stores a list of messages, the associated channel, and a timestamp for
 * when the history was last updated.
 */
public class MessagesInChannel {
    private LinkedList<Message> messages;
    private Channel channel;
    private LocalDateTime lastUpdated;

    /**
     * Creates a new history container for a channel.
     *
     * @param channel channel whose history is stored
     */
    public MessagesInChannel(Channel channel) {
        this.channel = channel;
        this.messages = new LinkedList<>();
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Adds new messages to the history.
     *
     * @param newMessages list of new messages
     */
    public void addMessages(LinkedList<Message> newMessages) {
        if (!newMessages.isEmpty()) {
            this.messages.addAll(newMessages);
            this.lastUpdated = LocalDateTime.now(); 
        }
    }

    /**
     * Returns the channel that the history belongs to.
     *
     * @return channel instance
     */
    public Channel getChannel() { return channel; }

    /**
     * Returns all cached messages.
     *
     * @return list of messages
     */
    public LinkedList<Message> getAllCachedMessages() { return this.messages; }

    /**
     * Returns the time when the history was last updated.
     *
     * @return last updated timestamp
     */
    public LocalDateTime getLastUpdated() { return lastUpdated; }
}