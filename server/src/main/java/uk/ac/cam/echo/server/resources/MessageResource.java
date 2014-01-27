package uk.ac.cam.echo.server.resources;

import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.models.MessageModel;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@Produces("application/json")
public class MessageResource {
    private Conversation conversation;
    public MessageResource(Conversation conversation) {
        this.conversation = conversation;
    }

    @GET
    public List<Message> getAll() {
        return HibernateUtil.getTransaction().createCriteria(MessageModel.class).list();
    }

    @GET
    @Path("/{messageId}")
    public Message get(@PathParam("messageId") long id) {
        return (Message) HibernateUtil.getTransaction().get(MessageModel.class, id);
    }

    @POST
    public Message create(@FormParam("name") String name, @FormParam("sender") long user_id) {

        User sender = new UserResource().get(user_id);

        MessageModel message = new MessageModel();
        message.setContents(name);
        message.setTimeStamp(new Date().getTime());
        message.setConversation(conversation);
        message.setSender(sender);

        HibernateUtil.getTransaction().save(message);
        return message;
    }

    @DELETE
    @Path("/{messageId}")
    public Response deleteMessage(@PathParam("messageId") long id) {
        Message u = get(id);
        HibernateUtil.getTransaction().delete(u);
        return Response.ok().build();
    }
}
