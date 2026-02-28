
public class channelController {
    private Model model;
    public channelController(Model model){
        this.model = model;

    }
    public void ChangeChannel(Channel channel){
        model.changeChannel(channel);
    }
    public AccesibleChannels GetAllChannels(){
        return model.getAccesibleChannels();
    }
public void createNewGlobalChannel(String newChannelName) {
        model.createNewGlobalChannel(newChannelName);
    }

}

