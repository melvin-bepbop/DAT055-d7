package Views;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import Controllers.channelController;
import Models.AccesibleChannels;
import Models.Channel;

/**
 * View class for channel-related interactions.
 *
 * Manages the channel list, switching the active channel, and creating new channels
 * by forwarding events to a channelController.
 */
public class channelView {
    private channelController chnlctrl;
    private IChatDisplay display; 

    /**
     * Creates a new channelView.
     *
     * @param controller controller that handles channel events
     * @param display display component that contains the channel buttons
     */
    public channelView(channelController controller, IChatDisplay display) {
        this.chnlctrl = controller;
        this.display = display;

        setupListeners();
        setupCreateChannelListener();
    }

    /**
     * Attaches listeners to all existing channel buttons on startup.
     */
    private void setupListeners() {
        // Attach the listener to all existing channels on startup
        for (int i = 0; i < display.getChannelButtons().size(); i++) {
            JButton btn = display.getChannelButtons().get(i);
            attachChannelListener(btn);
        }
    }

    /**
     * Attaches the standard click listener to a channel button.
     *
     * @param btn button representing a channel
     */
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

    /**
     * Sets up the listener for creating a new channel via the display.
     */
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

    /**
     * Adds a channel to the sidebar and wires up its click listener.
     *
     * @param channel channel to add to the sidebar
     */
    public void AddChannelToSideBar(Channel channel){
        JButton newBtn = display.addSingleChannelButton(channel.getChannelName());
        attachChannelListener(newBtn);
    }
}