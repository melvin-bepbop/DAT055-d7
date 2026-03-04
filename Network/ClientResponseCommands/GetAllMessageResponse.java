package Network.ClientResponseCommands;

import Controllers.channelController;
import Models.Channel;

public class GetAllMessageResponse implements IClientResponseCommands {

    private channelController chanCont;

    public GetAllMessageResponse(channelController channelControll){
        this.chanCont = channelControll;
    }
    public void SetChannelController(channelController channelControll){
        this.chanCont = channelControll;
    }
    @Override
    public void execute(String[] string){
        if (!string[1].equals("FAIL")) {
            for (int i = 1; i < string.length; i++) {
            String name = string[i];
            chanCont.AddToAccesibleChannels(new Channel(name));
            System.out.println("Added "+ name);
        }

        }
        else{
            System.out.println("Error couldnt find any accesible channels");
        }
    }
}
