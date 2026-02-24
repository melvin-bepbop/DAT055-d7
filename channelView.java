public class channelView {
    private channelController chnlctrl;
    public channelView (channelController channelController){
        this.chnlctrl = channelController;
    }
    public void ChangeChannel(Channel Channel){
        chnlctrl.ChangeChannel(Channel);
    }
    public AccesibleChannels getAccesibleChannels(){
        return chnlctrl.GetAllChannels();
    }
}
