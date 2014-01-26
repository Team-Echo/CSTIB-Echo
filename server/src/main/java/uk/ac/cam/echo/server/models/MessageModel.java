package uk.ac.cam.echo.server.models;

import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;

import javax.persistence.*;

@Table(name="Message")
public class MessageModel implements Message {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="MessageIdSeq")
    @SequenceGenerator(name="MessageIdSeq", sequenceName="Message_SEQ", allocationSize=1)
    private long id;

    private long timeStamp;
    private String contents;

    @ManyToOne(targetEntity = ConversationModel.class)
    private Conversation conversation;

    @ManyToOne(targetEntity = UserModel.class)
    private User sender;

    /** Getter and Setters **/
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
