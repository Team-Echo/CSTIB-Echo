package uk.ac.cam.echo.server.resources;

import uk.ac.cam.echo.data.Interest;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.resources.InterestResource;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.models.InterestModel;

import javax.ws.rs.core.Response;
import java.util.Collection;

public class InterestResourceImpl implements InterestResource {

    private User user;

    public InterestResourceImpl(User user) {
        this.user = user;
    }

    public Collection<Interest> getAll() {
        return user.getInterests();
    }

    public Interest get(long id) {
        return (Interest) HibernateUtil.getTransaction().get(InterestModel.class, id);
    }

    public Response delete(long id) {
        HibernateUtil.getTransaction().delete(get(id));
        return Response.ok().build();
    }

    public Response update(Interest m) {
        HibernateUtil.getTransaction().update(m);
        return Response.ok().build();
    }

    public Interest create(Interest data) {
        data.setUser(this.user);
        HibernateUtil.getTransaction().save(data);

        return data;
    }
}
