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

public class GetAllMessageResponse implements IClientResponseCommands {
    private Map<String, MessageFactory> messageRegistry;
    private channelController chanCont;
    private chatController chatcont;

    public GetAllMessageResponse(channelController channelControll, Map<String, MessageFactory> registry){
        this.chanCont = channelControll;
        this.messageRegistry = registry;
    }
    
    public void SetChannelController(channelController channelControll){
        this.chanCont = channelControll;
    }
    
    public void setChaCont(chatController chatCont) {
        this.chatcont = chatCont;
    }
    
    @Override
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