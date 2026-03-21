package Views;

import Models.Message;

/**
 * Renderer for image messages.
 *
 * Forwards Base64-encoded image data to IChatDisplay for display.
 */
public class ImageRenderer implements MessageRenderer {
    /**
     * Draws an image message.
     *
     * @param msg message to draw
     * @param display display where the image should be shown
     * @param timestamp timestamp as text
     * @param isMe whether the message is from the active user
     */
    @Override
    public void draw(Message msg, IChatDisplay display, String timestamp, boolean isMe) {
        
        display.addImageMessage(msg.getUsername(), msg.getContent(), timestamp, isMe);
    }
}