package Models;
import Services.ChannelService;
import Services.MessageService;
import Services.UserService;
import Database.IChannelRepo;
import Database.IMessageRepo;
import Database.IUserRepo;


public class serverModel {
    private UserService userService;
    private MessageService messageService;
    private ChannelService channelService;



    public serverModel(UserService us, MessageService ms, ChannelService cs){
        this.userService = us;
        this.messageService = ms;
        this.channelService = cs;

    }
    public boolean DecodeIncomingTransmition(){



        return false;
    }
}
