import java.time.LocalDateTime;

public class message {
    private String Username;
    private String Content;
    private LocalDateTime TimeStamp;
    private String type;

    //For creating a brand new message (auto-sets time to NOW)
    public message(String user, String cont, String type) {
        this.Username = user;
        this.Content = cont;
        this.type = type;
        this.TimeStamp = LocalDateTime.now(); 
    }
    
    //For loading an old message from the database (needs exact time)
    public message(String user, String cont, String type, LocalDateTime time) {
        this.Username = user;
        this.Content = cont;
        this.type = type;
        this.TimeStamp = time;
    }
    
    public String getUsername() { return Username; }
    public String getContent() { return Content; }
    public LocalDateTime getTimeStamp() { return TimeStamp; }
    public String getType() { return type; }
}