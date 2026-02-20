import java.util.LinkedList;
import java.time.LocalDateTime;

public class MessagesInChannel {
    private LinkedList<message> Messages;
    private Channel channel;
    private LocalDateTime LastUpdated;

    public MessagesInChannel(Channel channel){
        this.channel = channel;
        this.Messages = Database.GetAllMessagesInChannel(channel.getChannelName());
        this.LastUpdated = LocalDateTime.now();
    }
    public LinkedList<message> GetNewMessages(){
        LinkedList<message> newMessages = Database.GetNewMessagesInChannelFromTimeStamp(this.channel.getChannelName(), LastUpdated);
        LastUpdated = LocalDateTime.now();
        return newMessages;
    }
    public void updateMessages(LinkedList<message> newMessages){
        for(message mes : newMessages){
            this.Messages.add(mes);
        }
    }
    public Channel getChannel() {
        return channel;
    }
    
}
