package uk.ac.cam.echo.server.resources;

import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.async.SubscriptionResource;
import uk.ac.cam.echo.data.resources.ConversationResource;
import uk.ac.cam.echo.data.resources.MessageResource;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.models.ConversationModel;

import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;

public class ConversationResourceImpl implements ConversationResource {

    static private IdSubscriptionFactory<Long, Message> messagesSub = new IdSubscriptionFactory<Long, Message>();
    public List<Conversation> getAll() {
        return HibernateUtil.getTransaction().createCriteria(ConversationModel.class).list();
    }

    public Conversation get(long id) {
        return (Conversation) HibernateUtil.getTransaction().get(ConversationModel.class, id);
    }

    public MessageResource getMessageResource(long id) {
        return new MessageResourceImpl(get(id));
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
