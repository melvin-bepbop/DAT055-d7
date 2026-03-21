package Controllers;


import Models.Channel;
import Models.ISessionModel;
import Views.IChannelView;
import Network.Client;
import Network.NetworkCommands.ChangeChannelCommand;
import Network.NetworkCommands.CreateNewChannelCommand;
/**
 * Handles channel-related actions.
 *
 * Manages the active channel in the client session and sends channel requests to the server
 * using the socket protocol. Receives raw data from view.
 */

public class channelController implements IChannelView.ViewListener {
    private ISessionModel session; 
    private Client networkClient;

/**
     * Constructs a channelController with the required dependencies.
     *
     * @param session the current client session containing local state
     * @param networkClient client used to send requests to the server
     * @param chatView the chat view associated with the UI
     */

public channelController(ISessionModel session, Client networkClient, IChannelView chanView) {
        this.session = session;
        this.networkClient = networkClient;
        
        chanView.setViewListener(this);
        
        if (chanView instanceof java.beans.PropertyChangeListener) {
            this.session.addPropertyChangeListener((java.beans.PropertyChangeListener) chanView);
        }
    }



/**
     * Creates a new global channel, updates the database, and immediately 
     * switches the user into this newly created channel.
     *
     * @param newChannelName the name of the channel to be created
     */
@Override
    public void createNewGlobalChannel(String newChannelName) {
        String payload = CreateNewChannelCommand.identifier + ";" + newChannelName;
        networkClient.sendMessage(payload);
    }
    /**
     * Transitions the user to a different channel.
     * This method notifies the server of the move, retrieves the message history 
     * for the new channel, updates the local session memory, and finally refreshes 
     * the user interface to display the new chat room.
     *
     * @param channel the target channel the user wants to enter
     */
    @Override
    public void changeChannel(String channelName) {
        System.out.println("Switching to channel: " + channelName);

        Channel targetChannel = null;

        
        for (Channel c : session.getAccesibleChannels().getChannels()) {
            if (c.getChannelName().equals(channelName)) {
                targetChannel = c;
                break;
            }
        }

        if (targetChannel != null) {
            String activeUsername = session.getActiveUser().getUsername();
            String payload = ChangeChannelCommand.identifier + ";" + targetChannel.getChannelName() + ";" + activeUsername;
            networkClient.sendMessage(payload);
        } else {
            System.out.println("Error: Could not find channel object for " + channelName);
        }
    }

}