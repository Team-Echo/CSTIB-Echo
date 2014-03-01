package uk.ac.cam.echo.server.models;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import uk.ac.cam.echo.server.serializers.ForceEdgeSerializer;
import uk.ac.cam.echo.server.serializers.ForceNodeSerializer;

import javax.persistence.*;
import java.util.Map;

/**
 Author: Petar 'PetarV' Veličković

 A graph force edge model that will be put in permanent store.
*/
@JsonSerialize(using=ForceEdgeSerializer.class)
@Entity
@Table(name="FEdgesF")
public class ForceEdgeModel extends BaseModel
{
    public ForceEdgeModel() { }

    private static String[] allowed = {"source", "destination"};
    @JsonCreator
    public ForceEdgeModel(Map<String, Object> props)
    {
        super(props, allowed);
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="FEdgeIdSeq")
    @SequenceGenerator(name="FEdgeIdSeq", sequenceName="FEdge_SEQ", allocationSize = 1)
    private long id;

    @ManyToOne(targetEntity = ForceNodeModel.class)
    private ForceNodeModel source;
    private ForceNodeModel destination;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public ForceNodeModel getSource() { return source; }
    public void setSource(ForceNodeModel src) { this.source = src; }

    public ForceNodeModel getDestination() { return destination; }
    public void setDestination(ForceNodeModel dst) { this.destination = dst; }
}
