package uk.ac.cam.echo.server.resources;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.resources.InterestResource;
import uk.ac.cam.echo.data.resources.UserResource;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.models.ConversationModel;
import uk.ac.cam.echo.server.models.UserModel;

import javax.ws.rs.core.Response;
import java.util.List;

public class UserResourceImpl implements UserResource {
    public List<User> getAll() {
        return HibernateUtil.getTransaction().createCriteria(UserModel.class).list();
    }

    public User get(long id) {
        return (User) HibernateUtil.getTransaction().get(UserModel.class, id);
    }

    public InterestResource getInterestResource(long id) {
        return new InterestResourceImpl(get(id));
    }

    public User create(String username, Long conversationId) {
        Session session = HibernateUtil.getTransaction();
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setCurrentConversation((Conversation) session.load(ConversationModel.class, conversationId));
        session.save(user);
        return user;
    }

    public User create(User user) {
        HibernateUtil.getTransaction().save(user);
        return user;
    }

    public User authenticate(String username, String password) {
       User user = (User) HibernateUtil.getTransaction().createCriteria(UserModel.class)
                                      .add(Restrictions.eq("username", username))
                                      .add(Restrictions.eq("hashedPassword", UserModel.hashPassword(password)))
                                      .uniqueResult();

        return user;
    }

    public Response update(User item) {
        HibernateUtil.getTransaction().update(item);
        return Response.ok().build();
    }

    public Response delete(long id) {
        User u = get(id);
        HibernateUtil.getTransaction().delete(u);
        return Response.ok().build();
    }
}
