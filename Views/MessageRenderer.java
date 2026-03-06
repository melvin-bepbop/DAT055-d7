package Views;

import Models.Message;

/**
 * Abstraction for drawing messages.
 *
 * Implementations are responsible for rendering a message in a specific way.
 */
public interface MessageRenderer {
    /**
     * Draws a message.
     *
     * @param msg message to draw
     * @param iChatDisplay display where the message should be shown
     * @param timestamp timestamp as text
     * @param isMe whether the message is from the active user
     */
    void draw(Message msg, IChatDisplay iChatDisplay, String timestamp, boolean isMe);
}