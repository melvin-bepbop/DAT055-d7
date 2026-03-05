package Network.ClientResponseCommands;

import Controllers.channelController;
import Controllers.chatController;
import Models.Channel;
import Models.AccesibleChannels;


public class ChangeChannelResponse implements IClientResponseCommands {

    private channelController chanCont;
    private chatController chatCont;

    public ChangeChannelResponse(channelController channelControll, chatController chatController){
        this.chanCont = channelControll;
        this.chatCont = chatController;
    }
    public void SetChannelController(channelController channelControll){
        this.chanCont = channelControll;
    }
    public void setChatCont(chatController chatCont) {
        this.chatCont = chatCont;
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
            chanCont.changeChannel(targetChannel);
            chatCont.ChangingChat(targetChannel);
        }

       else{
            System.out.println("Error couldnt change channel");
        }
    }
}
