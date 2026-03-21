package Views;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import Models.Channel;

/**
 * View class for channel-related interactions.
 * Acts as an Observer to the ISessionModel.
 */

public class channelView implements IChannelView, PropertyChangeListener {
    private IChatDisplay display; 
    private ViewListener listener; 

    public channelView(IChatDisplay display) {
        this.display = display;
        setupListeners();
        setupCreateChannelListener();
    }
    @Override
    public void setViewListener(ViewListener listener) {
        this.listener = listener;
    }

    /**
     * Attaches listeners to all existing channel buttons on startup.
     */
    private void setupListeners() {
        
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
            if (listener != null) {
                listener.changeChannel(targetName); 
            }
        });
    }

    /**
     * Sets up the listener for creating a new channel via the display.
     */
private void setupCreateChannelListener() {
        display.getCreateChannelButton().addActionListener(e -> {
            String newChannelName = JOptionPane.showInputDialog(display.getFrame(), "Enter new channel name:");
            
            if (newChannelName != null && !newChannelName.trim().isEmpty()) {
                if (listener != null) {
                    listener.createNewGlobalChannel(newChannelName.trim());
                }
            }
        });
    }

    /**
     * Observer 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("newChannelAdded".equals(evt.getPropertyName())) {
            Channel newChannel = (Channel) evt.getNewValue();
            AddChannelToSideBar(newChannel);
        }
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