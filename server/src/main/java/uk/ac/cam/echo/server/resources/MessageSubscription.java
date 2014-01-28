package uk.ac.cam.echo.server.resources;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.async.Handler;
import uk.ac.cam.echo.data.async.Subscription;
import uk.ac.cam.echo.data.async.SubscriptionResource;

public class MessageSubscription implements SubscriptionResource<Message> {

    static public SseBroadcaster broadcaster = new SseBroadcaster();

    @Override
    public EventOutput subscribe() {
        final EventOutput event = new EventOutput();
        broadcaster.add(event);
        return event;
    }

    @Override
    public Subscription subscribe(Handler<Message> observer) {
        throw new UnsupportedOperationException();
    }

}
