package Network.ClientResponseCommands;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Map;

import Controllers.channelController;
import Controllers.chatController;
import Models.AccesibleChannels;
import Models.Message;
import Models.Channel;
import Models.MessageFactory; // Reuse the awesome interface you already made!

public class GetMessagesFromResponse implements IClientResponseCommands {

    private channelController chanCont;
    private chatController chatcont;
    
    // THE SECRET WEAPON: A dictionary of message blueprints
    private Map<String, MessageFactory> messageRegistry;

    // Inject the registry through the constructor
    public GetMessagesFromResponse(channelController channelControll, Map<String, MessageFactory> registry){
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
            System.out.println("Getting new messages from "+ targetChannel);

            LinkedList<Message> msgs = new LinkedList<>();
            
            for(int i = 2; i < string.length; i+=4){
                // 1. Extract the raw string data to make it easier to read
                String user = string[i];
                String type = string[i+1].toLowerCase();
                String content = string[i+2];
                LocalDateTime time = LocalDateTime.parse(string[i+3]);

                // 2. THE SOLID FIX: Look up the blueprint in the dictionary!
                MessageFactory factory = messageRegistry.get(type);
                
                if (factory != null) {
                    // Tell the factory to build it, we don't care if it's text or image!
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