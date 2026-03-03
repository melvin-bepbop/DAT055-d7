import java.io.IOException;
import java.net.ServerSocket;

import Network.Server;
import Database.PostgresTranslator;
import Controllers.channelController;
import Controllers.chatController;
import Database.PostgresTranslator;
import Models.AccesibleChannels;
import Models.Channel;
import Models.ClientSession;
import Models.ImageMessage;
import Models.TextMessage;
import Models.serverModel;
import Models.User;
import Services.ChannelService;
import Services.GlobalAccessRule;
import Services.IChannelAccessRule;
import Services.MessageService;
import Services.UserService;


public class AppServer {
    public static void main(String[] args) {
        PostgresTranslator db = new PostgresTranslator();
        db.registerMessageType("text", (u, c, t) -> new TextMessage(u, c, t));
        db.registerMessageType("image", (u, c, t) -> new ImageMessage(u, c, t));
        db.connect();

        try {
            ServerSocket serverSocket = new ServerSocket(5432);
            Server server = new Server(serverSocket);
            System.out.println("Servern körs på port 5432...");
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UserService userService = new UserService(db);
        IChannelAccessRule myRule = new GlobalAccessRule();

        ChannelService channelService = new ChannelService(db, db, myRule);
        MessageService messageService = new MessageService(db);
        serverModel sm = new serverModel(userService, messageService, channelService);
    }
    
}
