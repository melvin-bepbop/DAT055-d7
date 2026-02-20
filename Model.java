import java.util.LinkedList; 
import java.time.LocalDateTime;

public class Model {
    private AccesibleChannels accesibleChannels;
    private LinkedList<MessagesInChannel>  MsgHistoryInChannels = new LinkedList<>();
    private Channel ActiveChannel;
    private User ActiveUser;

    public Model(User user, Channel channel){
        this.ActiveChannel = channel;
        this.ActiveUser = user;
        this.MsgHistoryInChannels.add(new MessagesInChannel(channel));
    }
    public void addMessage(String content, String type){
        Database.AddMessage(ActiveUser.getUsername(), LocalDateTime.now(), ActiveChannel.getChannelName(), type, content);
    }
    public LinkedList<message> GetNewMessagesInActiveChannel(){
        MessagesInChannel activeChannelHistory = MsgHistoryInChannels.getFirst();
        for (MessagesInChannel messagesInChannel : MsgHistoryInChannels) {
            if(messagesInChannel.getChannel() == ActiveChannel){
                activeChannelHistory = messagesInChannel;
                break;
            }
        }
        LinkedList<message> newMessages = activeChannelHistory.GetNewMessages();
        activeChannelHistory.updateMessages(newMessages);
        return newMessages;
    }
    public void changeChannel(Channel newChannel){
        this.ActiveChannel = newChannel;
        boolean hasBeenBefore = false;
        MessagesInChannel activeChannelHistory = MsgHistoryInChannels.getFirst();
        for (MessagesInChannel messagesInChannel : MsgHistoryInChannels) {
            if(messagesInChannel.getChannel() == ActiveChannel){
                activeChannelHistory = messagesInChannel;
                hasBeenBefore = true;
                break;
            }
        }
        if(hasBeenBefore){
            LinkedList<message> newMessages = activeChannelHistory.GetNewMessages();
            activeChannelHistory.updateMessages(newMessages);
        }
        else{
            MsgHistoryInChannels.add(new MessagesInChannel(newChannel));
        }
    }
    public AccesibleChannels getAccesibleChannels() {
        return accesibleChannels;
    }
    public Channel getActiveChannel() {
        return ActiveChannel;
    }
    public User getActiveUser() {
        return ActiveUser;
    }
}
