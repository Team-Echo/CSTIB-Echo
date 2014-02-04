package uk.ac.cam.echo.server.models;

import org.codehaus.jackson.annotate.JsonCreator;
import uk.ac.cam.echo.data.Tag;

import javax.persistence.*;
import java.util.Map;

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
}
