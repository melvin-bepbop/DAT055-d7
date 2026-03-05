package Network.ClientResponseCommands;


import Controllers.channelController;
import Models.Channel;


public class NewChannelResponse implements IClientResponseCommands {

    private channelController chanCont;

    public NewChannelResponse(channelController channelControll){
        this.chanCont = channelControll;
    }
    public void SetChannelController(channelController channelControll){
        this.chanCont = channelControll;
    }
 
    @Override
    public void execute(String[] string){
        //NEWCHNL;CHANNEL
        chanCont.AddToAccesibleChannels(new Channel(string[1]));
        
    }
}
