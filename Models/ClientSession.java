package Models;
import java.util.LinkedList;

public class ClientSession {
    private User activeUser;
    private Channel activeChannel;
    private AccesibleChannels accesibleChannels;
    private LinkedList<MessagesInChannel> msgHistoryInChannels;

    public ClientSession(User user, Channel startChannel, AccesibleChannels initialChannels) {
        this.activeUser = user;
        this.activeChannel = startChannel;
        this.accesibleChannels = initialChannels;
        this.msgHistoryInChannels = new LinkedList<>();
    }

    public User getActiveUser() { return activeUser; }
    public Channel getActiveChannel() { return activeChannel; }
    public AccesibleChannels getAccesibleChannels() { return accesibleChannels; }
    public LinkedList<MessagesInChannel> getMsgHistoryInChannels() { return msgHistoryInChannels; }

    public void changeChannel(Channel newChannel) {
        this.activeChannel = newChannel; 
    }


    public LinkedList<Message> getHistoryForActiveChannel() {
        for (MessagesInChannel folder : msgHistoryInChannels) {
            if (folder.getChannel().getChannelName().equals(activeChannel.getChannelName())) {
                return folder.getAllCachedMessages(); 
            }
        }
        return new LinkedList<>(); 
    }
    public void addChannelHistory(MessagesInChannel msgInChnl){
       msgHistoryInChannels.add(msgInChnl); 
    }
}