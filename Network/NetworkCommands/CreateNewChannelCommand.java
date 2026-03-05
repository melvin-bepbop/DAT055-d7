package Network.NetworkCommands;

import java.time.LocalDateTime;

import Database.IChannelRepo;
import Database.IMessageRepo;
import Network.ClientHandler;

public class CreateNewChannelCommand implements INetworkCommand {
    public final static String identifier = "NEWCHNL";

    private IChannelRepo channelRepo;

    public CreateNewChannelCommand(IChannelRepo channelRepo){
        this.channelRepo = channelRepo;
    }
    @Override
    public void execute(String[] data, ClientHandler sender){
        //NEWCHNL;CHANNEL
        String Channel = data[1];

        channelRepo.AddChannel(Channel);
        sender.broadcastMessage(identifier+";"+Channel);
    }
}
