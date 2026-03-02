package Models;

import java.time.LocalDateTime;

public abstract class Message {
    protected String username;
    protected String content;
    protected LocalDateTime timeStamp;
    protected String type;

    public Message(String user, String cont, String type, LocalDateTime time) {
        this.username = user;
        this.content = cont;
        this.type = type;
        this.timeStamp = time;
    }

    public String getUsername() { return username; }
    public String getContent() { return content; }
    public String getType() { return type; }
    public LocalDateTime getTimeStamp() { return timeStamp; }
}