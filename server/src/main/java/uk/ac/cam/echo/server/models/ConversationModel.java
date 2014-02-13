package uk.ac.cam.echo.server.models;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import uk.ac.cam.echo.data.*;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.serializers.ConversationSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
@JsonSerialize(using = ConversationSerializer.class)
@Entity
@Table(name="Conversation")
public class ConversationModel extends BaseModel implements Conversation {

    public ConversationModel() {

    }

    private static String[] allowed = {"name", "conferenceId"};
    @JsonCreator
    public ConversationModel(Map<String, Object> props) {
        super(props, allowed);
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="ConversationIdSeq")
    @SequenceGenerator(name="ConversationIdSeq", sequenceName="Conversation_SEQ", allocationSize=1)
    private long id;

    private String name;

    @ManyToOne(targetEntity = ConferenceModel.class)
    private Conference conference;

    @ManyToMany(targetEntity = TagModel.class)
    @JoinTable(name="CONVERSATION_TAG",
               joinColumns = {@JoinColumn(name = "conversation_id")},
               inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<Tag> tags;

    @OneToMany(targetEntity = MessageModel.class,
               mappedBy = "conversation")
    @OrderBy("timeStamp DESC")
    private List<Message> messages;

    @OneToMany(targetEntity = UserModel.class, mappedBy = "currentConversation")
    private Set<User> users;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addUser(User u) {
        users.add(u);
    }

    public void removeUser(User u) {
        if (users.contains(u)) {
            users.remove(u);
        }
    }

    public void addMessage(Message m) {
        messages.add(m);
    }

    public List<Message> getSortedMessages() {
        return new ArrayList<Message>(messages);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Message> getMessages(int n) {
        return HibernateUtil.getTransaction().createCriteria(MessageModel.class)
                .add(Restrictions.eq("conversation", this)).addOrder(Order.desc("timeStamp")).setMaxResults(n).list();
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void setConferenceId(Long id) {
        if (id != null && id != 0)
            setConference((Conference) HibernateUtil.getSession().load(ConferenceModel.class, id));
    }


}
