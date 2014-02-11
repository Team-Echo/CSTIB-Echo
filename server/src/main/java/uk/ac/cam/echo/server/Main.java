package uk.ac.cam.echo.server;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;
import uk.ac.cam.echo.data.*;
import uk.ac.cam.echo.server.filters.HibernateRequestFilter;
import uk.ac.cam.echo.server.filters.HibernateResponseFilter;
import uk.ac.cam.echo.server.filters.JacksonWithHibernateJsonProvider;
import uk.ac.cam.echo.server.models.*;
import uk.ac.cam.echo.server.resources.ConferenceResourceImpl;
import uk.ac.cam.echo.server.resources.ConversationResourceImpl;
import uk.ac.cam.echo.server.resources.MessageResourceImpl;
import uk.ac.cam.echo.server.resources.UserResourceImpl;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static String BASE_URI = "http://localhost:8080/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static URI getUri() {
        int port;
        try {
            port = Integer.valueOf(System.getenv("PORT"));
        } catch (Exception e) {
            port = 8080;
        }
        return UriBuilder.fromUri("http://0.0.0.0/").port(port).build();
    }

    private static JacksonJaxbJsonProvider getJsonProvider() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule testModule = new SimpleModule("testModule", new Version(1,0,0,null))
                .addAbstractTypeMapping(Conference.class, ConferenceModel.class)
                .addAbstractTypeMapping(Conversation.class, ConversationModel.class)
                .addAbstractTypeMapping(Message.class, MessageModel.class)
                .addAbstractTypeMapping(Tag.class, TagModel.class)
                .addAbstractTypeMapping(User.class, UserModel.class);


        mapper.registerModule(testModule);

        JacksonJaxbJsonProvider provider = new JacksonWithHibernateJsonProvider();
        provider.setMapper(mapper);
        return provider;
    }

    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in uk.ac.cam.echo package
        HibernateUtil.getSessionFactory();
        final ResourceConfig rc = new ResourceConfig().packages("uk.ac.cam.echo.server");

        rc.register(UserResourceImpl.class);
        rc.register(MessageResourceImpl.class);
        rc.register(ConversationResourceImpl.class);
        rc.register(ConferenceResourceImpl.class);

        rc.register(HibernateRequestFilter.class);
        rc.register(HibernateResponseFilter.class);
        final String disableMoxy = CommonProperties.MOXY_JSON_FEATURE_DISABLE + '.'
                + rc.getRuntimeType().name().toLowerCase();
        rc.property(disableMoxy, true);
        rc.register(getJsonProvider());


        rc.register(LoggingFilter.class);
        rc.register(SseFeature.class);
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(getUri(), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        while(true) {
            System.in.read();
        }
    }
}

