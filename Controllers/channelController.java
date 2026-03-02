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

public class channelController {
    private ClientSession session; 
    private ChannelService channelService; 
    private MessageService messageService; 
    private chatView chatView;

    public channelController(ClientSession session, ChannelService channelService, MessageService messageService, chatView chatView) {
        this.session = session;
        this.channelService = channelService;
        this.chatView = chatView;
        this.messageService = messageService;
    }

    public void createNewGlobalChannel(String newChannelName) {
        //Tell the Service to update the Database
        channelService.createNewChannel(newChannelName, session.getActiveUser());
        
        // Update the local Session so the GUI draws the new button
        Channel newChan = new Channel(newChannelName);
        session.getAccesibleChannels().getChannels().add(newChan);
        changeChannel(newChan);
    }

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
    public AccesibleChannels GetAllChannels() {
        return session.getAccesibleChannels();
    }
}