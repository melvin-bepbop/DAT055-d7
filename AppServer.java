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
import Controllers.channelController;
import Controllers.chatController;
import Database.PostgresTranslator;
import Models.AccesibleChannels;
import Models.Channel;
import Models.ClientSession;
import Models.ImageMessage;
import Models.TextMessage;
//import Models.serverModel;
import Models.User;
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
        PostgresTranslator db = new PostgresTranslator();
        db.registerMessageType("text", (u, c, t) -> new TextMessage(u, c, t));
        db.registerMessageType("image", (u, c, t) -> new ImageMessage(u, c, t));
        db.connect();

        Router serverRouter = new Router();
        serverRouter.registerCommand(LoginCommand.identifier, new LoginCommand(db));
        serverRouter.registerCommand(SignupCommand.identifier, new SignupCommand(db));
        serverRouter.registerCommand(GetServersCommand.identifier, new GetServersCommand(db));
        serverRouter.registerCommand(GetAllMessageCommand.identifier, new GetAllMessageCommand(db));
        serverRouter.registerCommand(ChangeChannelCommand.identifier, new ChangeChannelCommand(db));
        serverRouter.registerCommand(GetMessagesFromCommand.identifier,new GetMessagesFromCommand(db));
        serverRouter.registerCommand(SendMessageCommand.identifier, new SendMessageCommand(db));
        serverRouter.registerCommand(CreateNewChannelCommand.identifier, new CreateNewChannelCommand(db));

        //serverRouter.registerCommand(GetAllMessageCommand.identifier, new GetAllMessageCommand(db));


        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            Server server = new Server(serverSocket,serverRouter);
            System.out.println("Servern körs på port 8080...");
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        



        UserService userService = new UserService(db);
        IChannelAccessRule myRule = new GlobalAccessRule();

        ChannelService channelService = new ChannelService(db, db, myRule);
        MessageService messageService = new MessageService(db);
       // serverModel sm = new serverModel(userService, messageService, channelService);
    }
    
}
