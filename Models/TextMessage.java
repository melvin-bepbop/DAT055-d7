package Models;
import java.time.LocalDateTime;

/**
 * Message type for text messages.
 *
 * Automatically sets the type to "text" in the Message base class.
 */
public class TextMessage extends Message {
    /**
     * Creates a new text message.
     *
     * @param user sender username
     * @param cont message text content
     * @param time message timestamp
     */
    public TextMessage(String user, String cont, LocalDateTime time) {
        super(user, cont, "text", time);
    }
}