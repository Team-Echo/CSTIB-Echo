package uk.ac.cam.echo.server.serializers;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import uk.ac.cam.echo.server.models.ForceEdgeModel;

import java.io.IOException;

/**
 Author: Petar 'PetarV' Veličković
*/
public class ForceEdgeSerializer extends JsonSerializer<ForceEdgeModel>
{
    public ForceEdgeSerializer() { }

    @Override
    public void serialize(ForceEdgeModel fedge, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException
    {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("sourceId", fedge.getSource().getId());
        jsonGenerator.writeNumberField("destinationId", fedge.getDestination().getId());
        jsonGenerator.writeEndObject();
    }
}
