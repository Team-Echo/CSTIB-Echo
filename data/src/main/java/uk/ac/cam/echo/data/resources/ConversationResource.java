package uk.ac.cam.echo.data.resources;

import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.async.SubscriptionResource;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/conversations")
@Produces("application/json")
public interface ConversationResource extends RestResource<Conversation> {
    @GET
    public Collection<Conversation> getAll();

    @GET
    @Path("/{conversationId}")
    public Conversation get(@PathParam("conversationId") long id);

    @GET
    @Path("/{conversationId}/users")
    public Collection<User> getUsers(@PathParam("conversationId") long id);

    @Path("/{conversationId}/messageSubscription")
    SubscriptionResource<Message> listenToMessages(@PathParam("conversationId") long id);

    @Path("/{conversationId}/messages")
    public MessageResource getMessageResource(@PathParam("conversationId") long id);

    @POST
    public Conversation create(@FormParam("name") String name, @FormParam("conference") long conference_id);

}
