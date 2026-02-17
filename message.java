import java.time.LocalDateTime;

public class message {
    private String Username;
    private String Content;
    private LocalDateTime TimeStamp;
    private String type;
<<<<<<< Updated upstream
    public message(String user, String cont, LocalDateTime time, String type){
=======
    public Message(String user, String cont, String type){
>>>>>>> Stashed changes
        this.Username = user;
        this.Content = cont;
        this.TimeStamp = LocalDateTime.now();
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
