package Models;
import java.time.LocalDateTime;

@FunctionalInterface
public interface MessageFactory {
    Message create(String user, String content, LocalDateTime time);
}