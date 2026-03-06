package Views;

import java.io.File;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Abstraction for the chat display layer.
 *
 * Groups methods for rendering messages, handling input, and exposing
 * channel-related controls without requiring controllers to know Swing.
 */
public interface IChatDisplay {
    /**
     * Adds a text message to the view.
     *
     * @param user sender username
     * @param text message text content
     * @param time timestamp as text
     * @param isMe whether the message is from the active user
     */
    void addMessage(String user, String text, String time, boolean isMe);
    
    /**
     * Adds an image message to the view.
     *
     * @param user sender username
     * @param base64Data Base64 representation of the image
     * @param time timestamp as text
     * @param isMe whether the message is from the active user
     */
    void addImageMessage(String user, String base64Data, String time, boolean isMe);
    
    /**
     * Clears all messages in the view.
     */
    void clearChat();

    /**
     * Returns the text in the input field.
     *
     * @return current input text
     */
    String getInputText();

    /**
     * Clears the input field.
     */
    void clearInputField();
    
    /**
     * Prompts the user to select an image file.
     *
     * @return selected file, or null if the user cancels
     */
    File promptUserForImageFile(); 

    /**
     * Registers an action that runs when the user sends text.
     *
     * @param action action to run
     */
    void onSendAction(Runnable action);

    /**
     * Registers an action that runs when the user uploads an image.
     *
     * @param action action to run
     */
    void onImageUploadAction(Runnable action);
    
    /**
     * Returns the create-channel button.
     *
     * @return button component
     */
    JButton getCreateChannelButton();

    /**
     * Returns the list of channel buttons.
     *
     * @return list of channel buttons
     */
    LinkedList<JButton> getChannelButtons();

    /**
     * Adds a new channel button.
     *
     * @param name channel name
     * @return the created button
     */
    JButton addSingleChannelButton(String name);

    /**
     * Returns the main window.
     *
     * @return JFrame instance
     */
    JFrame getFrame();

}