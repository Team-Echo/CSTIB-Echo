package uk.ac.cam.echo.server.serializers;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.hibernate.proxy.HibernateProxy;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;

import java.io.IOException;


public class MessageSerializer extends JsonSerializer<Message> {
    public MessageSerializer() {
    }

    @Override
    public void serialize(Message message, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", message.getId());
        jsonGenerator.writeStringField("contents", message.getContents());
        jsonGenerator.writeNumberField("timeStamp", message.getTimeStamp());
        if (message.getSender() != null) {
             User user = message.getSender();
             if (user instanceof HibernateProxy) {
                 user = (User) ((HibernateProxy) user).getHibernateLazyInitializer().getImplementation();
             }
             jsonGenerator.writeObjectField("sender", user);
        }
             //jsonGenerator.writeNumberField("senderId", message.getSender().getId());
        jsonGenerator.writeStringField("senderName", message.getSenderName());
        jsonGenerator.writeNumberField("conversationId", message.getConversation().getId());
        jsonGenerator.writeEndObject();
    }
}
