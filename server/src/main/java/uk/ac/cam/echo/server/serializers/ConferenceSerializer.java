package uk.ac.cam.echo.server.serializers;


import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import uk.ac.cam.echo.data.Conference;

import java.io.IOException;

public class ConferenceSerializer extends JsonSerializer<Conference> {
    public ConferenceSerializer() {
    }

    @Override
    public void serialize(Conference conference, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", conference.getId());
        jsonGenerator.writeStringField("name", conference.getName());
        jsonGenerator.writeEndObject();
    }
}
