package uk.ac.cam.echo.server.serializers;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import uk.ac.cam.echo.data.Interest;

import java.io.IOException;

/**
 Author: Petar 'PetarV' Veličković
*/
public class InterestSerializer extends JsonSerializer<Interest>
{
    public InterestSerializer() { }

    @Override
    public void serialize(Interest interest, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException
    {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", interest.getId());
        jsonGenerator.writeStringField("name", interest.getName());
        if (interest.getUser() != null)
        {
            jsonGenerator.writeNumberField("userId", interest.getUser().getId());
        }
        jsonGenerator.writeEndObject();
    }
}
