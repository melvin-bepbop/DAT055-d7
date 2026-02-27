import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

public class channelView {
    private channelController chnlctrl;
    private chatController chatCtrl;
    private GUI gui;

    public channelView(channelController controller, GUI gui) {
        this.chnlctrl = controller;
        this.gui = gui;
    }

    public void setChatController(chatController chatCtrl) {
        this.chatCtrl = chatCtrl;
        setupListeners();
    }

    private void setupListeners() {
    for (int i = 0; i < gui.getChannelButtons().size(); i++) {
        JButton btn = gui.getChannelButtons().get(i);
        
        btn.addActionListener(e -> {
            String targetName = btn.getText();
            System.out.println("Switching to channel: " + targetName);

            AccesibleChannels accessible = chnlctrl.GetAllChannels();
            Channel targetChannel = null;

            for (Channel c : accessible.getChannels()) {
                if (c.getChannelName().equals(targetName)) {
                    targetChannel = c;
                    break;
                }
            }

            if (targetChannel != null) {
                gui.clearChat(); 
                chnlctrl.ChangeChannel(targetChannel); 
                
                if (chatCtrl != null) {
                    chatCtrl.updateMessagesInChannel();
                }
            } else {
                System.out.println("Error: Could not find channel object for " + targetName);
            }
        });
    }
}
}