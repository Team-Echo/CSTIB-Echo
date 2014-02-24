package uk.ac.cam.echo.server.models;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import uk.ac.cam.echo.data.Interest;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.serializers.InterestSerializer;

import javax.persistence.*;
import java.util.Map;

@JsonSerialize(using=InterestSerializer.class)
@Entity
@Table(name="Interests")
public class InterestModel extends BaseModel implements Interest {
    public InterestModel() {

    }

    private static String[] allowed = {"name", "userId"};
    @JsonCreator
    public InterestModel(Map<String, Object> props) {
        super(props, allowed);
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="ConversationIdSeq")
    @SequenceGenerator(name="ConversationIdSeq", sequenceName="Conversation_SEQ", allocationSize=1)
    private long id;
    private String name;

    @ManyToOne(targetEntity = UserModel.class)
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId() {
        if (this.getUser() != null)
            return this.getUser().getId();
        return null;
    }

    public void setUserId(Long id) {
        if (id != null && id != 0)
            setUser((User) HibernateUtil.getSession().load(UserModel.class, id));
    }
}
