package Network.ClientResponseCommands;

import Models.ISessionModel;
import Network.Client;
import Network.NetworkCommands.GetAllMessageCommand;
import Models.Channel;

/**
 * Client-side handler for GETSERVERS responses.
 *
 * Populates the list of accessible channels in the channel controller and
 * switches to the first channel on success.
 */
public class GetServersResponse implements IClientResponseCommands {

    private ISessionModel session;
    private Client networkClient;

    /**
     * Creates a new GetServersResponse handler.
     *
     * @param session the application state model
     */
    public GetServersResponse(ISessionModel session, Client networkClient) {
        this.session = session;
        this.networkClient = networkClient;
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
            session.addAccessibleChannel(firstChannel);
            networkClient.sendMessage(GetAllMessageCommand.identifier + ";" + firstChannel.getChannelName());
            for (int i = 2; i < string.length; i++) {
                String name = string[i];
                session.addAccessibleChannel(new Channel(name));
            }
            session.changeChannel(firstChannel);
            
        }
        else{
            System.out.println("Error couldnt find any accesible channels");
        }
    }
    
}
