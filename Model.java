import java.util.LinkedList; 
import java.time.LocalDateTime;
import java.util.HashMap;

public class Model {
    private AccesibleChannels accesibleChannels;
    private LinkedList<MessagesInChannel> MsgHistoryInChannels = new LinkedList<>();
    private Channel ActiveChannel;
    private User ActiveUser;
    private HashMap<String, Integer> displayedMessageCount = new HashMap<>();
    private IDatabase db;

public Model(User user, Channel channel, IDatabase db){
        this.ActiveChannel = channel;
        this.ActiveUser = user;
        this.db = db;
        LinkedList<Channel> myChannels = db.GetAllChannelsWhereUserIn(user.getUsername());
        this.accesibleChannels = new AccesibleChannels(myChannels);
        this.MsgHistoryInChannels.add(new MessagesInChannel(channel, db));
    }

    public void addMessage(String content, String type){
        db.AddMessage(ActiveUser.getUsername(), LocalDateTime.now(), ActiveChannel.getChannelName(), type, content);
    }

    //for getting all
public LinkedList<message> GetNewMessagesInActiveChannel() {
    // 1. Find the folder for the current channel
    for (MessagesInChannel folder : MsgHistoryInChannels) {
        if (folder.getChannel().getChannelName().equals(ActiveChannel.getChannelName())) {
            // 2. Tell the folder to fetch new stuff using our DB toolbox
            return folder.getNewMessages(this.db);
        }
    }
    return new LinkedList<>(); // Return empty if channel not found
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
            MsgHistoryInChannels.add(new MessagesInChannel(newChannel, db));
        }
    }

public void createNewGlobalChannel(String newChannelName) {
    db.AddChannel(newChannelName);
    
    LinkedList<User> allUsers = db.GetAllUsers(); 
    for (User user : allUsers) {

        db.GrantUserPermissionToChannel(user.getUsername(), newChannelName);
    }
    db.UserJoinChannel(ActiveUser.getUsername(), newChannelName);
    Channel newChan = new Channel(newChannelName);
    this.accesibleChannels.getChannels().add(newChan);
}
public LinkedList<message> getHistoryForActiveChannel() {
        for (MessagesInChannel folder : MsgHistoryInChannels) {
            if (folder.getChannel().getChannelName().equals(ActiveChannel.getChannelName())) {
                return folder.getAllCachedMessages(); 
            }
        }
        return new LinkedList<>();
    }

    public AccesibleChannels getAccesibleChannels() { return accesibleChannels; }
    public Channel getActiveChannel() { return ActiveChannel; }
    public User getActiveUser() { return ActiveUser; }
}