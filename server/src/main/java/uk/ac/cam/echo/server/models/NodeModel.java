package uk.ac.cam.echo.server.models;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import uk.ac.cam.echo.server.serializers.NodeSerializer;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

/**
 Author: Petar 'PetarV' Veličković

 A graph node model that will be put in permanent store.
*/
@JsonSerialize(using=NodeSerializer.class)
@Entity
@Table(name="Node")
public class NodeModel extends BaseModel
{
    public NodeModel() { }

    private static String[] allowed = {"name"};
    @JsonCreator
    public NodeModel(Map<String, Object> props)
    {
        super(props, allowed);
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="NodeIdSeq")
    @SequenceGenerator(name="NodeIdSeq", sequenceName="Node_SEQ", allocationSize = 1)
    private long id;

    private String name;

    @ManyToMany(targetEntity = NodeModel.class)
    private Set<NodeModel> adjNodes;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<NodeModel> getAdjacent() { return adjNodes; }
    public void setAdjacent(Set<NodeModel> adj) { this.adjNodes = adj; }

    public void addAdjacentNode(NodeModel v)
    {
        if (!adjNodes.contains(v))
        {
            adjNodes.add(v);
        }
    }
}
