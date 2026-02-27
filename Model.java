import java.util.LinkedList; 
import java.time.LocalDateTime;
import java.util.HashMap;

public class Model {
    private AccesibleChannels accesibleChannels;
    private LinkedList<MessagesInChannel> MsgHistoryInChannels = new LinkedList<>();
    private Channel ActiveChannel;
    private User ActiveUser;
    
    private HashMap<String, Integer> displayedMessageCount = new HashMap<>();

    public Model(User user, Channel channel){
        this.ActiveChannel = channel;
        this.ActiveUser = user;
        this.accesibleChannels = new AccesibleChannels();
        this.MsgHistoryInChannels.add(new MessagesInChannel(channel));
    }

    public void addMessage(String content, String type){
        Database.AddMessage(ActiveUser.getUsername(), LocalDateTime.now(), ActiveChannel.getChannelName(), type, content);
    }

    //for getting all
    public LinkedList<message> GetNewMessagesInActiveChannel(){
        LinkedList<message> allMessages = Database.GetAllMessagesInChannel(ActiveChannel.getChannelName());
        
        int alreadyDrawn = displayedMessageCount.getOrDefault(ActiveChannel.getChannelName(), 0);
        
        LinkedList<message> newMessages = new LinkedList<>();
        for (int i = alreadyDrawn; i < allMessages.size(); i++) {
            newMessages.add(allMessages.get(i));
        }
        
        displayedMessageCount.put(ActiveChannel.getChannelName(), allMessages.size());
        
        return newMessages;
    }

    public void changeChannel(Channel newChannel){
        this.ActiveChannel = newChannel;
        
        displayedMessageCount.put(newChannel.getChannelName(), 0);
        
        boolean hasBeenBefore = false;
        for (MessagesInChannel messagesInChannel : MsgHistoryInChannels) {
            if(messagesInChannel.getChannel().getChannelName().equals(ActiveChannel.getChannelName())){ 
                hasBeenBefore = true;
                break;
            }
        }
        if(!hasBeenBefore){
            MsgHistoryInChannels.add(new MessagesInChannel(newChannel));
        }
    }

    public AccesibleChannels getAccesibleChannels() { return accesibleChannels; }
    public Channel getActiveChannel() { return ActiveChannel; }
    public User getActiveUser() { return ActiveUser; }
}