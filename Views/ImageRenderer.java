package Views;

import Models.Message;
import Utils.ImageUtils;
import javax.swing.ImageIcon;

public class ImageRenderer implements MessageRenderer {
    @Override
    public void draw(Message msg, GUI gui, String timestamp, boolean isMe) {
        // Image-specific replacement logic
        ImageIcon icon = ImageUtils.decodeBase64ToImage(msg.getContent());
        if (icon != null) {
            gui.addImageMessage(msg.getUsername(), icon, timestamp, isMe);
        }
    }
}