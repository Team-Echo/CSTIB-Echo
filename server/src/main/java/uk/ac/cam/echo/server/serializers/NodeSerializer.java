package uk.ac.cam.echo.server.serializers;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import uk.ac.cam.echo.server.models.NodeModel;

import java.io.IOException;

/**
 Author: Petar 'PetarV' Veličković
*/
public class NodeSerializer extends JsonSerializer<NodeModel>
{
    public NodeSerializer() { }

    @Override
    public void serialize(NodeModel node, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException
    {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", node.getId());
        jsonGenerator.writeStringField("name", node.getName());
        jsonGenerator.writeEndObject();
    }
}
