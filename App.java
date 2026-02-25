import javax.swing.SwingUtilities;
import java.util.LinkedList;

public class App {
    public static void main(String[] args) {
        // 1. Connect to the Postgres Database
        Database.connect();

        // 2. Run the UI safely on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            
            // 3. Initialize the User 
            User currentUser = new User("TestUseyk", "Password123"); 

            // 4. Initialize the Model with a temporary placeholder channel
            // We just need the Model built so we can ask it for the user's actual channels
            Channel tempChannel = new Channel("Loading...");
            Model model = new Model(currentUser, tempChannel); 

            // 5. Fetch the REAL channels for this user from the database
            AccesibleChannels accessible = model.getAccesibleChannels();
            LinkedList<Channel> channelList = accessible.getChannels();
            
            // 6. Set the real starting channel (the first one in their list)
            if (!channelList.isEmpty()) {
                Channel realStartingChannel = channelList.get(0);
                // Force the Model to switch to this real channel object
                model.changeChannel(realStartingChannel);
            } else {
                System.out.println("Warning: This user has no assigned channels!");
            }

            // 7. Convert Channel objects to a String array for the GUI sidebar
            String[] channelNames = new String[channelList.size()];
            for (int i = 0; i < channelList.size(); i++) {
                channelNames[i] = channelList.get(i).getChannelName();
            }

            // 8. Initialize GUI 
            GUI mygui = new GUI(channelNames);

            // 9. Initialize the Views
            chatView cView = new chatView(mygui);

            // 10. Initialize the Controllers
            chatController chatCtrl = new chatController(model, cView, currentUser);
            channelController chanCtrl = new channelController(model);
            
            // 11. Plug the controller into the chatView
            cView.setController(chatCtrl);

            // 12. Initialize channelView and plug in the chatController
            channelView chanView = new channelView(chanCtrl, mygui);
            chanView.setChatController(chatCtrl);

            // 13. Load the initial messages into the GUI for the starting channel
            chatCtrl.updateMessagesInChannel();
        });
    }
}