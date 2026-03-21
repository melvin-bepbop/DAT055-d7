package Views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import Models.User;
import Models.ISessionModel;
import Models.Message;

/**
 * View class that handles chat flow.
 *
 * Connects an IChatDisplay instance with a chatController and is responsible
 * for displaying messages and forwarding user interactions.
 */
public class chatView implements IChatView, PropertyChangeListener{
    private IChatDisplay display; 
    private ViewListener listener;
    private final Map<String, MessageRenderer> renderers = new HashMap<>();

    /**
     * Creates a new chatView.
     *
     * @param display display component that renders the chat content
     */
    public chatView(IChatDisplay display) { 
        this.display = display;
        setupUIActionListeners();
    }
    
    @Override
    public void setViewListener(ViewListener listener) {
        this.listener = listener;
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
     * Listens to the UI and forwards raw events to the Listener.
     */
    private void setupUIActionListeners() {
        display.onSendAction(() -> {
            if (listener != null) {
                listener.onSendTextMessage(display.getInputText());
                display.clearInputField();
            }
        });

        display.onImageUploadAction(() -> {
            File selectedFile = display.promptUserForImageFile();
            if (listener != null && selectedFile != null) {
                listener.onSendImageMessage(selectedFile);
            }
        });
    }
    @Override
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        ISessionModel session = (ISessionModel) evt.getSource();

        switch (evt.getPropertyName()) {
            case "chatHistoryUpdated":
                LinkedList<Message> newHistory = (LinkedList<Message>) evt.getNewValue();
                displayMessageHistory(newHistory, session.getActiveUser());
                break;
                
            case "activeChannelChanged":

                clearChatDisplay();

                LinkedList<Message> cachedHistory = session.getHistoryForActiveChannel();
                
                displayMessageHistory(cachedHistory, session.getActiveUser());
                break;
        }
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

        //System.out.println("new message!");
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
}
