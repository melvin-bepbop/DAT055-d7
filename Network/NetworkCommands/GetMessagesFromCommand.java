package Network.NetworkCommands;

import java.time.LocalDateTime;
import java.util.LinkedList;

import Database.IMessageRepo;
import Models.Message;
import Network.ClientHandler;

/**
 * Handles GETMSGSFROM requests on the server.
 *
 * Retrieves messages newer than a given timestamp for a channel and returns
 * them to the requesting client.
 */
public class GetMessagesFromCommand implements INetworkCommand {
    public final static String identifier = "GETMSGSFROM";

    private IMessageRepo msgRepo;

    /**
     * Creates a new GetMessagesFromCommand.
     *
     * @param messageRepo repository used to fetch messages
     */
    public GetMessagesFromCommand(IMessageRepo messageRepo){
        this.msgRepo = messageRepo;
    }
    @Override
    /**
     * Executes the GETMSGSFROM command.
     *
     * @param data protocol fields: GETMSGSFROM;channelName;timestamp
     * @param sender client handler that sent the request
     */
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
