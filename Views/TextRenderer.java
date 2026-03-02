package Views;

import Models.Message;

public class TextRenderer implements MessageRenderer {
    @Override
    public void draw(Message msg, GUI gui, String timestamp, boolean isMe) {
        gui.addMessage(msg.getUsername(), msg.getContent(), timestamp, isMe);
    }
}