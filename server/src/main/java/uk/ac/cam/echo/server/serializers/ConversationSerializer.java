package uk.ac.cam.echo.server.serializers;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import uk.ac.cam.echo.data.Conversation;

import java.io.IOException;

public class ConversationSerializer extends JsonSerializer<Conversation> {
    public ConversationSerializer() {
    }

    @Override
    public void serialize(Conversation conversation, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", conversation.getId());
        jsonGenerator.writeStringField("name", conversation.getName());
        jsonGenerator.writeEndObject();
    }
}
