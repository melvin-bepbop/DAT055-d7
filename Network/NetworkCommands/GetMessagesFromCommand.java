package Network.NetworkCommands;

import java.time.LocalDateTime;
import java.util.LinkedList;

import Database.IMessageRepo;
import Models.Message;
import Network.ClientHandler;

public class GetMessagesFromCommand implements INetworkCommand {
    public final static String identifier = "GETMSGSFROM";

    private IMessageRepo msgRepo;

    public GetMessagesFromCommand(IMessageRepo messageRepo){
        this.msgRepo = messageRepo;
    }
    @Override
    public void execute(String[] data, ClientHandler sender){
        String Channel = data[1];
        LocalDateTime time = LocalDateTime.parse(data[2]);
        LinkedList<Message> msgs  = msgRepo.GetNewMessagesInChannelFromTimeStamp(Channel, time);
        if (msgs != null) {
            String msg = "" + identifier+";"+Channel;
            for (Message msgnext: msgs) {
                msg = msg + ";" + msgnext.getUsername() + ";" + msgnext.getType() + ";" + msgnext.getContent() + ";" + msgnext.getTimeStamp().toString();
            }
            sender.respondToClient(msg);
            System.out.println(msg);
        }
        else{
            sender.respondToClient(identifier+";FAIL");
        }
    }
}
