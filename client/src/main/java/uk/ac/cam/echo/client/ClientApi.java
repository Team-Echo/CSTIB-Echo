package uk.ac.cam.echo.client;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import uk.ac.cam.echo.client.data.ConferenceData;
import uk.ac.cam.echo.client.data.ConversationData;
import uk.ac.cam.echo.client.data.MessageData;
import uk.ac.cam.echo.client.data.UserData;
import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.resources.ConferenceResource;
import uk.ac.cam.echo.data.resources.ConversationResource;
import uk.ac.cam.echo.data.resources.MessageResource;
import uk.ac.cam.echo.data.resources.UserResource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.Date;
import java.util.List;


public class ClientApi {
    private JacksonJaxbJsonProvider getJacksonProvider() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule testModule = new SimpleModule("testModule", new Version(1,0,0,null))
                .addAbstractTypeMapping(Conference.class, ConferenceData.class)
                .addAbstractTypeMapping(Conversation.class, ConversationData.class)
                .addAbstractTypeMapping(Message.class, MessageData.class)
                .addAbstractTypeMapping(User.class, UserData.class);

        mapper.registerModule(testModule);

        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(mapper);
        return provider;
    }

    public UserResource userResource;
    public ConferenceResource conferenceResource;
    public ConversationResource conversationResource;

    ClientApi(String address) {
        //Target t = ClientFactory.newClient().target(address);

        Client c = ClientBuilder.newClient();
        c.register(getJacksonProvider());

        WebTarget server = c.target(address);

        userResource = WebResourceFactory.newResource(UserResource.class, server);
        conferenceResource = WebResourceFactory.newResource(ConferenceResource.class, server);
        conversationResource = WebResourceFactory.newResource(ConversationResource.class, server);
    }

    public static void main(String[] args) {
        ClientApi api = new ClientApi("http://localhost:8080");

        List<Message> msgs = api.conversationResource.getMessageResource(1).getAll();

        for (Message m: msgs) {
            System.out.println(m.getContents());
        }

        MessageResource msg = api.conversationResource.getMessageResource(1);
        Message f = msg.create("working message " + new Date().getTime(), 2);

        msgs = api.conversationResource.getMessageResource(1).getAll();
        msg.create("Woohoo works", 2);

        for (Message m: msgs) {
            System.out.println(m.getContents());
        }
    }
}
