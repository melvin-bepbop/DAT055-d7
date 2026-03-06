package Network.ClientResponseCommands;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Map;

import Controllers.channelController;
import Controllers.chatController;
import Models.AccesibleChannels;
import Models.Message;
import Models.Channel;
import Models.MessageFactory;

/**
 * Client-side handler for GETMSGSFROM responses.
 *
 * Builds Message objects for new messages in a channel and asks the chat
 * controller to update the cached history.
 */
public class GetMessagesFromResponse implements IClientResponseCommands {

    private channelController chanCont;
    private chatController chatcont;
    
    private Map<String, MessageFactory> messageRegistry;

    /**
     * Creates a new GetMessagesFromResponse handler.
     *
     * @param channelControll channel controller used to resolve channels
     * @param registry registry mapping message types to factories
     */
    public GetMessagesFromResponse(channelController channelControll, Map<String, MessageFactory> registry){
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
     * Executes the GETMSGSFROM response.
     *
     * @param string protocol fields: GETMSGSFROM;channel;user;type;content;time;...
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
             chatcont.updateChannelHistory(targetChannel, msgs);
        }
        else{
            System.out.println("Error couldnt find any messages");
        }
    }
}