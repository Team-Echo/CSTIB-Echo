package uk.ac.cam.echo.data.async;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseFeature;
import uk.ac.cam.echo.data.resources.Resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

public interface SubscriptionResource<T> extends Resource {

    @Path("/")
    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    EventOutput subscribe();

    Subscription subscribe(Handler<T> observer);


}
