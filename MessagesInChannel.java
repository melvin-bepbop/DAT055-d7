import java.util.LinkedList;
import java.time.LocalDateTime;

public class MessagesInChannel {
    private LinkedList<message> Messages;
    private Channel channel;
    private LocalDateTime LastUpdated;

    public MessagesInChannel(Channel channel, IDatabase db){
        this.channel = channel;
        this.Messages = db.GetAllMessagesInChannel(channel.getChannelName());
        this.LastUpdated = LocalDateTime.now();
    }
public LinkedList<message> getNewMessages(IDatabase db) {
    LinkedList<message> freshMessages = db.GetNewMessagesInChannelFromTimeStamp(this.channel.getChannelName(), LastUpdated);
    LastUpdated = LocalDateTime.now();
    
    this.Messages.addAll(freshMessages); 
    
    return freshMessages;
}
    public void updateMessages(LinkedList<message> newMessages){
        for(message mes : newMessages){
            this.Messages.add(mes);
        }
    }
    public Channel getChannel() {
        return channel;
    }
    public LinkedList<message> getAllCachedMessages() {
        return this.Messages;
    }
    
}
