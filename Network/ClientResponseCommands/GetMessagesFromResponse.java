package Network.ClientResponseCommands;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Map;


import Models.AccesibleChannels;
import Models.Message;
import Models.Channel;
import Models.MessageFactory;
import Models.ISessionModel;

/**
 * Client-side handler for GETMSGSFROM responses.
 *
 * Builds Message objects for new messages in a channel and asks the chat
 * controller to update the cached history.
 */
public class GetMessagesFromResponse implements IClientResponseCommands {

    private ISessionModel session;
    
    private Map<String, MessageFactory> messageRegistry;

/**
     * Creates a new GetMessagesFromResponse handler.
     *
     * @param session the application state model
     * @param registry registry mapping message types to factories
     */
    public GetMessagesFromResponse(ISessionModel session, Map<String, MessageFactory> registry){
        this.session = session;
        this.messageRegistry = registry;
    }

    @Override
    /**
     * Executes the GETMSGSFROM response.
     *
     * @param string protocol fields: GETMSGSFROM;channel;user;type;content;time;...
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
            if (targetChannel == null) {
                System.out.println("Error: Target channel " + string[1] + " not found in accessible channels.");
                return;
            }
            }
            System.out.println("Getting new messages from "+ targetChannel);

            LinkedList<Message> msgs = new LinkedList<>();
            
            for(int i = 2; i < string.length; i+=4){
                String user = string[i];
                String type = string[i+1].toLowerCase();
                String content = string[i+2];
                LocalDateTime time = LocalDateTime.parse(string[i+3]);

                MessageFactory factory = messageRegistry.get(type);
                
                if (factory != null) {
                    msgs.add(factory.create(user, content, time));
                    System.out.println("Added " + type /*+ " message: " + ((i/4) + 1)*/);
                } else {
                    System.out.println("Warning: Unknown message type received: " + type);
                }
            }
             session.appendMessagesToChannel(targetChannel, msgs);
        }
        else{
            System.out.println("Error couldnt find any messages");
        }
    }
}