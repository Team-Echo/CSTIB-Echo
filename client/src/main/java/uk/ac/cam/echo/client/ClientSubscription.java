package uk.ac.cam.echo.client;

import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import uk.ac.cam.echo.data.async.Handler;
import uk.ac.cam.echo.data.async.Subscription;

import javax.ws.rs.client.WebTarget;

public class ClientSubscription<T> implements Subscription {
    private EventSource source;
    ClientSubscription(WebTarget target, Handler<T> h, Class<T> c) {
        final Class clazz = c;
        final Handler<T> handle = h;
        source = EventSource.target(target).build();
        source.register(new EventListener() {
            @Override
            public void onEvent(InboundEvent inboundEvent) {
                handle.handle((T) inboundEvent.readData(clazz));
            }
        });
        source.open();
    }

    @Override
    public void unsubscribe() {
        source.close();
    }
}
