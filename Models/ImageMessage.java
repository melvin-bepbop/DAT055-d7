package Models;
import java.time.LocalDateTime;

public class ImageMessage extends Message {
    public ImageMessage(String user, String cont, LocalDateTime time) {
        super(user, cont, "image", time);
    }
}