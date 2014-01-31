package uk.ac.cam.echo.client;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.glassfish.jersey.media.sse.SseFeature;
import uk.ac.cam.echo.client.data.ConferenceData;
import uk.ac.cam.echo.client.data.ConversationData;
import uk.ac.cam.echo.client.data.MessageData;
import uk.ac.cam.echo.client.data.UserData;
import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.async.Handler;
import uk.ac.cam.echo.data.async.Subscription;
import uk.ac.cam.echo.data.resources.ConferenceResource;
import uk.ac.cam.echo.data.resources.ConversationResource;
import uk.ac.cam.echo.data.resources.MessageResource;
import uk.ac.cam.echo.data.resources.UserResource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;


public class ClientApi {
    private static JacksonJaxbJsonProvider getJacksonProvider() {
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


    private Client client;
    private WebTarget server;
    ClientApi(String address) {
        client = ClientBuilder.newClient();
        client.register(getJacksonProvider());
        client.register(SseFeature.class);
        //client.register(LoggingFilter.class);

        server = client.target(address);

        userResource = (UserResource) ResourceFactory.newResource(UserResource.class, this);
        conferenceResource = (ConferenceResource) ResourceFactory.newResource(ConferenceResource.class, this);
        conversationResource = (ConversationResource) ResourceFactory.newResource(ConversationResource.class, this);
    }

    public Client getClient() {
        return client;
    }

    public WebTarget getServer() {
        return server;
    }

    public static void main(String[] args) {
        ClientApi api = new ClientApi("http://localhost:8080");
        MessageResource msgRes = api.conversationResource.getMessageResource(1);

        List<Message> msgs = msgRes.getAll();

        for (Message m: msgs) {
            System.out.println(m.getContents());
        }

        Handler<Message> test = new Handler<Message>(){
            public void handle(Message arg) {
               System.out.println(arg.getContents());
            }
        };

        Subscription happy = api.conversationResource.listenToMessages(1).subscribe(test);
        while(true) {
            try {
                String input = new DataInputStream(new BufferedInputStream(System.in)).readLine();
                if (input != null)
                    msgRes.create(input, 2);
                if (input.equals("quit")) {
                    happy.unsubscribe();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }
}
