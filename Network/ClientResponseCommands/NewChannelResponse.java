package Network.ClientResponseCommands;


import Controllers.channelController;
import Models.Channel;


/**
 * Client-side handler for NEWCHNL broadcasts.
 *
 * Adds the newly created channel to the accessible list in the channel controller.
 */
public class NewChannelResponse implements IClientResponseCommands {

    private channelController chanCont;

    /**
     * Creates a new NewChannelResponse handler.
     *
     * @param channelControll channel controller to update
     */
    public NewChannelResponse(channelController channelControll){
        this.chanCont = channelControll;
    }
    /**
     * Updates the channel controller reference after construction.
     *
     * @param channelControll channel controller to use
     */
    public void SetChannelController(channelController channelControll){
        this.chanCont = channelControll;
    }
 
    @Override
    /**
     * Executes the NEWCHNL response.
     *
     * @param string protocol fields: NEWCHNL;channelName
     */
    public void execute(String[] string){
        //NEWCHNL;CHANNEL
        chanCont.AddToAccesibleChannels(new Channel(string[1]));
        
    }
}
