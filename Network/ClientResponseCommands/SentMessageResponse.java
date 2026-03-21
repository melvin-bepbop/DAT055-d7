package Network.ClientResponseCommands;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Map;


import Models.Channel;
import Models.ISessionModel;
import Models.Message;
import Models.MessageFactory;

/**
 * Client-side handler for SENDMSG broadcasts.
 *
 * Reconstructs the sent message from the protocol payload and and appends it directly to the ISessionModel.
 */
public class SentMessageResponse implements IClientResponseCommands {

    private ISessionModel session;
    
    private Map<String, MessageFactory> messageRegistry;


/**
     * Creates a new SentMessageResponse handler.
     *
     * @param session the application state model
     * @param registry registry mapping message types to factories
     */
    public SentMessageResponse(ISessionModel session, Map<String, MessageFactory> registry){
        this.session = session;
        this.messageRegistry = registry;
    }
    
    @Override
    /**
     * Executes the SENDMSG broadcast.
     *
     * @param string protocol fields: SENDMSG;channel;user;type;content;time
     */
    public void execute(String[] string){
        // Format expected: SENDMSG;CHANNEL;USER;TYPE;CONTENT;TIME
        String channel = string[1];
        String user = string[2];
        String type = string[3].toLowerCase(); 
        String content = string[4];
        LocalDateTime ldt = LocalDateTime.parse(string[5]);
        
        
        MessageFactory factory = messageRegistry.get(type);
        
        
        if (factory != null) {
            Message msg = factory.create(user, content, ldt);
            System.out.println("Received " + type + " message");
            Channel targetChannel = null;
            for (Channel c : session.getAccesibleChannels().getChannels()) {
                if (c.getChannelName().equals(channel)) {
                    targetChannel = c;
                    break;
                }
            }

        
            if (targetChannel != null) {
                
                LinkedList<Message> newMessages = new LinkedList<>();
                newMessages.add(msg);
                
                session.appendMessagesToChannel(targetChannel, newMessages);
            }
        } else {
            System.out.println("Warning: Unknown message type received: " + type);
        }
    }
}