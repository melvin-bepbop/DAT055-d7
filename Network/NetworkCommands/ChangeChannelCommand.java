package Network.NetworkCommands;

import java.sql.SQLException;
import java.util.LinkedList;

import Database.IChannelRepo;
import Database.IMessageRepo;
import Models.Message;
import Network.ClientHandler;

public class ChangeChannelCommand implements INetworkCommand {
    public final static String identifier = "CHANGECHNL";

    private IChannelRepo chanRepo;

    public ChangeChannelCommand(IChannelRepo chanRepo){
        this.chanRepo = chanRepo;
    }
    @Override
    public void execute(String[] data, ClientHandler sender){
        String Channel = data[1];
        String user = data[2];
        
        chanRepo.UserJoinChannel(user,Channel);
        chanRepo.UserLeaveChannel(user, Channel);

        sender.respondToClient(identifier+";"+Channel+";SUCCESS");
    }
}
