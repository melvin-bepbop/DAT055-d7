import java.util.LinkedList;

public class UserManager {
    private IDatabase db;

    public UserManager(IDatabase db) {
        this.db = db;
    }

    public LinkedList<Channel> getAllowedChannels(User user) {
        return db.GetAllChannelsWhereUserIn(user.getUsername());
    }

    // You can move your join/leave logic here too!
    public void grantAccess(User user, Channel channel) {
        db.UserJoinChannel(user.getUsername(), channel.getChannelName());
    }

    public void revokeAccess(User user, Channel channel) {
        db.UserLeaveChannel(user.getUsername(), channel.getChannelName());
    }
}