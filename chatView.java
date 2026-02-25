import java.awt.event.ActionListener;

public class chatView {
    private GUI gui;
    private chatController chatCtrl; // 1. Add this slot

    public chatView(GUI gui){
        this.gui = gui;
    }

    // 2. Add this Setter method so App.java can plug the controller in
    public void setController(chatController chatCtrl) {
        this.chatCtrl = chatCtrl;
        setupListeners(); // Turn on the buttons only AFTER the controller is plugged in
    }

    public void addMessageToDisplay(message msg, User user){
        // Reminder: In Java, use .equals() for strings, not ==
        if(msg.getType().equals("text")){
            boolean isMe = user.getUsername().equals(msg.getUsername());
            gui.addMessage(msg.getUsername(), msg.getContent(), msg.getTimeStamp().toString(), isMe);
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
    }
}