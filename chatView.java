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
            ImageIcon imageIcon = ImageUtils.decodeBase64ToImage(msg.getContent());
            
            if (imageIcon != null) {
                gui.addImageMessage(msg.getUsername(), imageIcon, msg.getTimeStamp().toString(), isMe);
            }
        }
    }

    private void setupListeners() {
        ActionListener sendListener = e -> {
            String text = gui.getInputField().getText();
            if (!text.trim().isEmpty()) {
                chatCtrl.sendMessageToDatabase(text, "text");
                gui.getInputField().setText(""); 
            }
        };

        gui.getSendButton().addActionListener(sendListener);
        gui.getInputField().addActionListener(sendListener);

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