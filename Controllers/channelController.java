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
 * The controller responsible for handling user interactions related to channels.
 * It acts as the mediator between the user interface (Views), the business logic 
 * and database operations (Services), and the local state of the application (ClientSession).
 */

public class channelController {
    private ClientSession session; 
    private Client networkClient;
    private chatView chatView;
    private channelView chanView;

/**
     * Constructs a new channelController with the required dependencies.
     *
     * @param session        the current client session containing local state
     * @param channelService the service handling channel-related database logic
     * @param messageService the service handling message-related database logic
     * @param chatView       the GUI component where the chat is displayed
     */

public channelController(ClientSession session, Client networkClient, chatView chatView) {
        this.session = session;
        this.networkClient = networkClient;
        this.chatView = chatView;
        
    }
    

    public channelController(){}

    public void setChanView(channelView chanView) {
        this.chanView = chanView;
    }
    public void setChatView(chatView chatView) {
        this.chatView = chatView;
    }
    public void setNetworkClient(Client networkClient) {
        this.networkClient = networkClient;
    }
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
     * @param channel the target {@link Channel} the user wants to enter
     */
    public void changeChannel(Channel channel) {
        /*
        String username = session.getActiveUser().getUsername();
        String payload = ChangeChannelCommand.identifier + ";" + username + ";" + channel.getChannelName();
        
        networkClient.sendMessage(payload);
        */
       session.changeChannel(channel);
    }
    public void RequestChangeChannel(Channel channel){
        networkClient.sendMessage(ChangeChannelCommand.identifier+";"+channel.getChannelName()+";"+session.getActiveUser().getUsername());
    }
    
    /**
     * Retrieves the list of all channels that the current user has access to.
     * This data is fetched from the local session state.
     *
     * @return an {@link AccesibleChannels} object containing the user's available channels
     */

    public AccesibleChannels GetAllChannels() {
        return session.getAccesibleChannels();
    }
    public void AddToAccesibleChannels(Channel channel){
        session.getAccesibleChannels().addChannel(channel);
        chanView.AddChannelToSideBar(channel);
    }
}