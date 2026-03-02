package Database;
import java.time.LocalDateTime;
import java.util.LinkedList;

import Models.Message;

public interface IMessageRepo {
    void AddMessage(String userName, LocalDateTime time, String channelName, String type, String content);
    LinkedList<Message> GetAllMessagesInChannel(String channel);
    LinkedList<Message> GetNewMessagesInChannelFromTimeStamp(String channel, LocalDateTime Timestamp);
}
