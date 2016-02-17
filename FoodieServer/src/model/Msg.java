package model;



import java.util.Date;

/**
 * @author Guangyu Chen, Yaoxiu Hu
 * @version 2.0
 */

public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;

    private final String content;
    private final User sender;
    private final User receiver;
    private int type;
    private final Date time;

    /**
     * recommended constructor
     *
     * @param sender   message sender
     * @param receiver message receiver
     * @param content  chat content in String
     */
    public Msg(User sender, User receiver, String content) {
        if (sender == null) {
            throw new IllegalArgumentException("Sender cannot be null");
        }
        if (receiver == null) {
            throw new IllegalArgumentException("Receiver cannot be null");
        }
        if ((content == null) || (content.length() == 0)) {
            throw new IllegalArgumentException("Message content cannot be null or empty");
        }
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        // automatically set date and time as the message is created
        this.type = TYPE_SENT;
        this.time = new Date();
    }

    public String getContent() {
        return content;
    }

    public void sendToReceive() {
        type = TYPE_RECEIVED;
    }

    public int getType() {
        return type;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public Date getTime() {
        return time;
    }
}
