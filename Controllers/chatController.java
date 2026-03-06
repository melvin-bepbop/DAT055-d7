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

/**
 * Handles chat interactions for the active channel.
 *
 * Responsible for sending messages, requesting message history from the server,
 * and updating the UI based on the local session cache.
 */
public class chatController {
    private chatView chatview;
    private ClientSession session; 
    private Client networkClient; 

    
    /**
     * Creates a chat controller bound to a session, view, and network client.
     *
     * @param session current client session (active user/channel and cached history)
     * @param chatview chat UI view to update
     * @param networkClient network client used to send protocol requests
     */
    public chatController(ClientSession session, chatView chatview, Client networkClient){
        this.session = session;
        this.chatview = chatview;
        this.networkClient = networkClient;
    }

    /**
     * Sends a message to the server for the currently active channel.
     *
     * The payload format is SENDMSG;CHANNEL;USER;TYPE;CONTENT;TIME.
     *
     * @param content message content (protocol field)
     * @param type message type identifier, for example "text" or "image"
     */
    public void sendMessageToServer(String content, String type){
        User activeUser = session.getActiveUser();
        Channel activeChannel = session.getActiveChannel();

        //SENDMSG;CHANNEL;USER;TYPE;CONTENT;TIME
        String payload = SendMessageCommand.identifier +";"+activeChannel.getChannelName() + ";"+ activeUser.getUsername()+";"+type+ ";" + content + ";"+ LocalDateTime.now().toString();
        

        networkClient.sendMessage(payload);

    }

    /**
     * Renders the cached message history for the currently active channel.
     */
    public void loadChannelHistory() {
        // Fetch the cached messages from our local Session
        LinkedList<Message> history = session.getHistoryForActiveChannel();
        User currentUser = session.getActiveUser();
        chatview.clearChatDisplay();
        // Draw them to the screen
        chatview.displayMessageHistory(history, currentUser);
    }

    /**
     * Stores a full history list for a channel into the session cache and refreshes the UI.
     *
     * @param history messages belonging to the given channel
     * @param channel the channel the messages belong to
     */
    public void addChannelHistory(LinkedList<Message> history, Channel channel) {
        // Fetch the cached messages from our local Session
        MessagesInChannel msgHis = new MessagesInChannel(channel);
        msgHis.addMessages(history);
        session.addChannelHistory(msgHis);
        loadChannelHistory();
    }

    /**
     * If the given message belongs to the currently active channel, requests any messages newer
     * than the last cached timestamp from the server.
     *
     * @param channelName channel name associated with the incoming message
     * @param msg the incoming message (used to decide whether to fetch updates)
     */
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

    /**
     * Switches chat context to the given channel by requesting history if needed and/or requesting
     * messages newer than the cached timestamp.
     *
     * @param channel the channel to switch to
     */
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

    /**
     * Appends newly received messages to the cached history for a channel and refreshes the UI.
     *
     * @param channel channel to update
     * @param msgs messages to append
     */
    public void updateChannelHistory(Channel channel, LinkedList<Message> msgs){
        for (MessagesInChannel message : session.getMsgHistoryInChannels()) {
            if(message.getChannel().getChannelName().equals(channel.getChannelName())){
                message.addMessages(msgs);
            }
        }
        loadChannelHistory();
    }
}