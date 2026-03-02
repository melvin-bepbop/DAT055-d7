package Services;
import java.time.LocalDateTime;
import java.util.LinkedList;

import Database.IMessageRepo;
import Models.Channel;
import Models.User;
import Models.Message;

public class MessageService {
    private IMessageRepo messageRepo;

    public MessageService(IMessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public void sendMessage(User user, Channel channel, String content, String type) {
        messageRepo.AddMessage(
            user.getUsername(), 
            LocalDateTime.now(), 
            channel.getChannelName(), 
            type, 
            content
        );
    }

    public LinkedList<Message> getHistory(Channel channel) {
        return messageRepo.GetAllMessagesInChannel(channel.getChannelName());
    }
    


    public LinkedList<Message> getNewMessages(Channel channel, LocalDateTime lastChecked) {
        return messageRepo.GetNewMessagesInChannelFromTimeStamp(
            channel.getChannelName(), 
            lastChecked
        );
    }
}