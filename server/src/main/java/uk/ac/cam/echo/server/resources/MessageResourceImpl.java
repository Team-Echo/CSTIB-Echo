package uk.ac.cam.echo.server.resources;

import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.resources.MessageResource;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.models.MessageModel;

import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

public class MessageResourceImpl implements MessageResource {
    private Conversation conversation;
    public MessageResourceImpl(Conversation conversation) {
        this.conversation = conversation;
    }

    public List<Message> getAll() {
        return HibernateUtil.getTransaction().createCriteria(MessageModel.class).list();
    }

    public Message get(long id) {
        return (Message) HibernateUtil.getTransaction().get(MessageModel.class, id);
    }

    public Message create(String name, long user_id) {

        User sender = new UserResourceImpl().get(user_id);

        MessageModel message = new MessageModel();
        message.setContents(name);
        message.setTimeStamp(new Date().getTime());
        message.setConversation(conversation);
        message.setSender(sender);

        HibernateUtil.getTransaction().save(message);
        return message;
    }

    public Response deleteMessage(long id) {
        Message u = get(id);
        HibernateUtil.getTransaction().delete(u);
        return Response.ok().build();
    }
}
