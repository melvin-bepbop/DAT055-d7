package Models;
import java.util.LinkedList;

public class AccesibleChannels {
    private LinkedList<Channel> channels;

    public AccesibleChannels(LinkedList<Channel> channels) {
        this.channels = channels;
    }

    public AccesibleChannels() {
        this.channels = new LinkedList<>();
    }

    public LinkedList<Channel> getChannels() {
        return channels;
    }

    public void addChannel(Channel channel) {
        this.channels.add(channel);
    }
}