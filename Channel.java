
import java.time.LocalDateTime;
    
 /**
 * Represents a communication channel within the chat application.
 * This class stores the basic details of a channel, including its name 
 * and the exact date and time it was created.
 */

public class Channel {
    private String channelName;
    private LocalDateTime createdAt;

/**
     * Default constructor.
     * Creates a temporary placeholder channel with the name "temp". 
     * The creation time is automatically set to the current system time.
     */

    public Channel(){
        this.channelName = "temp";
        this.createdAt = LocalDateTime.now();

/**
     * Constructs a new Channel with the specified name.
     * The creation time is automatically set to the current system time.
     *
     * @param channelName the name to be assigned to the new channel
     */

    }
    public Channel(String channelName) {
        this.channelName = channelName;
        this.createdAt = LocalDateTime.now(); 
        
    
    }

/**
     * Constructs a Channel with a specified name and a specific creation time.
     * This constructor is primarily used when loading existing channels 
     * from the database where the creation time is already known.
     *
     * @param channelName the name of the channel
     * @param time        the original date and time the channel was created
     */

    public Channel(String channelName, LocalDateTime time){
        this.channelName = channelName;
        this.createdAt = time;
    }

    /**
     * Retrieves the name of the channel.
     *
     * @return the current name of the channel
     */
    public String getChannelName() {
        return channelName;
    }

   

  
}