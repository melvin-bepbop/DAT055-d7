package Network.NetworkCommands;

import java.time.LocalDateTime;

import Database.IChannelRepo;
import Database.IMessageRepo;
import Network.ClientHandler;

/**
 * Handles NEWCHNL requests on the server.
 *
 * Creates a new channel in the repository and broadcasts its creation
 * to all connected clients.
 */
public class CreateNewChannelCommand implements INetworkCommand {
    public final static String identifier = "NEWCHNL";

    private IChannelRepo channelRepo;

    /**
     * Creates a new CreateNewChannelCommand.
     *
     * @param channelRepo repository used to add channels
     */
    public CreateNewChannelCommand(IChannelRepo channelRepo){
        this.channelRepo = channelRepo;
    }
    @Override
    /**
     * Executes the NEWCHNL command.
     *
     * @param data protocol fields: NEWCHNL;channelName
     * @param sender client handler that sent the request
     */
    public void execute(String[] data, ClientHandler sender){
        //NEWCHNL;CHANNEL
        String Channel = data[1];

        channelRepo.AddChannel(Channel);
        sender.broadcastMessage(identifier+";"+Channel);
    }
}
