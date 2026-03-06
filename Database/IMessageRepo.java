package Database;
import java.time.LocalDateTime;
import java.util.LinkedList;

import Models.Message;

/**
 * Repository interface for message-related database operations.
 *
 * Implementations store and retrieve messages for channels.
 */
public interface IMessageRepo {
    /**
     * Stores a message.
     *
     * @param userName message author username
     * @param time message timestamp
     * @param channelName channel name
     * @param type message type identifier
     * @param content message content
     */
    void AddMessage(String userName, LocalDateTime time, String channelName, String type, String content);

    /**
     * Fetches all messages in a channel.
     *
     * @param channel channel name
     * @return list of messages in the channel
     */
    LinkedList<Message> GetAllMessagesInChannel(String channel);

    /**
     * Fetches messages newer than a given timestamp in a channel.
     *
     * @param channel channel name
     * @param Timestamp lower bound timestamp (exclusive)
     * @return list of messages after the given timestamp
     */
    LinkedList<Message> GetNewMessagesInChannelFromTimeStamp(String channel, LocalDateTime Timestamp);
}
