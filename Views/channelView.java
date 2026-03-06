package Views;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import Controllers.channelController;
import Models.AccesibleChannels;
import Models.Channel;

public class channelView {
    private channelController chnlctrl;
    private IChatDisplay display; 

    public channelView(channelController controller, IChatDisplay display) {
        this.chnlctrl = controller;
        this.display = display;

        setupListeners();
        setupCreateChannelListener();
    }

    private void setupListeners() {
        // Attach the listener to all existing channels on startup
        for (int i = 0; i < display.getChannelButtons().size(); i++) {
            JButton btn = display.getChannelButtons().get(i);
            attachChannelListener(btn);
        }
    }

    private void attachChannelListener(JButton btn) {
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

                chnlctrl.RequestChangeChannel(targetChannel); 
            } else {
                System.out.println("Error: Could not find channel object for " + targetName);
            }
        });
    }

    private void setupCreateChannelListener() {
        display.getCreateChannelButton().addActionListener(e -> {
            // 1. Pop up a box asking for the new name
            String newChannelName = JOptionPane.showInputDialog(display.getFrame(), "Enter new channel name:");
            
            // 2. Make sure they didn't hit 'Cancel' or leave it blank
            if (newChannelName != null && !newChannelName.trim().isEmpty()) {
                
                // 3. Tell the Controller to save it to the Database AND update the Session
                chnlctrl.createNewGlobalChannel(newChannelName);
                
                // 4. Add the button to the screen
                
                // 5. Attach the standard click listener to this brand new button using our helper!
            }
        });
    }
    public void AddChannelToSideBar(Channel channel){
        JButton newBtn = display.addSingleChannelButton(channel.getChannelName());
        attachChannelListener(newBtn);
    }
}