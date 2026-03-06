package Network.ClientResponseCommands;

import Controllers.channelController;
import Controllers.chatController;
import Models.AccesibleChannels;
import Models.Channel;
import Models.Message;
import Models.MessageFactory;
import Models.MessagesInChannel;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Map;

/**
 * Client-side handler for GETALLMSG responses.
 *
 * Builds Message objects from the flat protocol response for a channel and
 * passes them to the chat controller as full history.
 */
public class GetAllMessageResponse implements IClientResponseCommands {
    private Map<String, MessageFactory> messageRegistry;
    private channelController chanCont;
    private chatController chatcont;

    /**
     * Creates a new GetAllMessageResponse handler.
     *
     * @param channelControll channel controller used to resolve channels
     * @param registry registry mapping message types to factories
     */
    public GetAllMessageResponse(channelController channelControll, Map<String, MessageFactory> registry){
        this.chanCont = channelControll;
        this.messageRegistry = registry;
    }
    
    /**
     * Updates the channel controller reference after construction.
     *
     * @param channelControll channel controller to use
     */
    public void SetChannelController(channelController channelControll){
        this.chanCont = channelControll;
    }
    
    /**
     * Updates the chat controller reference after construction.
     *
     * @param chatCont chat controller to use
     */
    public void setChaCont(chatController chatCont) {
        this.chatcont = chatCont;
    }
    
    @Override
    /**
     * Executes the GETALLMSG response.
     *
     * @param string protocol fields: GETALLMSG;channel;user;type;content;time;...
     */
    public void execute(String[] string){
        if (!string[1].equals("FAIL")) {
            AccesibleChannels accessible = chanCont.GetAllChannels();
            Channel targetChannel = null;

            for (Channel c : accessible.getChannels()) {
                if (c.getChannelName().equals(string[1])) {
                    targetChannel = c;
                    break;
                }
            }
            
            MessagesInChannel messagesInChannel = new MessagesInChannel(targetChannel);
            System.out.println("Getting message from "+ targetChannel);

            LinkedList<Message> msgs = new LinkedList<>();
            
            for(int i = 2; i < string.length; i+=4){
                // 1. Extract variables for clean reading
                String user = string[i];
                String type = string[i+1].toLowerCase();
                String content = string[i+2];
                LocalDateTime time = LocalDateTime.parse(string[i+3]);

                // 2. Lookup the factory blueprint in the registry
                MessageFactory factory = messageRegistry.get(type);

                // 3. Build the object dynamically!
                if (factory != null) {
                    msgs.add(factory.create(user, content, time));
                    System.out.println("Added " + type /*+ " message: " + (i/4)*/);
                } else {
                    System.out.println("Warning: Unknown message type received: " + type);
                }
            }
            
            messagesInChannel.addMessages(msgs);
            chatcont.addChannelHistory(msgs, targetChannel); 
        }
        else{
            System.out.println("Error couldnt find any messages");
        }
    }
}