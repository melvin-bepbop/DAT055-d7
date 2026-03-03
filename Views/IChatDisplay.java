package Views;

import java.io.File;

public interface IChatDisplay {
    // 1. Matches your exact GUI method: addMessage()
    void addMessage(String user, String text, String time, boolean isMe);
    
    // 2. We use an overloaded method for images (takes String instead of ImageIcon)
    void addImageMessage(String user, String base64Data, String time, boolean isMe);
    
    // 3. Matches your exact GUI methods:
    void clearChat();
    String getInputText();
    void clearInputField();
    
    // 4. UI Interactions & Listeners (These hide the JButtons from the Controller)
    File promptUserForImageFile(); 
    void onSendAction(Runnable action);
    void onImageUploadAction(Runnable action);
}