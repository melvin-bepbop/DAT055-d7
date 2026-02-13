import java.time.LocalDateTime;

public class message {
    private String Username;
    private String Content;
    private LocalDateTime TimeStamp;
    private String type;
    public message(String user, String cont, LocalDateTime time, String type){
        this.Username = user;
        this.Content = cont;
        this.TimeStamp = time;
        this.type = type;
    }
    
    public String getUsername() {
        return Username;
    }
    public String getContent() {
        return Content;
    }
    public LocalDateTime getTimeStamp() {
        return TimeStamp;
    }
    public String getType() {
        return type;
    }
}
