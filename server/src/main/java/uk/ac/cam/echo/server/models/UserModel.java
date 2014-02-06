package uk.ac.cam.echo.server.models;

import org.codehaus.jackson.annotate.JsonCreator;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.server.HibernateUtil;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name="`User`")
public class UserModel extends BaseModel implements User {

    public UserModel() {

    }

    private static String[] allowed = {"username", "currentConversationId"};
    @JsonCreator
    public UserModel(Map<String, Object> props) {
        super(props, allowed);
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="UserIdSeq")
    @SequenceGenerator(name="UserIdSeq", sequenceName="USER_SEQ", allocationSize=1)
    private long id;
    private String username;

    @ManyToOne(targetEntity = ConversationModel.class)
    private Conversation currentConversation;


    /*    Getters and Setters */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Conversation getCurrentConversation() {
        return currentConversation;
    }

    public void setCurrentConversation(Conversation currentConversation) {
        this.currentConversation = currentConversation;
    }

    public void setCurrentConversationId(long id) {
        setCurrentConversation((Conversation) HibernateUtil.getSession().load(ConversationModel.class, id));
    }
}
