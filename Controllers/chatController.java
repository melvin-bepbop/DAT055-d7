package Controllers;
import java.util.LinkedList;

import Models.Channel;
import Models.ClientSession;
import Models.User;
import Models.Message;
import Views.chatView;
import Network.Client;

public class chatController {
    private chatView chatview;
    private ClientSession session; 
    private Client networkClient; 

    
public chatController(ClientSession session, chatView chatview, Client networkClient){
        this.session = session;
        this.chatview = chatview;
        this.networkClient = networkClient;
    }

    /*public void sendMessageToDatabase(String Content, String type){
        //Tell the Server (Service) to save it to Postgres
        messageService.sendMessage(session.getActiveUser(), session.getActiveChannel(), Content, type);
        
        //Immediately trigger the check to pull it back and display it!
        checkForNewMessages();
    }*/
   // Renamed because we are no longer talking to the Database directly
    public void sendMessageToServer(String content, String type){
        User activeUser = session.getActiveUser();
        Channel activeChannel = session.getActiveChannel();

        // 1. Format the request into a String Payload
        // Example text:  "MSG_SEND|text|Alice|General|Hello everyone!"
        // Example image: "MSG_SEND|image|Alice|General|Base64String..."
        String payload = "MSG_SEND|" + type + "|" + activeUser.getUsername() + "|" + 
                         activeChannel.getChannelName() + "|" + content;
        
        // 2. Push it down the network pipe!
        networkClient.sendMessage(payload);
        
        // NO MORE POLLING! 
        // We do not check for new messages here. We just wait for the Server
        // to broadcast this message back to our listenForMessage() thread.
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

    /*public void checkForNewMessages() {
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
    }*/
}