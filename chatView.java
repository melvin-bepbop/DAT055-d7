import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;

public class chatView {
    private GUI gui;
    private chatController chatCtrl;

    public chatView(GUI gui){
        this.gui = gui;
    }

    public void setController(chatController chatCtrl) {
        this.chatCtrl = chatCtrl;
        setupListeners(); 
    }

    public void addMessageToDisplay(message msg, User user){
        boolean isMe = user.getUsername().equals(msg.getUsername());

        if(msg.getType().equals("text")){
            gui.addMessage(msg.getUsername(), msg.getContent(), msg.getTimeStamp().toString(), isMe);
            
        } else if (msg.getType().equals("image")) {
            // 1. Convert the giant text string back into a real picture
            ImageIcon imageIcon = ImageUtils.decodeBase64ToImage(msg.getContent());
            
            // 2. Tell the GUI to draw the picture! 
            // (NOTE: You will need to create this addImageMessage method in your GUI.java file!)
            if (imageIcon != null) {
                gui.addImageMessage(msg.getUsername(), imageIcon, msg.getTimeStamp().toString(), isMe);
            }
        }
    }

    private void setupListeners() {
        // --- TEXT SENDING LISTENER ---
        ActionListener sendListener = e -> {
            String text = gui.getInputField().getText();
            if (!text.trim().isEmpty()) {
                chatCtrl.sendMessageToDatabase(text, "text");
                gui.getInputField().setText(""); 
            }
        };

        gui.getSendButton().addActionListener(sendListener);
        gui.getInputField().addActionListener(sendListener);

        // --- IMAGE UPLOAD LISTENER ---
        ActionListener imageUploadListener = e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null); // Opens the file explorer pop-up

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                
                // Shred the image into a Base64 String using our new utility
                String base64Image = ImageUtils.encodeFileToBase64(selectedFile);
                
                if (base64Image != null) {
                    // Send it to the database just like a normal message, but flag it as "image"
                    chatCtrl.sendMessageToDatabase(base64Image, "image");
                }
            }
        };

        // Hook up the image button! 
        // (NOTE: Make sure you have an "Upload Image" button in your GUI.java and a getter for it!)
        gui.getImageButton().addActionListener(imageUploadListener); 
    }
}