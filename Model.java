import java.util.LinkedList; 
import java.time.LocalDateTime;
import java.util.HashMap;

public class Model {
    private AccesibleChannels accesibleChannels;
    private LinkedList<MessagesInChannel> MsgHistoryInChannels = new LinkedList<>();
    private Channel ActiveChannel;
    private User ActiveUser;
    
    // NEW: This map tracks how many messages we've drawn on the screen per channel 
    // so we don't accidentally draw duplicates when you send a new message!
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

    public LinkedList<message> GetNewMessagesInActiveChannel(){
        // 1. Ask the Database for ALL messages in the current channel
        LinkedList<message> allMessages = Database.GetAllMessagesInChannel(ActiveChannel.getChannelName());
        
        // 2. Find out how many messages we have ALREADY drawn on the screen
        int alreadyDrawn = displayedMessageCount.getOrDefault(ActiveChannel.getChannelName(), 0);
        
        // 3. Collect ONLY the messages we haven't drawn yet
        LinkedList<message> newMessages = new LinkedList<>();
        for (int i = alreadyDrawn; i < allMessages.size(); i++) {
            newMessages.add(allMessages.get(i));
        }
        
        // 4. Update our tracker so we don't draw these again next time
        displayedMessageCount.put(ActiveChannel.getChannelName(), allMessages.size());
        
        return newMessages;
    }

    public void changeChannel(Channel newChannel){
        this.ActiveChannel = newChannel;
        
        // When you click a channel button, the UI clears the screen (gui.clearChat()).
        // So we MUST reset our counter to 0 so GetNewMessagesInActiveChannel() 
        // gives us the full history to redraw!
        displayedMessageCount.put(newChannel.getChannelName(), 0);
        
        // Keeps your original tracking logic intact
        boolean hasBeenBefore = false;
        for (MessagesInChannel messagesInChannel : MsgHistoryInChannels) {
            // Fix: Replaced '==' with '.getChannelName().equals()' to compare properly
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