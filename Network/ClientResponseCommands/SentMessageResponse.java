package Network.ClientResponseCommands;

import java.time.LocalDateTime;
import java.util.Map;

import Controllers.chatController;
import Models.Message;
import Models.MessageFactory;

/**
 * Client-side handler for SENDMSG broadcasts.
 *
 * Reconstructs the sent message from the protocol payload and forwards it
 * to the chat controller if it belongs to the active channel.
 */
public class SentMessageResponse implements IClientResponseCommands {

    private chatController chatCont;
    
    private Map<String, MessageFactory> messageRegistry;


    /**
     * Creates a new SentMessageResponse handler.
     *
     * @param registry registry mapping message types to factories
     */
    public SentMessageResponse(Map<String, MessageFactory> registry){
        this.messageRegistry = registry;
    }

    /**
     * Updates the chat controller reference after construction.
     *
     * @param chatCont chat controller to use
     */
    public void setChatCont(chatController chatCont) {
        this.chatCont = chatCont;
    }
    
    @Override
    /**
     * Executes the SENDMSG broadcast.
     *
     * @param string protocol fields: SENDMSG;channel;user;type;content;time
     */
    public void execute(String[] string){
        // Format expected: SENDMSG;CHANNEL;USER;TYPE;CONTENT;TIME
        String Channel = string[1];
        String user = string[2];
        String type = string[3].toLowerCase(); // Normalize to lowercase just in case
        String content = string[4];
        LocalDateTime ldt = LocalDateTime.parse(string[5]);
        
        // 1. Look up the blueprint in the dictionary
        MessageFactory factory = messageRegistry.get(type);
        
        // 2. Build and route the message dynamically!
        if (factory != null) {
            Message msg = factory.create(user, content, ldt);
            System.out.println("Received " + type + " message");
            chatCont.addNewMessageIfInChannel(Channel, msg);
        } else {
            System.out.println("Warning: Unknown message type received: " + type);
        }
    }
}