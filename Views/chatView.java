package Views;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;

import Controllers.chatController;
import Models.User;
import Models.Message;
import Utils.ImageUtils;

public class chatView {
    private GUI gui;
    private chatController chatCtrl;
    
    private final Map<String, MessageRenderer> renderers = new HashMap<>();

    public chatView(GUI gui) {
        this.gui = gui;
        
        renderers.put("text", new TextRenderer());
        renderers.put("image", new ImageRenderer());
    }

    public void setController(chatController chatCtrl) {
        this.chatCtrl = chatCtrl;
        setupListeners(); 
    }

    public void addMessageToDisplay(Message msg, User user) {
        boolean isMe = user.getUsername().equals(msg.getUsername());
        String time = msg.getTimeStamp().toString();

        MessageRenderer renderer = renderers.get(msg.getType().toLowerCase());
        
        if (renderer != null) {
            renderer.draw(msg, gui, time, isMe);
        }
    }

    public void clearChatDisplay() {
        gui.clearChat(); 
    }

    public void displayMessageHistory(LinkedList<Message> history, User activeUser) {
        clearChatDisplay(); 
        for (Message msg : history) {
            addMessageToDisplay(msg, activeUser); 
        }
    }

    private void setupListeners() {
        // Logic for Text Sending
        ActionListener sendListener = e -> {
            String text = gui.getInputField().getText();
            if (!text.trim().isEmpty()) {
                chatCtrl.sendMessageToDatabase(text, "text");
                gui.getInputField().setText(""); 
            }
        };

        gui.getSendButton().addActionListener(sendListener);
        gui.getInputField().addActionListener(sendListener);

        // Logic for Image Uploading
        ActionListener imageUploadListener = e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null); 
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String base64Image = ImageUtils.encodeFileToBase64(selectedFile);
                
                if (base64Image != null) {
                    chatCtrl.sendMessageToDatabase(base64Image, "image");
                }
            }
        };

        gui.getImageButton().addActionListener(imageUploadListener); 
    }
}