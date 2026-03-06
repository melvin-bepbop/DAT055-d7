package Models;
import java.time.LocalDateTime;

/**
 * Message type for image messages.
 *
 * Automatically sets the type to "image" in the Message base class.
 */
public class ImageMessage extends Message {
    /**
     * Creates a new image message.
     *
     * @param user sender username
     * @param cont Base64 image content or path, depending on the protocol
     * @param time message timestamp
     */
    public ImageMessage(String user, String cont, LocalDateTime time) {
        super(user, cont, "image", time);
    }
}