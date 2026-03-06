package Models;
import java.time.LocalDateTime;

@FunctionalInterface
/**
 * Factory abstraction for creating Message objects.
 *
 * Used to map message types to construction logic.
 */
public interface MessageFactory {
    /**
     * Creates a new message.
     *
     * @param user sender username
     * @param content message content
     * @param time message timestamp
     * @return new Message instance
     */
    Message create(String user, String content, LocalDateTime time);
}