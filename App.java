import javax.swing.SwingUtilities;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import Controllers.LoginController;
import Controllers.channelController;
import Controllers.chatController;
import Models.AccesibleChannels;
import Models.Channel;
import Models.ClientSession;
import Models.ImageMessage;
import Models.MessageFactory;
import Models.TextMessage;
import Models.User;
import Views.GUI;
import Views.LoginView;
import Views.channelView;
import Views.chatView;
import Network.Client;
import Network.ClientRouter;
import Network.ClientResponseCommands.*;
import Network.NetworkCommands.*;

/**
 * Client application entry point.
 *
 * Responsible for starting the desktop chat client: it establishes the socket
 * connection to the server, configures the Client and ClientRouter with all
 * response handlers, and wires together the MVC components.
 */
public class App {
    private static LoginController loginController;
    private static Client networkClient;
    private static ClientRouter clientRouter;

    public static void main(String[] args) {
    
        clientRouter = new ClientRouter();


        try {
            Socket socket = new Socket("localhost", 8080);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            
            networkClient = new Client(socket, bufferedReader, bufferedWriter, clientRouter);
            networkClient.listenForMessage();
            
        } catch (IOException e) {
            System.err.println("Kunde inte ansluta till servern: " + e.getMessage());
            return; 
        }

        
        SwingUtilities.invokeLater(() -> {
            
            LoginView loginView = new LoginView();
            
            
            Runnable onLoginSuccess = () -> {                
                User currentUser = loginController.getLoggedInUser();
                
                
                AccesibleChannels accessible = new AccesibleChannels(new LinkedList<>());
                Channel startingChannel = new Channel("Loading...");

                
                ClientSession session = new ClientSession(currentUser, startingChannel, accessible);

                
                GUI mygui = new GUI(new String[0]); 
                chatView cView = new chatView(mygui);
                channelView chanView = new channelView(mygui);

                
                cView.registerRenderer("text", (msg, g, time, isMe) -> {
                    g.addMessage(msg.getUsername(), msg.getContent(), time, isMe);
                });
                cView.registerRenderer("image", (msg, g, time, isMe) -> {
                    g.addImageMessage(msg.getUsername(), msg.getContent(), time, isMe);
                });

                
                new chatController(session, cView, networkClient);
                new channelController(session, networkClient, chanView);

                
                Map<String, MessageFactory> clientRegistry = new HashMap<>();
                clientRegistry.put("text", (u, c, t) -> new TextMessage(u, c, t));
                clientRegistry.put("image", (u, c, t) -> new ImageMessage(u, c, t));

                clientRouter.registerCommand(GetServersCommand.identifier, new GetServersResponse(session, networkClient));
                clientRouter.registerCommand(ChangeChannelCommand.identifier, new ChangeChannelResponse(session, networkClient));
                clientRouter.registerCommand(GetAllMessageCommand.identifier, new GetAllMessageResponse(session, clientRegistry));
                clientRouter.registerCommand(GetMessagesFromCommand.identifier, new GetMessagesFromResponse(session, clientRegistry));
                clientRouter.registerCommand(SendMessageCommand.identifier, new SentMessageResponse(session, clientRegistry));
                clientRouter.registerCommand(CreateNewChannelCommand.identifier, new NewChannelResponse(session));

                
                networkClient.sendMessage(GetServersCommand.identifier + ";" + currentUser.getUsername());
            };

            
            loginController = new LoginController(loginView, onLoginSuccess, networkClient);
            clientRouter.registerCommand(LoginCommand.identifier, new LoginResponse(loginController));
            clientRouter.registerCommand(SignupCommand.identifier, new SignupResponse(loginController));

            
            loginView.show();
        });
    }
}