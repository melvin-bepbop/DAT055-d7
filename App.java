import javax.swing.SwingUtilities;
import java.util.LinkedList;

public class App {
    private static LoginController loginController;
    public static void main(String[] args) {
        // Start database
        Database.connect();

        //GUI
        SwingUtilities.invokeLater(() -> {
            //Login/Sign in
            LoginView loginView = new LoginView();
            
            //Skapa controllern
            loginController = new LoginController(loginView, () -> {                
                //Hämta inloggad användaren
                User currentUser = loginController.getLoggedInUser();
                Channel tempChannel = new Channel("Loading...");
                Model model = new Model(currentUser, tempChannel); 

            //hämta kanalerna
            AccesibleChannels accessible = model.getAccesibleChannels();
            LinkedList<Channel> channelList = accessible.getChannels();
            
            //sätt start kanal
            if (!channelList.isEmpty()) {
                Channel realStartingChannel = channelList.get(0);
                model.changeChannel(realStartingChannel);
            } else {
                System.out.println("Warning: This user has no assigned channels!");
            }

            //channel name string array
            String[] channelNames = new String[channelList.size()];
            for (int i = 0; i < channelList.size(); i++) {
                channelNames[i] = channelList.get(i).getChannelName();
            }
            GUI mygui = new GUI(channelNames);

            chatView cView = new chatView(mygui);

            chatController chatCtrl = new chatController(model, cView, currentUser);
            channelController chanCtrl = new channelController(model);

            cView.setController(chatCtrl);

            channelView chanView = new channelView(chanCtrl, mygui);
            chanView.setChatController(chatCtrl);

            chatCtrl.updateMessagesInChannel();
        });
        loginView.show();
    });
}
}