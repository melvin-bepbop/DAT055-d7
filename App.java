import javax.swing.SwingUtilities;

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
import Network.ClientResponseCommands.ChangeChannelResponse;
import Network.ClientResponseCommands.GetAllMessageResponse;
import Network.ClientResponseCommands.GetMessagesFromResponse;
import Network.ClientResponseCommands.GetServersResponse;
import Network.ClientResponseCommands.LoginResponse;
import Network.ClientResponseCommands.NewChannelResponse;
import Network.ClientResponseCommands.SentMessageResponse;
import Network.ClientResponseCommands.SignupResponse;
import Network.NetworkCommands.ChangeChannelCommand;
import Network.NetworkCommands.CreateNewChannelCommand;
import Network.NetworkCommands.GetAllMessageCommand;
import Network.NetworkCommands.GetMessagesFromCommand;
import Network.NetworkCommands.GetServersCommand;
import Network.NetworkCommands.LoginCommand;
import Network.NetworkCommands.SendMessageCommand;
import Network.NetworkCommands.SignupCommand;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class App {
    private static LoginController loginController;
    private static Client networkClient;
    private static ClientRouter clientRouter;
    private static channelController chanCtrl;
    private static chatController chatCtrl;

    public static void main(String[] args) {
        LinkedList<Channel> channelList = new LinkedList<>();
        AccesibleChannels accessible = new AccesibleChannels(channelList);
        clientRouter = new ClientRouter();
        Map<String, MessageFactory> clientRegistry = new HashMap<>();
        clientRegistry.put("text", (u, c, t) -> new TextMessage(u, c, t));
        clientRegistry.put("image", (u, c, t) -> new ImageMessage(u, c, t));
        GetServersResponse gsr = new GetServersResponse(chanCtrl);
        GetAllMessageResponse gamr = new GetAllMessageResponse(chanCtrl, clientRegistry);
        ChangeChannelResponse ccr = new ChangeChannelResponse(chanCtrl, chatCtrl);
        GetMessagesFromResponse gmfr = new GetMessagesFromResponse(chanCtrl, clientRegistry);
        SentMessageResponse smr = new SentMessageResponse(clientRegistry);
        NewChannelResponse ncr = new NewChannelResponse(chanCtrl);


        try{
            Socket socket = new Socket("localhost", 8080);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            networkClient = new Client(socket, bufferedReader, bufferedWriter, clientRouter, "Guest");
            
            networkClient.listenForMessage();
            
        } catch (IOException e) {
            System.err.println("Kunde inte ansluta till servern: " + e.getMessage());
            return; 
        }

        ///FIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIX UNDER

        SwingUtilities.invokeLater(() -> {
            
            // Create the Login UI
            LoginView loginView = new LoginView();
            
            Runnable onLoginSuccess = () -> {                
                User currentUser = loginController.getLoggedInUser();
                
                networkClient.sendMessage(GetServersCommand.identifier+";"+currentUser.getUsername());
                
                Channel startingChannel = new Channel("Loading...");
                if (!channelList.isEmpty()) {
                    startingChannel = channelList.get(0);
                } else {
                    System.out.println("Warning: This user has no assigned channels!");
                }

                ClientSession session = new ClientSession(currentUser, startingChannel, accessible);

                // BUILD THE UI
              String[] channelNames = new String[channelList.size()];
                for (int i = 0; i < channelList.size(); i++) {
                    channelNames[i] = channelList.get(i).getChannelName();
                }
                
                GUI mygui = new GUI(channelNames);
                chatView cView = new chatView(mygui);

                cView.registerRenderer("text", (msg, g, time, isMe) -> {
                    g.addMessage(msg.getUsername(), msg.getContent(), time, isMe);
                });

                cView.registerRenderer("image", (msg, g, time, isMe) -> {

                    g.addImageMessage(msg.getUsername(), msg.getContent(), time, isMe);
                });

                // CREATE CONTROLLERS 
                

                 chatCtrl = new chatController(session, cView, networkClient);
                chanCtrl = new channelController(session, networkClient, cView);
                gsr.SetChannelController(chanCtrl);

                // CONNECT VIEWS
                cView.setController(chatCtrl);
                
               
                channelView chanView = new channelView(chanCtrl, mygui);

                chanCtrl.setChanView(chanView);
                ccr.SetChannelController(chanCtrl);
                gamr.SetChannelController(chanCtrl);
                gsr.SetChannelController(chanCtrl);
                ccr.setChatCont(chatCtrl);
                gamr.setChaCont(chatCtrl);;
                gmfr.SetChannelController(chanCtrl);
                gmfr.setChaCont(chatCtrl);
                smr.setChatCont(chatCtrl);
                ncr.SetChannelController(chanCtrl);



                // LOAD THE FIRST ROOM 
 
                chanCtrl.changeChannel(startingChannel); 
            };

            //Create the Login Controller
            loginController = new LoginController(loginView, onLoginSuccess, networkClient);
            
            loginView.show();
            //regsiter commands
            clientRouter.registerCommand(GetServersCommand.identifier, gsr);
            clientRouter.registerCommand(LoginCommand.identifier, new LoginResponse(loginController));
            clientRouter.registerCommand(SignupCommand.identifier, new SignupResponse(loginController));
            clientRouter.registerCommand(ChangeChannelCommand.identifier, ccr);
            clientRouter.registerCommand(GetAllMessageCommand.identifier, gamr);
            clientRouter.registerCommand(GetMessagesFromCommand.identifier, gmfr);
            clientRouter.registerCommand(SendMessageCommand.identifier, smr);
            clientRouter.registerCommand(CreateNewChannelCommand.identifier, ncr);


        });
    }
    
}