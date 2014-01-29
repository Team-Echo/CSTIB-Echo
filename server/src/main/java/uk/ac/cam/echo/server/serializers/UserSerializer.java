package uk.ac.cam.echo.server.serializers;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import uk.ac.cam.echo.data.User;

import java.io.IOException;

public class UserSerializer extends JsonSerializer<User> {
    public UserSerializer() {
    }

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", user.getId());
        jsonGenerator.writeStringField("username", user.getUsername());
        jsonGenerator.writeEndObject();
    }
}
