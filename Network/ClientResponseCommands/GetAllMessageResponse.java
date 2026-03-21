package Network.ClientResponseCommands;

import Models.AccesibleChannels;
import Models.Channel;
import Models.Message;
import Models.MessageFactory;
import Models.MessagesInChannel;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Map;
import Models.ISessionModel;

/**
 * Client-side handler for GETALLMSG responses.
 *
 * Builds Message objects from the flat protocol response for a channel and
 * passes them to the chat controller as full history.
 */
public class GetAllMessageResponse implements IClientResponseCommands {
    private Map<String, MessageFactory> messageRegistry;
    private ISessionModel session;

/**
     * Creates a new GetAllMessageResponse handler.
     *
     * @param session the application state model
     * @param registry registry mapping message types to factories
     */
    public GetAllMessageResponse(ISessionModel session, Map<String, MessageFactory> registry){
        this.session = session;
        this.messageRegistry = registry;
    }
    
    
    @Override
    /**
     * Executes the GETALLMSG response.
     *
     * @param string protocol fields: GETALLMSG;channel;user;type;content;time;...
     */
    public void execute(String[] string){
        if (!string[1].equals("FAIL")) {
            AccesibleChannels accessible = session.getAccesibleChannels();
            Channel targetChannel = null;

            for (Channel c : accessible.getChannels()) {
                if (c.getChannelName().equals(string[1])) {
                    targetChannel = c;
                    break;
                }
            }
            
            System.out.println("Getting message from "+ targetChannel);

            LinkedList<Message> msgs = new LinkedList<>();
            
            for(int i = 2; i < string.length; i+=4){
                
                String user = string[i];
                String type = string[i+1].toLowerCase();
                String content = string[i+2];
                LocalDateTime time = LocalDateTime.parse(string[i+3]);

                
                MessageFactory factory = messageRegistry.get(type);

                
                if (factory != null) {
                    msgs.add(factory.create(user, content, time));
                    System.out.println("Added " + type /*+ " message: " + (i/4)*/);
                } else {
                    System.out.println("Warning: Unknown message type received: " + type);
                }
            }
            
            MessagesInChannel newMsgHistory = new MessagesInChannel(targetChannel);
            newMsgHistory.addMessages(msgs);
            session.addChannelHistory(newMsgHistory);
        }
        else{
            System.out.println("Error couldnt find any messages");
        }
    }
}