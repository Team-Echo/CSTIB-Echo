package uk.ac.cam.echo.server.models;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Tag;
import uk.ac.cam.echo.server.serializers.TagSerializer;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

@JsonSerialize(using = TagSerializer.class)
@Entity
@Table(name="Tags")
public class TagModel extends BaseModel implements Tag {

    public TagModel() {

    }

    private static String[] allowed = {"name"};
    @JsonCreator
    public TagModel(Map<String, Object> props) {
        super(props, allowed);
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="ConversationIdSeq")
    @SequenceGenerator(name="ConversationIdSeq", sequenceName="Conversation_SEQ", allocationSize=1)
    private long id;
    private String name;

    @ManyToMany(mappedBy = "tags", targetEntity = ConversationModel.class)
    private Set<Conversation> conversationSet;

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
}
