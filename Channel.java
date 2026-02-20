
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