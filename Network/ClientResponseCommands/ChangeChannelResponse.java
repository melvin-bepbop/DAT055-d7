package Network.ClientResponseCommands;


import Models.ISessionModel;
import Models.MessagesInChannel;
import Models.Channel;
import Models.AccesibleChannels;
import Network.Client;
import Network.NetworkCommands.GetAllMessageCommand;
import Network.NetworkCommands.GetMessagesFromCommand;


/**
 * Client-side handler for CHANGECHNL responses.
 *
 * Locates the target channel and instructs the controllers to switch channel
 * and update the chat view if the operation succeeded.
 */
public class ChangeChannelResponse implements IClientResponseCommands {

    private ISessionModel session;
    private Client networkClient;

/**
     * Creates a new ChangeChannelResponse handler.
     *
     * @param session the application state model
     * @param networkClient the client used to send follow-up requests for chat history
     */
    public ChangeChannelResponse(ISessionModel session, Client networkClient) {
        this.session = session;
        this.networkClient = networkClient;
    }

    @Override
    /**
     * Executes the CHANGECHNL response.
     *
     * @param string protocol fields: CHANGECHNL;channelName;SUCCESS or CHANGECHNL;FAIL
     */
    public void execute(String[] string){
        if (!string[1].equals("FAIL")) {

            AccesibleChannels accessible = session.getAccesibleChannels();
            Channel targetChannel = null;

            for (Channel c : accessible.getChannels()) {
                if (c.getChannelName().equals(string[1])) {
                    targetChannel = c;
                    break;
                }
            }
           if (targetChannel != null) {
                session.changeChannel(targetChannel);

                boolean isLoaded = false;
                MessagesInChannel chanHistory = new MessagesInChannel(targetChannel);
                
                for (MessagesInChannel messagesInChannel : session.getMsgHistoryInChannels()) {
                    if(messagesInChannel.getChannel().getChannelName().equals(targetChannel.getChannelName())){
                        isLoaded = true;
                        chanHistory = messagesInChannel;
                        break;
                    }
                }
                
                if (!isLoaded) {
                    networkClient.sendMessage(GetAllMessageCommand.identifier + ";" + targetChannel.getChannelName());
                } else {
                    networkClient.sendMessage(GetMessagesFromCommand.identifier + ";" + targetChannel.getChannelName() + ";" + chanHistory.getLastUpdated().toString());
                }
            
        }

       else {
                System.out.println("Error: Target channel " + string[1] + " not found in accessible channels.");
            }
    }
    else{
            System.out.println("Error couldnt change channel");
        }
}
}
