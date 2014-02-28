package uk.ac.cam.echo.server.serializers;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import uk.ac.cam.echo.server.models.ForceNodeModel;

import java.io.IOException;

/**
 Author: Petar 'PetarV' Veličković
*/
public class ForceNodeSerializer extends JsonSerializer<ForceNodeModel>
{
    public ForceNodeSerializer() { }

    @Override
    public void serialize(ForceNodeModel fnode, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException
    {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", fnode.getId());
        jsonGenerator.writeStringField("name", fnode.getName());
        jsonGenerator.writeNumberField("type", fnode.getType());
        jsonGenerator.writeNumberField("internalId", fnode.getInternalId());
        jsonGenerator.writeEndObject();
    }
}
