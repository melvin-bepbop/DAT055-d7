import java.util.LinkedList;

public class AccesibleChannels {
    private LinkedList<Channel> channels;

    public AccesibleChannels(){
        channels = Database.GetAllChannels();
    }

    public LinkedList<Channel> getChannels() {
        return channels;
    }
}
