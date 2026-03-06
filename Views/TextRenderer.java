package Views;

import Models.Message;

/**
 * Renderer for text messages.
 *
 * Draws a message by calling IChatDisplay with the text content.
 */
public class TextRenderer implements MessageRenderer {
    /**
     * Draws a text message.
     *
     * @param msg message to draw
     * @param iChatDisplay display that receives the text
     * @param timestamp timestamp as text
     * @param isMe whether the message is from the active user
     */
    @Override
    public void draw(Message msg,  IChatDisplay iChatDisplay, String timestamp, boolean isMe) {
        iChatDisplay.addMessage(msg.getUsername(), msg.getContent(), timestamp, isMe);
    }
}