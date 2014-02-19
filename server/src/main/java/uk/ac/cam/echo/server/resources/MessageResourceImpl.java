package uk.ac.cam.echo.server.resources;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.resources.MessageResource;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.models.MessageModel;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MessageResourceImpl implements MessageResource {
    private Conversation conversation;
    public MessageResourceImpl(Conversation conversation) {
        this.conversation = conversation;
    }

    public List<Message> getAll() {
        return HibernateUtil.getTransaction().createCriteria(MessageModel.class)
                .add(Restrictions.eq("conversation", conversation)).addOrder(Order.asc("timeStamp")).list();
    }

    public List<Message> getRecent(int n) {
        List<Message> ret = HibernateUtil.getTransaction().createCriteria(MessageModel.class)
                .add(Restrictions.eq("conversation", conversation)).addOrder(Order.desc("timeStamp")).setMaxResults(n).list();
        Collections.reverse(ret);
        return ret;
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
        if (sender.getDisplayName() != null) message.setSenderName(sender.getDisplayName());
        else message.setSenderName(sender.getUsername());

        HibernateUtil.getTransaction().save(message);
        ConversationResourceImpl.broadcastMessage(message);
        return message;
    }


    public Response update(Message m) {
        HibernateUtil.getTransaction().update(m);
        return Response.ok().build();
    }

    @Override
    public Message create(Message data) {
        MessageModel msg = (MessageModel) data;
        msg.setTimeStamp(new Date().getTime());

        HibernateUtil.getTransaction().save(msg);
        ConversationResourceImpl.broadcastMessage(msg);
        return msg;
    }

    public Response delete(long id) {
        Message u = get(id);
        HibernateUtil.getTransaction().delete(u);
        return Response.ok().build();
    }
}
