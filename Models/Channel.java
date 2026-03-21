package Models;

import java.time.LocalDateTime;
    
/**
 * Represents a communication channel in the application.
 *
 * Stores the channel name and the time when the channel was created.
 */
public class Channel {
    private String channelName;
    private LocalDateTime createdAt;

    /**
     * Creates a temporary default channel named "temp".
     */
    public Channel(){
        this.channelName = "temp";
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Creates a new channel with the given name.
     *
     * @param channelName channel name
     */
    public Channel(String channelName) {
        this.channelName = channelName;
        this.createdAt = LocalDateTime.now(); 
        
    
    }

    /**
     * Creates a channel with the given name and a specific creation time.
     *
     * @param channelName channel name
     * @param time time when the channel was created
     */
    public Channel(String channelName, LocalDateTime time){
        this.channelName = channelName;
        this.createdAt = time;
    }

    /**
     * Returns the channel name.
     *
     * @return channel name
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * Returns the time when the channel was created.
     *
     * @return creation time
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
   

  
}