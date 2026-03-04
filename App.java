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

import java.util.LinkedList;

public class App {
    private static LoginController loginController;

    public static void main(String[] args) {
        /*
        PostgresTranslator myActualDatabase = new PostgresTranslator();

        myActualDatabase.connect();
    
        UserService userService = new UserService(myActualDatabase);
        IChannelAccessRule myRule = new GlobalAccessRule();

        ChannelService channelService = new ChannelService(myActualDatabase, myActualDatabase, myRule);
        MessageService messageService = new MessageService(myActualDatabase);*/

        // 3. Start the GUI thread
        SwingUtilities.invokeLater(() -> {
            
            // Create the Login UI
            LoginView loginView = new LoginView();
            
            Runnable onLoginSuccess = () -> {                
                // --- A. GET LOGGED IN USER ---
                User currentUser = loginController.getLoggedInUser();
                
                // --- B. FETCH DATA FROM SERVER & CREATE SESSION ---
                LinkedList<Channel> channelList = channelService.loadUserChannels(currentUser);
                AccesibleChannels accessible = new AccesibleChannels(channelList);
                
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
                // Notice how they take the Session AND the Services now!
                chatController chatCtrl = new chatController(session, cView, networkClient);
                channelController chanCtrl = new channelController(session, networkClient, cView);

                // --- E. CONNECT VIEWS ---
                cView.setController(chatCtrl);
                
                // We use our perfectly cleaned up channelView!
                new channelView(chanCtrl, mygui);

                // --- F. LOAD THE FIRST ROOM ---
                // The Director handles everything: fetching history, saving to RAM, and drawing the screen!
                chanCtrl.changeChannel(startingChannel); 
            };

            // 4. Create the Login Controller
            loginController = new LoginController(loginView, onLoginSuccess, networkClient);
            
            loginView.show();
        });
    }
}