package uk.ac.cam.echo.server.models;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.serializers.MessageSerializer;

import javax.persistence.*;
import java.util.Map;

@JsonSerialize(using= MessageSerializer.class)
@Entity
@Table(name="Message")
public class MessageModel extends BaseModel implements Message {
    public MessageModel() {

    }

    private static String[] allowed = {"contents", "senderId", "conversationId"};
    @JsonCreator
    public MessageModel(Map<String, Object> props) {
        super(props, allowed);
    }

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
    public void setConversationId(Long  id) {
        if (id != null) {
            this.conversation = (Conversation) HibernateUtil.getSession().load(ConversationModel.class, id);
        }
    }

    public User getSender() {
        return sender;
    }

    public String getSenderName() {
        if (sender.getDisplayName() != null) return sender.getDisplayName();
        return sender.getUsername();
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setSenderId(Long id) {
        if (id != null)
            this.sender = (User) HibernateUtil.getSession().load(UserModel.class, id);
    }
}
