package Network.NetworkCommands;

import java.time.LocalDateTime;
import java.util.LinkedList;

import Database.IMessageRepo;
import Models.Message;
import Network.ClientHandler;

/**
 * Handles SENDMSG requests on the server.
 *
 * Persists the incoming message using the message repository and broadcasts
 * the original payload to all connected clients.
 */
public class SendMessageCommand implements INetworkCommand {
    public final static String identifier = "SENDMSG";

    private IMessageRepo msgRepo;

    /**
     * Creates a new SendMessageCommand.
     *
     * @param messageRepo repository used to store messages
     */
    public SendMessageCommand(IMessageRepo messageRepo){
        this.msgRepo = messageRepo;
    }
    @Override
    /**
     * Executes the SENDMSG command.
     *
     * @param data protocol fields: SENDMSG;channel;user;type;content;time
     * @param sender client handler that sent the request
     */
    public void execute(String[] data, ClientHandler sender){
        //SENDMSG;CHANNEL;USER;TYPE;CONTENT;TIME
        String Channel = data[1];
        String user = data[2];
        String type = data[3];
        String content = data[4];
        LocalDateTime ldt = LocalDateTime.parse(data[5]);
        msgRepo.AddMessage(user, ldt, Channel, type, content);
        sender.broadcastMessage(String.join(";", data));
        
    }
}
//AddMessage(String userName, LocalDateTime time, String channelName, String type, String content)