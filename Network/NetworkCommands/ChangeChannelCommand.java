package Network.NetworkCommands;

import java.sql.SQLException;
import java.util.LinkedList;

import Database.IChannelRepo;
import Database.IMessageRepo;
import Models.Message;
import Network.ClientHandler;

/**
 * Handles CHANGECHNL requests on the server.
 *
 * Updates the user's active channel according to the channel repository
 * and reports success back to the client.
 */
public class ChangeChannelCommand implements INetworkCommand {
    public final static String identifier = "CHANGECHNL";

    private IChannelRepo chanRepo;

    /**
     * Creates a new ChangeChannelCommand.
     *
     * @param chanRepo repository used to update active channel state
     */
    public ChangeChannelCommand(IChannelRepo chanRepo){
        this.chanRepo = chanRepo;
    }
    @Override
    /**
     * Executes the CHANGECHNL command.
     *
     * @param data protocol fields: CHANGECHNL;channelName;username
     * @param sender client handler that sent the request
     */
    public void execute(String[] data, ClientHandler sender){
        String Channel = data[1];
        String user = data[2];
        
        chanRepo.UserJoinChannel(user,Channel);
        chanRepo.UserLeaveChannel(user, Channel);

        sender.respondToClient(identifier+";"+Channel+";SUCCESS");
    }
}
