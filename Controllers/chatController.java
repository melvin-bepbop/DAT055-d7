package Controllers;
import java.time.LocalDateTime;
import java.util.LinkedList;

import Models.Channel;
import Models.ClientSession;
import Models.User;
import Models.Message;
import Models.MessagesInChannel;
import Views.chatView;
import Network.Client;
import Network.NetworkCommands.GetAllMessageCommand;
import Network.NetworkCommands.GetMessagesFromCommand;
import Network.NetworkCommands.SendMessageCommand;

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

        //SENDMSG;CHANNEL;USER;TYPE;CONTENT;TIME
        String payload = SendMessageCommand.identifier +";"+activeChannel.getChannelName() + ";"+ activeUser.getUsername()+";"+type+ ";" + content + ";"+ LocalDateTime.now().toString();
        

        networkClient.sendMessage(payload);

    }

    public void loadChannelHistory() {
        // Fetch the cached messages from our local Session
        LinkedList<Message> history = session.getHistoryForActiveChannel();
        User currentUser = session.getActiveUser();
        chatview.clearChatDisplay();
        // Draw them to the screen
        chatview.displayMessageHistory(history, currentUser);
    }
    public void addChannelHistory(LinkedList<Message> history, Channel channel) {
        // Fetch the cached messages from our local Session
        MessagesInChannel msgHis = new MessagesInChannel(channel);
        msgHis.addMessages(history);
        session.addChannelHistory(msgHis);
        loadChannelHistory();
    }
    public void addNewMessageIfInChannel(String channelName, Message msg){
        Channel activeChannel = session.getActiveChannel();
        if(activeChannel.getChannelName().equals(channelName)){
            System.out.println("Interested "+ activeChannel.getChannelName());
            LinkedList<MessagesInChannel> history = session.getMsgHistoryInChannels();
            MessagesInChannel hstr = new MessagesInChannel(activeChannel);
            for (MessagesInChannel messagesInChannel : history) {
                if(messagesInChannel.getChannel().getChannelName().equals(activeChannel.getChannelName())){
                    hstr = messagesInChannel;
                    break;
                }
            }
            networkClient.sendMessage(GetMessagesFromCommand.identifier+";"+channelName+";"+hstr.getLastUpdated().toString());

        }
            
    }
    public void ChangingChat(Channel channel){
        LinkedList<MessagesInChannel> history = session.getMsgHistoryInChannels();
        boolean isLoaded = false;
        MessagesInChannel hstr = new MessagesInChannel(channel);
        for (MessagesInChannel messagesInChannel : history) {
            if(messagesInChannel.getChannel().getChannelName().equals(channel.getChannelName())){
                isLoaded = true;
                hstr = messagesInChannel;
                break;
                
            }
            System.out.println(messagesInChannel.getChannel().getChannelName());
        }
        if (!isLoaded) {
            networkClient.sendMessage(GetAllMessageCommand.identifier+";"+channel.getChannelName());
        }
        else{
            networkClient.sendMessage(GetMessagesFromCommand.identifier+";"+channel.getChannelName()+";"+hstr.getLastUpdated().toString());
        }
    }
    public void updateChannelHistory(Channel channel, LinkedList<Message> msgs){
        for (MessagesInChannel message : session.getMsgHistoryInChannels()) {
            if(message.getChannel().getChannelName().equals(channel.getChannelName())){
                message.addMessages(msgs);
            }
        }
        loadChannelHistory();
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
