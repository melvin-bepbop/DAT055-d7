package Models;

import java.time.LocalDateTime;

/**
 * Abstract base class for chat messages.
 *
 * Contains shared fields for username, content, type, and timestamp.
 */
public abstract class Message {
    protected String username;
    protected String content;
    protected LocalDateTime timeStamp;
    protected String type;

    /**
     * Creates a new message.
     *
     * @param user sender username
     * @param cont message content
     * @param type message type
     * @param time message timestamp
     */
    public Message(String user, String cont, String type, LocalDateTime time) {
        this.username = user;
        this.content = cont;
        this.type = type;
        this.timeStamp = time;
    }

    /**
     * Returns the sender username.
     *
     * @return username
     */
    public String getUsername() { return username; }

    /**
     * Returns the message content.
     *
     * @return text content or other data
     */
    public String getContent() { return content; }

    /**
     * Returns the message type.
     *
     * @return type string
     */
    public String getType() { return type; }

    /**
     * Returns the message timestamp.
     *
     * @return timestamp
     */
    public LocalDateTime getTimeStamp() { return timeStamp; }
}