package Controllers;


import Models.AccesibleChannels;
import Models.Channel;
import Models.ClientSession;
import Views.channelView;
import Views.chatView;
import Network.Client;
import Network.NetworkCommands.ChangeChannelCommand;
import Network.NetworkCommands.CreateNewChannelCommand;
/**
 * Handles channel-related actions.
 *
 * Manages the active channel in the client session and sends channel requests to the server
 * using the socket protocol. It also updates the UI through the bound channel view.
 */

public class channelController {
    private ClientSession session; 
    private Client networkClient;
    private chatView chatView;
    private channelView chanView;

/**
     * Constructs a channelController with the required dependencies.
     *
     * @param session the current client session containing local state
     * @param networkClient client used to send requests to the server
     * @param chatView the chat view associated with the UI
     */

public channelController(ClientSession session, Client networkClient, chatView chatView) {
        this.session = session;
        this.networkClient = networkClient;
        this.chatView = chatView;
        
    }
    

    public channelController(){}

    /**
     * Binds the channel sidebar view used for UI updates (e.g. adding channels to the list).
     *
     * @param chanView channel view to bind
     */
    public void setChanView(channelView chanView) {
        this.chanView = chanView;
    }

    /**
     * Binds the chat view reference (used by other parts of the UI layer).
     *
     * @param chatView chat view to bind
     */
    public void setChatView(chatView chatView) {
        this.chatView = chatView;
    }

    /**
     * Sets the network client used for sending requests.
     *
     * @param networkClient client to use
     */
    public void setNetworkClient(Client networkClient) {
        this.networkClient = networkClient;
    }

    /**
     * Sets the session used for local channel state.
     *
     * @param session session to use
     */
    public void setSession(ClientSession session) {
        this.session = session;
    }

/**
     * Creates a new global channel, updates the database, and immediately 
     * switches the user into this newly created channel.
     *
     * @param newChannelName the name of the channel to be created
     */
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
    public void changeChannel(Channel channel) {
        /*
        String username = session.getActiveUser().getUsername();
        String payload = ChangeChannelCommand.identifier + ";" + username + ";" + channel.getChannelName();
        
        networkClient.sendMessage(payload);
        */
       session.changeChannel(channel);
    }

    /**
     * Requests that the server change the active channel for the current user.
     *
     * @param channel target channel
     */
    public void RequestChangeChannel(Channel channel){
        networkClient.sendMessage(ChangeChannelCommand.identifier+";"+channel.getChannelName()+";"+session.getActiveUser().getUsername());
    }
    
    /**
     * Retrieves the list of all channels that the current user has access to.
     * This data is fetched from the local session state.
     *
     * @return an AccesibleChannels object containing the user's available channels
     */

    public AccesibleChannels GetAllChannels() {
        return session.getAccesibleChannels();
    }

    /**
     * Adds a channel to the locally accessible list and updates the sidebar UI.
     *
     * @param channel channel to add
     */
    public void AddToAccesibleChannels(Channel channel){
        session.getAccesibleChannels().addChannel(channel);
        chanView.AddChannelToSideBar(channel);
    }
}