package uk.ac.cam.echo.server.models;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import uk.ac.cam.echo.server.serializers.ForceNodeSerializer;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

/**
 Author: Petar 'PetarV' Veličković

 A graph force node model that will be put in permanent store.
*/
@JsonSerialize(using=ForceNodeSerializer.class)
@Entity
@Table(name="FNodesF")
public class ForceNodeModel extends BaseModel
{
    public ForceNodeModel() { }

    private static String allowed[] = {"name", "type", "internalId"};
    @JsonCreator
    public ForceNodeModel(Map<String, Object> props)
    {
        super(props, allowed);
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="FNodeIdSeq")
    @SequenceGenerator(name="FNodeIdSeq", sequenceName="FNode_SEQ", allocationSize = 1)
    private long id;

    private String name;
    private long type;
    private long internalId;

    @OneToMany(targetEntity = ForceEdgeModel.class, mappedBy = "source")
    private Set<ForceEdgeModel> adjNodes;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public long getType()
    {
        return type;
    }
    public void setType(long type)
    {
        this.type = type;
    }

    public long getInternalId() { return internalId; }
    public void setInternalId(long iid) { this.internalId = iid; }

    public Set<ForceEdgeModel> getAdjacent() { return adjNodes; }
    public void setAdjacent(Set<ForceEdgeModel> adj) { this.adjNodes = adj; }

    public void addAdjacentNode(ForceEdgeModel v)
    {
        if (!adjNodes.contains(v))
        {
            adjNodes.add(v);
        }
    }
}
