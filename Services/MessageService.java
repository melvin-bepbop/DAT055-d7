package Services;
import java.time.LocalDateTime;
import java.util.LinkedList;

import Database.IMessageRepo;
import Models.Channel;
import Models.User;
import Models.Message;

/**
 * Provides message-related application logic.
 *
 * This service wraps a message repository and offers convenience methods
 * for sending messages and retrieving message history.
 */
public class MessageService {
    private IMessageRepo messageRepo;

    /**
     * Creates a message service.
     *
     * @param messageRepo repository used for message operations
     */
    public MessageService(IMessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    /**
     * Persists a message for a channel.
     *
     * @param user author of the message
     * @param channel channel to store the message in
     * @param content message content
     * @param type message type identifier
     */
    public void sendMessage(User user, Channel channel, String content, String type) {
        messageRepo.AddMessage(
            user.getUsername(), 
            LocalDateTime.now(), 
            channel.getChannelName(), 
            type, 
            content
        );
    }

    /**
     * Retrieves the full message history for a channel.
     *
     * @param channel channel to load history for
     * @return list of messages in the channel
     */
    public LinkedList<Message> getHistory(Channel channel) {
        return messageRepo.GetAllMessagesInChannel(channel.getChannelName());
    }
    


    /**
     * Retrieves messages newer than a given timestamp for a channel.
     *
     * @param channel channel to query
     * @param lastChecked lower bound timestamp (exclusive)
     * @return list of messages after the given timestamp
     */
    public LinkedList<Message> getNewMessages(Channel channel, LocalDateTime lastChecked) {
        return messageRepo.GetNewMessagesInChannelFromTimeStamp(
            channel.getChannelName(), 
            lastChecked
        );
    }
}