package Network.NetworkCommands;

import java.util.ArrayList;
import java.util.LinkedList;

import Database.IChannelRepo;
import Models.Channel;
import Network.ClientHandler;

public class GetServersCommand implements INetworkCommand{
    public final static String identifier = "GETSERVERS";

    private IChannelRepo channelRepo;

    public GetServersCommand(IChannelRepo channelRepo){
        this.channelRepo = channelRepo;
    }
    @Override
    public void execute(String[] data, ClientHandler sender){
        String username = data[1];
        LinkedList<Channel> channels  = channelRepo.GetAllChannels();
        if (channels != null) {
            String msg = "" + identifier;
            for (Channel channel : channels) {
                msg = msg+";"+channel.getChannelName();
            }
            sender.respondToClient(msg);
            //System.out.println(msg);
        }
        else{
            sender.respondToClient(identifier+";FAIL");
        }
    }
}


/*
package Network.NetworkCommands;

import Database.IUserRepo;
import Network.ClientHandler;

public class SignupCommand {
    
}
 */