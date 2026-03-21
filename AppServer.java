import java.io.IOException;
import java.net.ServerSocket;

import Network.Router;
import Network.Server;
import Network.NetworkCommands.ChangeChannelCommand;
import Network.NetworkCommands.CreateNewChannelCommand;
import Network.NetworkCommands.GetAllMessageCommand;
import Network.NetworkCommands.GetMessagesFromCommand;
import Network.NetworkCommands.GetServersCommand;
import Network.NetworkCommands.LoginCommand;
import Network.NetworkCommands.SendMessageCommand;
import Network.NetworkCommands.SignupCommand;
import Database.PostgresTranslator;
import Models.ImageMessage;
import Models.TextMessage;
import Services.ChannelService;
import Services.GlobalAccessRule;
import Services.IChannelAccessRule;
import Services.MessageService;
import Services.UserService;


/**
 * Server application entry point.
 *
 * Responsible for bootstrapping the backend: it creates and connects the
 * PostgresTranslator (implementing all repository interfaces), registers each
 * network command with a Router, and starts the TCP Server on a fixed port.
 * It also constructs service-layer classes that encapsulate higher-level
 * user, channel and message operations.
 */
public class AppServer {
    /**
     * Starts the chat server and listens for client connections.
     *
     * This method wires the Router to database-backed command handlers, binds
     * a ServerSocket to port 8080, and begins accepting clients through the
     * Server wrapper. It then initialises service objects that can be used for
     * higher-level server logic.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        PostgresTranslator database = PostgresTranslator.getInstance();
        database.registerMessageType("text", (u, c, t) -> new TextMessage(u, c, t));
        database.registerMessageType("image", (u, c, t) -> new ImageMessage(u, c, t));
        database.connect();

        Router serverRouter = new Router();
        serverRouter.registerCommand(LoginCommand.identifier, new LoginCommand(database));
        serverRouter.registerCommand(SignupCommand.identifier, new SignupCommand(database));
        serverRouter.registerCommand(GetServersCommand.identifier, new GetServersCommand(database));
        serverRouter.registerCommand(GetAllMessageCommand.identifier, new GetAllMessageCommand(database));
        serverRouter.registerCommand(ChangeChannelCommand.identifier, new ChangeChannelCommand(database));
        serverRouter.registerCommand(GetMessagesFromCommand.identifier,new GetMessagesFromCommand(database));
        serverRouter.registerCommand(SendMessageCommand.identifier, new SendMessageCommand(database));
        serverRouter.registerCommand(CreateNewChannelCommand.identifier, new CreateNewChannelCommand(database));

        //serverRouter.registerCommand(GetAllMessageCommand.identifier, new GetAllMessageCommand(database));


        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            Server server = new Server(serverSocket,serverRouter);
            System.out.println("Servern körs på port 8080...");
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        



         new UserService(database);
        IChannelAccessRule myRule = new GlobalAccessRule();

         new ChannelService(database, database, myRule);
         new MessageService(database);
       
    }
    
}
