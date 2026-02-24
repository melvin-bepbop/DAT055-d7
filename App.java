import javax.swing.SwingUtilities;
import java.util.LinkedList;

public class App {
    public static void main(String[] args) {
        // 1. Connect to the Postgres Database
        Database.connect();

        // 2. Run the UI safely on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            
            // 3. Initialize the User 
            // Using your constructor: User(String username, String password)
            // Note: Your constructor will automatically try to insert this into the DB.
            User currentUser = new User("TestUser", "Password123"); 

            // 4. Determine the starting Channel
            // We need a Channel object to pass into the Model. 
            // We can just create a default one for startup.
            Channel startingChannel = new Channel("General");

            // 5. Initialize the Model 
            // Using your constructor: Model(User user, Channel channel)
            Model model = new Model(currentUser, startingChannel); 

            // 6. Fetch the initial channels to build the GUI
            // Your model already creates an AccesibleChannels instance, so we can use it!
            AccesibleChannels accessible = model.getAccesibleChannels();
            LinkedList<Channel> channelList = accessible.getChannels();
            
            // Convert Channel objects to a String array for the GUI
            String[] channelNames = new String[channelList.size()];
            for (int i = 0; i < channelList.size(); i++) {
                channelNames[i] = channelList.get(i).getChannelName();
            }

            // 7. Initialize GUI 
            // Using the exact variable name you requested
            GUI mygui = new GUI(channelNames);

            // 8. Initialize the Views
            // Using your exact constructors from chatView.java and channelView.java
            chatView cView = new chatView(mygui);

            // 9. Initialize the Controllers
            // Using your exact constructors from chatController.java and channelController.java
            chatController chatCtrl = new chatController(model, cView, currentUser);
            channelController chanCtrl = new channelController(model);
            
            // 10. Plug the controller into the view
            cView.setController(chatCtrl);

            // 11. Initialize channelView
            channelView chanView = new channelView(chanCtrl, mygui);
           
            // This lets the channel buttons refresh the chat!
            chanView.setChatController(chatCtrl);

            // 12. Load the initial messages into the GUI for the starting channel
            chatCtrl.updateMessagesInChannel();
        });
    }
}