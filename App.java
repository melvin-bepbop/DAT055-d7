import javax.swing.SwingUtilities;

import Controllers.LoginController;
import Controllers.channelController;
import Controllers.chatController;
import Database.PostgresTranslator;
import Models.AccesibleChannels;
import Models.Channel;
import Models.ClientSession;
import Models.ImageMessage;
import Models.TextMessage;
import Models.User;
import Services.ChannelService;
import Services.GlobalAccessRule;
import Services.IChannelAccessRule;
import Services.MessageService;
import Services.UserService;
import Views.GUI;
import Views.LoginView;
import Views.channelView;
import Views.chatView;
import Network.Client;
import Network.ClientRouter;
import Network.ClientResponseCommands.GetServersResponse;
import Network.ClientResponseCommands.LoginResponse;
import Network.ClientResponseCommands.SignupResponse;
import Network.NetworkCommands.GetServersCommand;
import Network.NetworkCommands.LoginCommand;
import Network.NetworkCommands.SignupCommand;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;

public class App {
    private static LoginController loginController;
    private static Client networkClient;
    private static ClientRouter clientRouter;
    private static channelController chanCtrl;

    public static void main(String[] args) {
        LinkedList<Channel> channelList = new LinkedList<>();
        AccesibleChannels accessible = new AccesibleChannels(channelList);
        clientRouter = new ClientRouter();
        GetServersResponse gsr = new GetServersResponse(chanCtrl);


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

                // Create the pure data session! No database links here.
                ClientSession session = new ClientSession(currentUser, startingChannel, accessible);

                // --- C. BUILD THE UI ---
              String[] channelNames = new String[channelList.size()];
                for (int i = 0; i < channelList.size(); i++) {
                    channelNames[i] = channelList.get(i).getChannelName();
                }
                
                GUI mygui = new GUI(channelNames);
                chatView cView = new chatView(mygui);

                // ---> UPDATED LAMBDAS <---
                cView.registerRenderer("text", (msg, g, time, isMe) -> {
                    g.addMessage(msg.getUsername(), msg.getContent(), time, isMe);
                });

                cView.registerRenderer("image", (msg, g, time, isMe) -> {
                    // No more ImageIcon conversion here! 
                    // We just pass the Base64 string directly to the interface.
                    g.addImageMessage(msg.getUsername(), msg.getContent(), time, isMe);
                });

                // --- D. CREATE CONTROLLERS ---
                // Notice how they take the Session AND the Services now
                

                chatController chatCtrl = new chatController(session, cView, networkClient);
                chanCtrl = new channelController(session, networkClient, cView);
                gsr.SetChannelController(chanCtrl);

                // --- E. CONNECT VIEWS ---
                cView.setController(chatCtrl);
                
                // We use our perfectly cleaned up channelView!
                channelView chanView = new channelView(chanCtrl, mygui);

                chanCtrl.setChanView(chanView);

                // --- F. LOAD THE FIRST ROOM ---
                // The Director handles everything: fetching history, saving to RAM, and drawing the screen!
                chanCtrl.changeChannel(startingChannel); 
            };

            // 4. Create the Login Controller
            loginController = new LoginController(loginView, onLoginSuccess, networkClient);
            
            loginView.show();
            clientRouter.registerCommand(GetServersCommand.identifier, gsr);
            clientRouter.registerCommand(LoginCommand.identifier, new LoginResponse(loginController));
            clientRouter.registerCommand(SignupCommand.identifier, new SignupResponse(loginController));
        });
    }
    
}