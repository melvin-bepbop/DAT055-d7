package Network.NetworkCommands;

import java.util.LinkedList;

import Database.IChannelRepo;
import Database.IMessageRepo;
import Models.Message;
import Network.ClientHandler;

public class GetAllMessageCommand implements INetworkCommand {
    public final static String identifier = "GETALLMSG";

    private IMessageRepo msgRepo;

    public GetAllMessageCommand(IMessageRepo messageRepo){
        this.msgRepo = messageRepo;
    }
    @Override
    public void execute(String[] data, ClientHandler sender){
        String Channel = data[1];
        LinkedList<Message> msgs  = msgRepo.GetAllMessagesInChannel(Channel);
        if (msgs != null) {
            String msg = "" + identifier+";"+Channel;
            for (Message msgnext: msgs) {
                msg = msg + ";" + msgnext.getUsername() + ";" + msgnext.getType() + ";" + msgnext.getContent() + ";" + msgnext.getTimeStamp().toString();
            }
            sender.respondToClient(msg);
            //System.out.println(msg);
        }
        else{
            sender.respondToClient(identifier+";FAIL");
        }
    }
    
}
