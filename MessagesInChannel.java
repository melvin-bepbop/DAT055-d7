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
    public void UpdateMessageList(){
        LinkedList<message> newMessages = Database.GetNewMessagesInChannelFromTimeStamp(this.channel.getChannelName(), LastUpdated);
        for(message mes : newMessages){
            this.Messages.add(mes);
        }
    }
}
