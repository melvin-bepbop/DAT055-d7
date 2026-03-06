package Views;

import java.io.File;
import java.nio.channels.Channel;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import Controllers.chatController;
import Models.User;
import Models.Message;
import Utils.ImageUtils;

/**
 * View class that handles chat flow.
 *
 * Connects an IChatDisplay instance with a chatController and is responsible
 * for displaying messages and forwarding user interactions.
 */
public class chatView {
    private IChatDisplay display; 
    private chatController chatCtrl;
    private final Map<String, MessageRenderer> renderers = new HashMap<>();

    /**
     * Creates a new chatView.
     *
     * @param display display component that renders the chat content
     */
    public chatView(IChatDisplay display) { 
        this.display = display;
    }

    /**
     * Registers a renderer for a specific message type.
     *
     * @param type type string, for example "text" or "image"
     * @param renderer renderer to use for this type
     */
    public void registerRenderer(String type, MessageRenderer renderer) {
        renderers.put(type.toLowerCase(), renderer);
    }

    /**
     * Sets the controller and attaches listeners to the display.
     *
     * @param chatCtrl controller to use
     */
    public void setController(chatController chatCtrl) {
        this.chatCtrl = chatCtrl;
        setupListeners(); 
    }

    /**
     * Adds a single message to the view.
     *
     * @param msg message to display
     * @param user active user, used to decide whether the message is "mine"
     */
    public void addMessageToDisplay(Message msg, User user) {
        boolean isMe = user.getUsername().equals(msg.getUsername());
        String time = msg.getTimeStamp().toString();

        System.out.println("new message!");
        MessageRenderer renderer = renderers.get(msg.getType().toLowerCase());
        if (renderer != null) {
            renderer.draw(msg, display, time, isMe); 
        }
    }

    /**
     * Clears the entire chat display.
     */
    public void clearChatDisplay() {
        display.clearChat(); 
    }

    /**
     * Displays a list of history messages for the active user.
     *
     * @param history list of history messages
     * @param activeUser active user
     */
    public void displayMessageHistory(LinkedList<Message> history, User activeUser) {
        clearChatDisplay(); 
        for (Message msg : history) {
            addMessageToDisplay(msg, activeUser); 
        }
    }

    /**
     * Wires up send and image upload actions from the display to the controller.
     */
    private void setupListeners() {
        // Text logic
        display.onSendAction(() -> {
            String text = display.getInputText();
            if (!text.trim().isEmpty()) {
                chatCtrl.sendMessageToServer(text, "text");
                display.clearInputField(); 
            }
        });

        // Image logic
        display.onImageUploadAction(() -> {
            File selectedFile = display.promptUserForImageFile(); // Ask GUI for file
            
            if (selectedFile != null) {
                String base64Image = ImageUtils.encodeFileToBase64(selectedFile); // chatView handles data
                if (base64Image != null) {
                    chatCtrl.sendMessageToServer(base64Image, "image");
                }
            }
        });
    }
    /**
     * Notifies the controller that the channel has changed and reloads history.
     *
     * @param targetChannel channel to switch to
     */
    public void changingChannel(Channel targetChannel){
        chatCtrl.loadChannelHistory();
    }
}