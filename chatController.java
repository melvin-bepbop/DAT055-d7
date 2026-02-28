import java.util.LinkedList;

public class chatController {
    private chatView chatview;
    private User user;
    private Model model;
    public chatController(Model model, chatView chatview, User user){
        this.model = model;
        this.chatview = chatview;
        this.user = user;
        
    }
    public void updateMessagesInChannel(){
        LinkedList<message> newMessages = model.GetNewMessagesInActiveChannel();
        for (message message : newMessages) {
            chatview.addMessageToDisplay(message, user);
        }
    }
    public void sendMessageToDatabase(String Content, String type){
        model.addMessage(Content, type);
        updateMessagesInChannel();
    }
    public void loadChannelHistory() {
        LinkedList<message> history = model.getHistoryForActiveChannel();
        User currentUser = model.getActiveUser();
        
        for (message msg : history) {
            chatview.addMessageToDisplay(msg, currentUser);
        }
    }
}
