package uk.ac.cam.echo.client;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.glassfish.jersey.media.sse.SseFeature;
import uk.ac.cam.echo.client.data.*;
import uk.ac.cam.echo.data.*;
import uk.ac.cam.echo.data.resources.ConferenceResource;
import uk.ac.cam.echo.data.resources.ConversationResource;
import uk.ac.cam.echo.data.resources.UserResource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;


public class ClientApi {
    private static JacksonJaxbJsonProvider getJacksonProvider() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule testModule = new SimpleModule("testModule", new Version(1,0,0,null))
                .addAbstractTypeMapping(Conference.class, ConferenceData.class)
                .addAbstractTypeMapping(Conversation.class, ConversationData.class)
                .addAbstractTypeMapping(Message.class, MessageData.class)
                .addAbstractTypeMapping(User.class, UserData.class)
                .addAbstractTypeMapping(Interest.class, InterestData.class)
                .addAbstractTypeMapping(Tag.class, TagData.class);

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
    public ClientApi(String address) {
        client = ClientBuilder.newClient();
        client.register(getJacksonProvider());
        client.register(SseFeature.class);
//        client.register(LoggingFilter.class);

        server = client.target(address);

        userResource = (UserResource) ResourceFactory.newResource(UserResource.class, this);
        conferenceResource = (ConferenceResource) ResourceFactory.newResource(ConferenceResource.class, this);
        conversationResource = (ConversationResource) ResourceFactory.newResource(ConversationResource.class, this);
    }

    public Message newMessage(Conversation conversation) {
        MessageData res = new MessageData();
        res.setConversation(conversation);
        res.setApi(this);
        return res;
    }

    public Conversation newConversation() {
        ConversationData res = new ConversationData();
        res.setApi(this);
        return res;
    }

    public Conference newConference() {
        ConferenceData res = new ConferenceData();
        res.setApi(this);
        return res;
    }

    public Tag newTag(Conversation conversation) {
        TagData tag = new TagData();
        tag.setApi(this);
        tag.configureResource(conversation.getId());

        return tag;
    }

    public Interest newInterest() {
        InterestData interest = new InterestData();
        interest.setApi(this);

        return interest;
    }

    public User newUser() {
        UserData res = new UserData();
        res.setApi(this);
        return res;
    }

    public Client getClient() {
        return client;
    }

    public WebTarget getServer() {
        return server;
    }

}
