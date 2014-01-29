package uk.ac.cam.echo.server.resources;

import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.resources.UserResource;
import uk.ac.cam.echo.server.HibernateUtil;
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

    public User create(String username) {
        UserModel user = new UserModel();
        user.setUsername(username);

        HibernateUtil.getTransaction().save(user);
        return user;
    }

    public Response deleteUser(long id) {
        User u = get(id);
        HibernateUtil.getTransaction().delete(u);
        return Response.ok().build();
    }
}
