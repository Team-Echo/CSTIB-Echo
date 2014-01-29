package uk.ac.cam.echo.server.resources;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import uk.ac.cam.echo.data.async.Handler;
import uk.ac.cam.echo.data.async.Subscription;
import uk.ac.cam.echo.data.async.SubscriptionResource;

import javax.ws.rs.core.MediaType;

public class SubscriptionResourceImpl<T> implements SubscriptionResource<T>{
    private SseBroadcaster broadcaster;
    public SubscriptionResourceImpl() {
        broadcaster = new SseBroadcaster();
    }

    @Override
    public EventOutput subscribe() {
        final EventOutput event = new EventOutput();
        broadcaster.add(event);
        return event;
    }

    public Subscription subscribe(Handler<T> observer) {
        throw new UnsupportedOperationException();
    }

    public void broadcast(T data) {
        OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
        OutboundEvent event = eventBuilder.mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(data)
                .build();
        broadcaster.broadcast(event);
    }
}
