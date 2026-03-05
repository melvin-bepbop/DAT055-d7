package Network.ClientResponseCommands;

import java.time.LocalDateTime;
import java.util.LinkedList;

import Controllers.channelController;
import Controllers.chatController;
import Models.AccesibleChannels;
import Models.ImageMessage;
import Models.Message;
import Models.Channel;

import Models.TextMessage;

public class GetMessagesFromResponse implements IClientResponseCommands {

    private channelController chanCont;
    private chatController chatcont;

    public GetMessagesFromResponse(channelController channelControll){
        this.chanCont = channelControll;
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
            for(int i = 2; i <string.length; i+=4){
                if(string[i+1].equals("text")){
                    msgs.add(new TextMessage(string[i], string[i+2], LocalDateTime.parse(string[i+3]))); 
                }
                else if(string[i+1].equals("image")){
                    msgs.add(new ImageMessage(string[i], string[i+2], LocalDateTime.parse(string[i+3])));
                }
                System.out.println("Added message: "+(1+i)/4);
                System.out.println(string[i]+";"+ string[i+2]+";"+ LocalDateTime.parse(string[i+3]).toString());


            }
             chatcont.updateChannelHistory(targetChannel, msgs);
        }
        else{
            System.out.println("Error couldnt find any messages");
        }
    }
}
