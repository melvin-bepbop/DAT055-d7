package Network.ClientResponseCommands;

import Controllers.channelController;

import Models.Channel;

public class GetServersResponse implements IClientResponseCommands {

    private channelController chanCont;

    public GetServersResponse(channelController channelControll){
        this.chanCont = channelControll;
    }
    public void SetChannelController(channelController channelControll){
        this.chanCont = channelControll;
    }

    @Override
    public void execute(String[] string){
        if (!string[1].equals("FAIL")) {
            Channel firstChannel = new Channel(string[1]);
            chanCont.AddToAccesibleChannels(firstChannel);
            for (int i = 2; i < string.length; i++) {
                String name = string[i];
                chanCont.AddToAccesibleChannels(new Channel(name));
            }
            chanCont.changeChannel(firstChannel);
            
        }
        else{
            System.out.println("Error couldnt find any accesible channels");
        }
    }
    
}
