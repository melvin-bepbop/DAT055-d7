package Network.ClientResponseCommands;

import Controllers.channelController;
import Controllers.chatController;
import Models.Channel;
import Models.AccesibleChannels;


/**
 * Client-side handler for CHANGECHNL responses.
 *
 * Locates the target channel and instructs the controllers to switch channel
 * and update the chat view if the operation succeeded.
 */
public class ChangeChannelResponse implements IClientResponseCommands {

    private channelController chanCont;
    private chatController chatCont;

    /**
     * Creates a new ChangeChannelResponse handler.
     *
     * @param channelControll channel controller to update
     * @param chatController chat controller to notify about channel changes
     */
    public ChangeChannelResponse(channelController channelControll, chatController chatController){
        this.chanCont = channelControll;
        this.chatCont = chatController;
    }
    /**
     * Updates the channel controller reference after construction.
     *
     * @param channelControll channel controller to use
     */
    public void SetChannelController(channelController channelControll){
        this.chanCont = channelControll;
    }
    /**
     * Updates the chat controller reference after construction.
     *
     * @param chatCont chat controller to use
     */
    public void setChatCont(chatController chatCont) {
        this.chatCont = chatCont;
    }

    @Override
    /**
     * Executes the CHANGECHNL response.
     *
     * @param string protocol fields: CHANGECHNL;channelName;SUCCESS or CHANGECHNL;FAIL
     */
    public void execute(String[] string){
        if (!string[1].equals("FAIL")) {

            AccesibleChannels accessible = chanCont.GetAllChannels();
            Channel targetChannel = null;

            for (Channel c : accessible.getChannels()) {
                if (c.getChannelName().equals(string[1])) {
                    targetChannel = c;
                    break;
                }
            }
            chanCont.changeChannel(targetChannel);
            chatCont.ChangingChat(targetChannel);
        }

       else{
            System.out.println("Error couldnt change channel");
        }
    }
}
