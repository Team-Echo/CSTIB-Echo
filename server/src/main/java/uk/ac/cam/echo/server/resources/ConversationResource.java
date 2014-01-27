package uk.ac.cam.echo.server.resources;

import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.models.ConversationModel;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/conversations")
@Produces("application/json")
public class ConversationResource {
    @GET
    public List<Conversation> getAll() {
        return HibernateUtil.getTransaction().createCriteria(ConversationModel.class).list();
    }

    @GET
    @Path("/{conversationId}")
    public Conversation get(@PathParam("conversationId") long id) {
        return (Conversation) HibernateUtil.getTransaction().get(ConversationModel.class, id);
    }

    @Path("/{conversationId}/messages")
    public MessageResource getMessageResource(@PathParam("conversationId") long id) {
        return new MessageResource(get(id));
    }

    @POST
    public Conversation create(@FormParam("name") String name, @FormParam("conference") long conference_id) {
        Conference conf = new ConferenceResource().get(conference_id);

        ConversationModel conversation = new ConversationModel();
        conversation.setName(name);
        conversation.setConference(conf);

        HibernateUtil.getTransaction().save(conversation);
        return conversation;
    }

    @DELETE
    @Path("/{conversationId}")
    public Response deleteConversation(@PathParam("conversationId") long id) {
        Conversation u = get(id);
        HibernateUtil.getTransaction().delete(u);
        return Response.ok().build();
    }
}
