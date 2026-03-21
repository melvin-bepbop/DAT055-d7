package Views;

public interface IChannelView {
    /**
     * Interface for the Controller to listen to channel-related UI actions.
     */
    interface ViewListener {
        void changeChannel(String channelName);
        void createNewGlobalChannel(String newChannelName);
    }

    void setViewListener(ViewListener listener);
}