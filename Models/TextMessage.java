package Models;
import java.time.LocalDateTime;

public class TextMessage extends Message {
    public TextMessage(String user, String cont, LocalDateTime time) {
        super(user, cont, "text", time);
    }
}