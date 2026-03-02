package Views;

import Models.Message;

public interface MessageRenderer {
    void draw(Message msg, GUI gui, String timestamp, boolean isMe);
}