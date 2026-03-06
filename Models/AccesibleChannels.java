package Models;
import java.util.LinkedList;

/**
 * Holds a list of channels that a user can access.
 *
 * Used on the client side to represent accessible channels in the session.
 */
public class AccesibleChannels {
    private LinkedList<Channel> channels;

    /**
     * Creates a new AccesibleChannels instance backed by an existing list.
     *
     * @param channels list of channels
     */
    public AccesibleChannels(LinkedList<Channel> channels) {
        this.channels = channels;
    }

    /**
     * Creates an empty AccesibleChannels instance.
     */
    public AccesibleChannels() {
        this.channels = new LinkedList<>();
    }

    /**
     * Returns the list of channels.
     *
     * @return list of channel objects
     */
    public LinkedList<Channel> getChannels() {
        return channels;
    }

    /**
     * Adds a channel to the list.
     *
     * @param channel channel to add
     */
    public void addChannel(Channel channel) {
        this.channels.add(channel);
    }
}