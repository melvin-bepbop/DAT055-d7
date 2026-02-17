import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Channel {
    private String channelName;
    private LocalDateTime createdAt;
    
    //private List<User> membersList;
    //private List<Message> messageList;
   // private List<User> blockList; 
    public Channel(){
        this.channelName = "temp";
        this.createdAt = LocalDateTime.now();
    }
    public Channel(String channelName) {
        this.channelName = channelName;
        this.createdAt = LocalDateTime.now(); 
        
       // this.membersList = new ArrayList<>();
       // this.messageList = new ArrayList<>();
       // this.blockList = new ArrayList<>();
    }
    public Channel(String channelName, LocalDateTime time){
        this.channelName = channelName;
        this.createdAt = time;
    }

   
    /*public void addUser(User user) {
        if (!membersList.contains(user)) {
            membersList.add(user);
            Database.joinChannel(user.getUsername(), this.channelName);
        }
    }

    public void removeUser(User user) {
        if (membersList.contains(user)){
        membersList.remove(user);
         Database.leaveChannel(user.getUsername(), this.channelName);
        }else{
        throw new IllegalArgumentException("User is not in the channel"); 
    }
    }
//updatera sql på något sätt också
    public void createNewMessage(User sender, String content) {
        Message msg = new Message(sender.getUsername(), content, "text");
        messageList.add(msg);
    }*/

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}