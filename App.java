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

import java.util.LinkedList;

public class App {
    private static LoginController loginController;

    public static void main(String[] args) {
        // 1. START THE DATABASE
        PostgresTranslator myActualDatabase = new PostgresTranslator();
        myActualDatabase.registerMessageType("text", (u, c, t) -> new TextMessage(u, c, t));
        myActualDatabase.registerMessageType("image", (u, c, t) -> new ImageMessage(u, c, t));
        myActualDatabase.connect();

        // 2. CREATE THE SERVICES (The "Server" logic)
        // Notice how the Database is passed into the Services, not the UI!
        UserService userService = new UserService(myActualDatabase);
    // 1. Create the rule
        IChannelAccessRule myRule = new GlobalAccessRule();

// 2. Hand it to the Service
        ChannelService channelService = new ChannelService(myActualDatabase, myActualDatabase, myRule);
        MessageService messageService = new MessageService(myActualDatabase);

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
                chatController chatCtrl = new chatController(session, cView, messageService);
                channelController chanCtrl = new channelController(session, channelService, messageService, cView);

                // --- E. CONNECT VIEWS ---
                cView.setController(chatCtrl);
                
                // We use our perfectly cleaned up channelView!
                new channelView(chanCtrl, mygui);

                // --- F. LOAD THE FIRST ROOM ---
                // The Director handles everything: fetching history, saving to RAM, and drawing the screen!
                chanCtrl.changeChannel(startingChannel); 
            };

            // 4. Create the Login Controller (Uses the Services!)
            loginController = new LoginController(loginView, onLoginSuccess, userService, channelService);
            
            loginView.show();
        });
    }
}