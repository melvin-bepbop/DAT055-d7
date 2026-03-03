package Controllers;
import java.util.LinkedList;

import Models.AccesibleChannels;
import Models.Channel;
import Models.ClientSession;
import Models.MessagesInChannel;
import Models.Message;
import Services.ChannelService;
import Services.MessageService;
import Views.chatView;

/**
 * The controller responsible for handling user interactions related to channels.
 * It acts as the mediator between the user interface (Views), the business logic 
 * and database operations (Services), and the local state of the application (ClientSession).
 */

public class channelController {
    private ClientSession session; 
    private ChannelService channelService; 
    private MessageService messageService; 
    private chatView chatView;

/**
     * Constructs a new channelController with the required dependencies.
     *
     * @param session        the current client session containing local state
     * @param channelService the service handling channel-related database logic
     * @param messageService the service handling message-related database logic
     * @param chatView       the GUI component where the chat is displayed
     */

    public channelController(ClientSession session, ChannelService channelService, MessageService messageService, chatView chatView) {
        this.session = session;
        this.channelService = channelService;
        this.chatView = chatView;
        this.messageService = messageService;
    }

/**
     * Creates a new global channel, updates the database, and immediately 
     * switches the user into this newly created channel.
     *
     * @param newChannelName the name of the channel to be created
     */

    public void createNewGlobalChannel(String newChannelName) {
        //Tell the Service to update the Database
        channelService.createNewChannel(newChannelName, session.getActiveUser());
        
        // Update the local Session so the GUI draws the new button
        Channel newChan = new Channel(newChannelName);
        session.getAccesibleChannels().getChannels().add(newChan);
        changeChannel(newChan);
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
        // Tell the Server/Database that the user moved
        channelService.updateUserActiveChannel(session.getActiveUser(), channel);
        
        // Fetch the chat history using YOUR actual method name!
        LinkedList<Message> history = messageService.getHistory(channel); 
        
        // Update our local RAM (Session)
        MessagesInChannel newFolder = new MessagesInChannel(channel);
        newFolder.addMessages(history);
        session.changeChannel(channel, newFolder);
        
        // Wipe the old chat off the screen and draw the new history
        chatView.displayMessageHistory(history, session.getActiveUser());
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
}