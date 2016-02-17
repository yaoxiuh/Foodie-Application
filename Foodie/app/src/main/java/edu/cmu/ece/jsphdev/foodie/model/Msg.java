package edu.cmu.ece.jsphdev.foodie.model;



import java.util.Date;

/**
 * the msg class represnet the message
 */

public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;

    private final String content;
    private final User sender;
    private int type;

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
        this.content = content;
        // automatically set date and time as the message is created
        this.type = TYPE_SENT;
    }

    /**
     * To get the content of messasge
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * To send the message
     */
    public void sendToReceive() {
        type = TYPE_RECEIVED;
    }

    /**
     * To get the type of message
     * @return the type pf message
     */
    public int getType() {
        return type;
    }

    /**
     * To get the sender of the message
     * @return sender
     */
    public User getSender() {
        return sender;
    }

}
