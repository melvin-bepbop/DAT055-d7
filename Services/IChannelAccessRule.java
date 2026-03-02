package Services;
import Database.IChannelRepo;
import Database.IUserRepo;


public interface IChannelAccessRule {
    void grantInitialAccess(String newUsername, IChannelRepo channelRepo);
    
    void grantAccessForNewChannel(String newChannelName, String creatorUsername, IUserRepo userRepo, IChannelRepo channelRepo);
}