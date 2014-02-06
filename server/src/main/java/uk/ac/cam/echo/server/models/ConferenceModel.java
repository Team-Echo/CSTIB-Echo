package uk.ac.cam.echo.server.models;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.server.serializers.ConferenceSerializer;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name="Conference")
@JsonSerialize(using=ConferenceSerializer.class)
public class ConferenceModel extends BaseModel implements Conference{
    public ConferenceModel() {

    }

    private static String[] allowed = {"name"};
    @JsonCreator
    public ConferenceModel(Map<String, Object> props) {
        super(props, allowed);
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="ConferenceIdSeq")
    @SequenceGenerator(name="ConferenceIdSeq", sequenceName="Conference_SEQ", allocationSize=1)
    private long id;

    private String name;

    @OneToMany(targetEntity = ConversationModel.class,
               mappedBy = "conference")
    private Set<Conversation> conversationSet;

    /** Serialization This is basically **/


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

    public Set<Conversation> getConversationSet() {
        return conversationSet;
    }

    public void setConversationSet(Set<Conversation> conversationSet) {
        this.conversationSet = conversationSet;
    }

    @Override
    public void addConversation(Conversation conv) {
        getConversationSet().add(conv);
    }
}
