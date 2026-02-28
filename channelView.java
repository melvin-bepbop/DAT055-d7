import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class channelView {
    private channelController chnlctrl;
    private chatController chatCtrl;
    private GUI gui;

    public channelView(channelController controller, GUI gui) {
        this.chnlctrl = controller;
        this.gui = gui;
    }

    public void setChatController(chatController chatCtrl) {
        this.chatCtrl = chatCtrl;
        setupListeners();
        setupCreateChannelListener();
    }

    private void setupListeners() {
    for (int i = 0; i < gui.getChannelButtons().size(); i++) {
        JButton btn = gui.getChannelButtons().get(i);
        
        btn.addActionListener(e -> {
            String targetName = btn.getText();
            System.out.println("Switching to channel: " + targetName);

            AccesibleChannels accessible = chnlctrl.GetAllChannels();
            Channel targetChannel = null;

            for (Channel c : accessible.getChannels()) {
                if (c.getChannelName().equals(targetName)) {
                    targetChannel = c;
                    break;
                }
            }

            if (targetChannel != null) {
                gui.clearChat(); 
                chnlctrl.ChangeChannel(targetChannel); 
                
                if (chatCtrl != null) {
                    chatCtrl.loadChannelHistory();
                }
            } else {
                System.out.println("Error: Could not find channel object for " + targetName);
            }
        });
    }
}

private void setupCreateChannelListener() {
        gui.getCreateChannelButton().addActionListener(e -> {
            // 1. Pop up a box asking for the new name
            String newChannelName = JOptionPane.showInputDialog(gui.getFrame(), "Enter new channel name:");
            
            // 2. Make sure they didn't hit 'Cancel' or leave it blank
            if (newChannelName != null && !newChannelName.trim().isEmpty()) {
                
                // 3. Tell the Controller to save it to the Database
                chnlctrl.createNewGlobalChannel(newChannelName);
                
                // 4. Add the button to the screen
                JButton newBtn = gui.addSingleChannelButton(newChannelName);
                
                // 5. Attach the standard click listener to this brand new button!
                newBtn.addActionListener(event -> {
                    System.out.println("Switching to channel: " + newChannelName);
                    
                    // Find the newly created channel in our accessible list
                    Channel targetChannel = null;
                    for (Channel c : chnlctrl.GetAllChannels().getChannels()) {
                        if (c.getChannelName().equals(newChannelName)) {
                            targetChannel = c;
                            break;
                        }
                    }

                    if (targetChannel != null) {
                        gui.clearChat(); 
                        chnlctrl.ChangeChannel(targetChannel); 
                        if (chatCtrl != null) {
                            chatCtrl.loadChannelHistory();
                        }
                    }
                });
            }
        });
    }

}