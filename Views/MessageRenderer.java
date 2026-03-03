package Views;

import Models.Message;

public interface MessageRenderer {
    void draw(Message msg, IChatDisplay iChatDisplay, String timestamp, boolean isMe);
}