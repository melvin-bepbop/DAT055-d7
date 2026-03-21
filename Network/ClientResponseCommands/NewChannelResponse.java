package Network.ClientResponseCommands;


import Models.ISessionModel;
import Models.Channel;



/**
 * Client-side handler for NEWCHNL broadcasts.
 *
 * Adds the newly created channel to the accessible list in the channel controller.
 */
public class NewChannelResponse implements IClientResponseCommands {

    private ISessionModel session;

 /**
     * Creates a new NewChannelResponse handler.
     *
     * @param session the application state model
     */
    public NewChannelResponse(ISessionModel session){
        this.session = session;
    }
 
    @Override
    /**
     * Executes the NEWCHNL response.
     *
     * @param string protocol fields: NEWCHNL;channelName
     */
    public void execute(String[] string){
        //NEWCHNL;CHANNEL
        session.addAccessibleChannel(new Channel(string[1]));
        
    }
}
