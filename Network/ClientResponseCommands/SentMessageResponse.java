package Network.ClientResponseCommands;

import java.time.LocalDateTime;

import Controllers.chatController;
import Models.Message;
import Models.TextMessage;
import Models.ImageMessage;

public class SentMessageResponse implements IClientResponseCommands {

    private chatController chatCont;

    public SentMessageResponse(){
    }

    public void setChatCont(chatController chatCont) {
        this.chatCont = chatCont;
    }
    @Override
    public void execute(String[] string){
        //SENDMSG;CHANNEL;USER;TYPE;CONTENT;TIME
        String Channel = string[1];
        String user = string[2];
        String type = string[3];
        String content = string[4];
        LocalDateTime ldt = LocalDateTime.parse(string[5]);
        Message msg;
        if(type.equals("text")){
            msg = new TextMessage(user, content, ldt);
            System.out.println("recieved txt");
        }
        else{
            msg = new ImageMessage(user, content, ldt);
            System.out.println("recieved img");

        }
        chatCont.addNewMessageIfInChannel(Channel, msg);
    }
}
