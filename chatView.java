public class chatView {
    private GUI gui;
    public chatView(GUI gui){
        this.gui = gui;
    }
    public void addMessageToDisplay(message msg, User user){
        if(msg.getType() == "text"){
            gui.addMessage(msg.getUsername(), msg.getContent(), msg.getTimeStamp().toString(), user.getUsername() == msg.getUsername());
        }
    }
}
