package Controllers;
import java.time.LocalDateTime;
import java.util.LinkedList;

import Models.Channel;
import Models.ClientSession;
import Models.MessagesInChannel;
import Models.User;
import Models.Message;
import Services.MessageService;
import Views.chatView;

public class chatController {
    private chatView chatview;
    private ClientSession session; 
    private MessageService messageService; 

    
    public chatController(ClientSession session, chatView chatview, MessageService messageService){
        this.session = session;
        this.chatview = chatview;
        this.messageService = messageService;
    }

    public void sendMessageToDatabase(String Content, String type){
        //Tell the Server (Service) to save it to Postgres
        messageService.sendMessage(session.getActiveUser(), session.getActiveChannel(), Content, type);
        
        //Immediately trigger the check to pull it back and display it!
        checkForNewMessages();
    }

    public void loadChannelHistory() {
        // Fetch the cached messages from our local Session
        LinkedList<Message> history = session.getHistoryForActiveChannel();
        User currentUser = session.getActiveUser();
        
        // Draw them to the screen
        for (Message msg : history) {
            chatview.addMessageToDisplay(msg, currentUser);
        }
    }

    public void checkForNewMessages() {
        Channel activeChan = session.getActiveChannel();
        
        // Find the folder in our Session to see when we last checked
        MessagesInChannel currentFolder = null;
        for (MessagesInChannel folder : session.getMsgHistoryInChannels()) {
            if (folder.getChannel().getChannelName().equals(activeChan.getChannelName())) {
                currentFolder = folder;
                break;
            }
        }

        if (currentFolder != null) {
        // Ask the Server (MessageService) for new messages since our last timestamp
            LocalDateTime lastCheck = currentFolder.getLastUpdated();
            LinkedList<Message> freshMessages = messageService.getNewMessages(activeChan, lastCheck);

         // Put them in the folder and tell the GUI to draw them
            if (!freshMessages.isEmpty()) {
                currentFolder.addMessages(freshMessages); 
                
                User currentUser = session.getActiveUser();
                for (Message msg : freshMessages) {
                    chatview.addMessageToDisplay(msg, currentUser);
                }
            }
        }
    }
}