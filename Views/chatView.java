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

public class chatView {
    private IChatDisplay display; 
    private chatController chatCtrl;
    private final Map<String, MessageRenderer> renderers = new HashMap<>();

    public chatView(IChatDisplay display) { 
        this.display = display;
    }

    public void registerRenderer(String type, MessageRenderer renderer) {
        renderers.put(type.toLowerCase(), renderer);
    }

    public void setController(chatController chatCtrl) {
        this.chatCtrl = chatCtrl;
        setupListeners(); 
    }

    public void addMessageToDisplay(Message msg, User user) {
        boolean isMe = user.getUsername().equals(msg.getUsername());
        String time = msg.getTimeStamp().toString();

        System.out.println("new message!");
        MessageRenderer renderer = renderers.get(msg.getType().toLowerCase());
        if (renderer != null) {
            renderer.draw(msg, display, time, isMe); 
        }
    }

    public void clearChatDisplay() {
        display.clearChat(); 
    }

    public void displayMessageHistory(LinkedList<Message> history, User activeUser) {
        clearChatDisplay(); 
        for (Message msg : history) {
            addMessageToDisplay(msg, activeUser); 
        }
    }

    private void setupListeners() {
        // --- UPDATED TO MATCH YOUR EXACT INTERFACE NAMES ---
        
        // Text Logic
        display.onSendAction(() -> {
            String text = display.getInputText();
            if (!text.trim().isEmpty()) {
                chatCtrl.sendMessageToServer(text, "text");
                display.clearInputField(); 
            }
        });

        // Image Logic
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
    public void changingChannel(Channel targetChannel){
        chatCtrl.loadChannelHistory();
    }
}