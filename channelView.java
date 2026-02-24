import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

public class channelView {
    private channelController chnlctrl;
    private chatController chatCtrl; // We need this to refresh the chat!
    private GUI gui;

    // We take the Channel Controller and GUI in the constructor
    public channelView(channelController controller, GUI gui) {
        this.chnlctrl = controller;
        this.gui = gui;
    }

    // THE SETTER TRICK: Inject the chatController later
    public void setChatController(chatController chatCtrl) {
        this.chatCtrl = chatCtrl;
        setupListeners(); // Now that we have everything, turn on the buttons!
    }

    private void setupListeners() {
    // Loop through every channel button in the GUI
    for (int i = 0; i < gui.getChannelButtons().size(); i++) {
        JButton btn = gui.getChannelButtons().get(i);
        
        btn.addActionListener(e -> {
            String targetName = btn.getText();
            System.out.println("Switching to channel: " + targetName);

            // 1. Get the list of existing channels from the database/model
            AccesibleChannels accessible = chnlctrl.GetAllChannels();
            Channel targetChannel = null;

            // 2. Find the exact object that matches the button's name
            for (Channel c : accessible.getChannels()) {
                if (c.getChannelName().equals(targetName)) {
                    targetChannel = c;
                    break;
                }
            }

            // 3. If we found it, switch to it!
            if (targetChannel != null) {
                gui.clearChat(); // Clear the screen
                chnlctrl.ChangeChannel(targetChannel); // Pass the EXISTING object
                
                if (chatCtrl != null) {
                    chatCtrl.updateMessagesInChannel(); // Load the new messages
                }
            } else {
                System.out.println("Error: Could not find channel object for " + targetName);
            }
        });
    }
}
}