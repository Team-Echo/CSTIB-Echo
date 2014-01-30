package uk.ac.cam.echo.server;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;
import uk.ac.cam.echo.server.filters.HibernateRequestFilter;
import uk.ac.cam.echo.server.filters.HibernateResponseFilter;
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
        int port = Integer.valueOf(System.getenv("PORT"));
        return UriBuilder.fromUri("http://0.0.0.0/").port(port).build();
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

        rc.register(JacksonFeature.class);
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

