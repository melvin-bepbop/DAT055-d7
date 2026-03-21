package Controllers;

import java.io.File;
import java.time.LocalDateTime;
import java.util.LinkedList;

import Models.Channel;
import Models.ISessionModel;
import Models.Message;
import Models.MessagesInChannel;
import Models.User;
import Views.IChatView;
import Network.Client;
import Network.NetworkCommands.GetMessagesFromCommand;
import Network.NetworkCommands.SendMessageCommand;
import Utils.ImageUtils;

/**
 * Handles chat interactions for the active channel.
 *
 * Responsible for sending messages, requesting message history from the server,
 * and updating the UI based on the local session cache.
 */
public class chatController implements IChatView.ViewListener {
    private ISessionModel session;
    private Client networkClient; 

    
    /**
     * Creates a chat controller bound to a session, view, and network client.
     *
     * @param session current client session (active user/channel and cached history)
     * @param chatview chat UI view to update
     * @param networkClient network client used to send protocol requests
     */
    public chatController(ISessionModel session, IChatView chatview, Client networkClient){
        this.session = session;
        this.networkClient = networkClient;

        chatview.setViewListener(this);
        
        if (chatview instanceof java.beans.PropertyChangeListener) {
            this.session.addPropertyChangeListener((java.beans.PropertyChangeListener) chatview);
        }
    }
    @Override
    public void onSendTextMessage(String rawText) {
        String cleanText = rawText.trim();
        if (!cleanText.isEmpty()) {
            sendMessageToServer(cleanText, "text");
        }
    }

    @Override
    public void onSendImageMessage(File selectedFile) {
        
        String base64Image = ImageUtils.encodeFileToBase64(selectedFile); 
        if (base64Image != null) {
            sendMessageToServer(base64Image, "image");
        }
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
     * Stores a full history list for a channel into the session cache and refreshes the UI.
     *
     * @param history messages belonging to the given channel
     * @param channel the channel the messages belong to
     */


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
            MessagesInChannel chanHistory = new MessagesInChannel(activeChannel);
            for (MessagesInChannel messagesInChannel : history) {
                if(messagesInChannel.getChannel().getChannelName().equals(activeChannel.getChannelName())){
                    chanHistory = messagesInChannel;
                    break;
                }
            }
            networkClient.sendMessage(GetMessagesFromCommand.identifier+";"+channelName+";"+chanHistory.getLastUpdated().toString());

        }
            
    }



}