package Views;

import Models.Message;

public class TextRenderer implements MessageRenderer {
    @Override
    public void draw(Message msg,  IChatDisplay iChatDisplay, String timestamp, boolean isMe) {
        iChatDisplay.addMessage(msg.getUsername(), msg.getContent(), timestamp, isMe);
    }
}