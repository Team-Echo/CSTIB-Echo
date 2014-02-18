package uk.ac.cam.echo.client;

import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import uk.ac.cam.echo.client.data.BaseData;
import uk.ac.cam.echo.data.async.Handler;
import uk.ac.cam.echo.data.async.Subscription;

import javax.ws.rs.client.WebTarget;

public class ClientSubscription<T> implements Subscription {
    private EventSource source;
    ClientSubscription(ClientApi a, WebTarget target, Handler<T> h, Class<T> c) {
        final Class clazz = c;
        final Handler<T> handle = h;
        final ClientApi api = a;

        source = EventSource.target(target).build();
        source.register(new EventListener() {
            @Override
            public void onEvent(InboundEvent inboundEvent) {
                T data = (T) inboundEvent.readData(clazz);
                if (data instanceof BaseData)
                    ((BaseData) data).setApi(api);
                handle.handle(data);
            }
        });
        source.open();
    }

    @Override
    public void unsubscribe() {
        source.close();
    }
}
