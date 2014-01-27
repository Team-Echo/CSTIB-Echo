package uk.ac.cam.echo.server.resources;

import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.models.UserModel;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users")
@Produces("application/json")
public class UserResource {
    @GET
    public List<User> getAll() {
        return HibernateUtil.getTransaction().createCriteria(UserModel.class).list();
    }

    @GET
    @Path("/{userId}")
    public User get(@PathParam("userId") long id) {
        return (User) HibernateUtil.getTransaction().get(UserModel.class, id);
    }

    @POST
    public User create(@FormParam("username") String username) {
        UserModel user = new UserModel();
        user.setUsername(username);

        HibernateUtil.getTransaction().save(user);
        return user;
    }

    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") long id) {
        User u = get(id);
        HibernateUtil.getTransaction().delete(u);
        return Response.ok().build();
    }
}
