package Network.ClientResponseCommands;

import Controllers.channelController;

import Models.Channel;

/**
 * Client-side handler for GETSERVERS responses.
 *
 * Populates the list of accessible channels in the channel controller and
 * switches to the first channel on success.
 */
public class GetServersResponse implements IClientResponseCommands {

    private channelController chanCont;

    /**
     * Creates a new GetServersResponse handler.
     *
     * @param channelControll controller that manages channel state
     */
    public GetServersResponse(channelController channelControll){
        this.chanCont = channelControll;
    }
    /**
     * Updates the channel controller reference after construction.
     *
     * @param channelControll controller that manages channel state
     */
    public void SetChannelController(channelController channelControll){
        this.chanCont = channelControll;
    }

    @Override
    /**
     * Executes the GETSERVERS response.
     *
     * @param string protocol fields: GETSERVERS;channel1;channel2;...
     */
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
