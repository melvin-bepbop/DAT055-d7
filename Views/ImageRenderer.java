package Views;

import Models.Message;

public class ImageRenderer implements MessageRenderer {
    @Override
    public void draw(Message msg, IChatDisplay display, String timestamp, boolean isMe) {
        // ZERO GUI LOGIC HERE NOW!
        // msg.getContent() is the Base64 String. We just pass it straight through.
        display.addImageMessage(msg.getUsername(), msg.getContent(), timestamp, isMe);
    }
}