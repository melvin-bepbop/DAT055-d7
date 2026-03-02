package Models;
import java.util.LinkedList;
import java.time.LocalDateTime;

public class MessagesInChannel {
    private LinkedList<Message> messages;
    private Channel channel;
    private LocalDateTime lastUpdated;

    public MessagesInChannel(Channel channel) {
        this.channel = channel;
        this.messages = new LinkedList<>();
        this.lastUpdated = LocalDateTime.now();
    }

    public void addMessages(LinkedList<Message> newMessages) {
        if (!newMessages.isEmpty()) {
            this.messages.addAll(newMessages);
            this.lastUpdated = LocalDateTime.now(); 
        }
    }

    public Channel getChannel() { return channel; }
    public LinkedList<Message> getAllCachedMessages() { return this.messages; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
}