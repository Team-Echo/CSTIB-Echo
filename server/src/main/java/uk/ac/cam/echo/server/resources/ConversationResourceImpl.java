package uk.ac.cam.echo.server.resources;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.async.SubscriptionResource;
import uk.ac.cam.echo.data.resources.ConversationResource;
import uk.ac.cam.echo.data.resources.MessageResource;
import uk.ac.cam.echo.data.resources.TagResource;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.models.ConversationModel;

import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;

public class ConversationResourceImpl implements ConversationResource {

    static private IdSubscriptionFactory<Long, Message> messagesSub = new IdSubscriptionFactory<Long, Message>();
    public List<Conversation> getAll() {
        return HibernateUtil.getTransaction().createCriteria(ConversationModel.class).addOrder(Order.asc("id")).list();
    }

    public Conversation get(long id) {
        return (Conversation) HibernateUtil.getTransaction().get(ConversationModel.class, id);
    }

    public Conversation get(String name) {
        return (Conversation) HibernateUtil.getTransaction().createCriteria(ConversationModel.class)
                .add(Restrictions.eq("name", name)).addOrder(Order.desc("id")).list().get(0);
    }


    public MessageResource getMessageResource(long id) {
        return new MessageResourceImpl(get(id));
    }

    @Override
    public TagResource getTagResource(long id) {
        return new TagResourceImpl(get(id));
    }

    public Conversation create(String name, long conference_id) {
        Conference conf = new ConferenceResourceImpl().get(conference_id);

        ConversationModel conversation = new ConversationModel();
        conversation.setName(name);
        conversation.setConference(conf);

        HibernateUtil.getTransaction().save(conversation);
        return conversation;
    }



    public Response delete(long id) {
        Conversation u = get(id);
        HibernateUtil.getTransaction().delete(u);
        return Response.ok().build();
    }

    public Response update(Conversation conversation) {
        HibernateUtil.getTransaction().update(conversation);
        return Response.ok().build();
    }

    @Override
    public Conversation create(Conversation data) {
        HibernateUtil.getTransaction().save(data);
        return data;
    }

    public static void broadcastMessage(Message m) {
        messagesSub.get(m.getConversation().getId()).broadcast(m);
    }

    public SubscriptionResource listenToMessages(long id) {
        return messagesSub.get(id);
    }

    @Override
    public Collection<User> getUsers(long id) {
        return get(id).getUsers();
    }
}
