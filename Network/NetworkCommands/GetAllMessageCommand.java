package Network.NetworkCommands;

import java.util.LinkedList;

import Database.IChannelRepo;
import Database.IMessageRepo;
import Models.Message;
import Network.ClientHandler;

/**
 * Handles GETALLMSG requests on the server.
 *
 * Retrieves all messages for a channel and encodes them into a flat
 * protocol response for the requesting client.
 */
public class GetAllMessageCommand implements INetworkCommand {
    public final static String identifier = "GETALLMSG";

    private IMessageRepo msgRepo;

    /**
     * Creates a new GetAllMessageCommand.
     *
     * @param messageRepo repository used to fetch messages
     */
    public GetAllMessageCommand(IMessageRepo messageRepo){
        this.msgRepo = messageRepo;
    }
    @Override
    /**
     * Executes the GETALLMSG command.
     *
     * @param data protocol fields: GETALLMSG;channelName
     * @param sender client handler that sent the request
     */
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
