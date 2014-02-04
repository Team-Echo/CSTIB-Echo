package uk.ac.cam.echo.data.resources;

import uk.ac.cam.echo.data.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users")
@Produces("application/json")
public interface UserResource extends RestResource<User>{
    @GET
    public List<User> getAll();

    @GET
    @Path("/{id}")
    public User get(@PathParam("id") long id);

    @PUT
    @Consumes("application/json")
    public Response update(User item);

    @POST
    @Consumes("application/json")
    public User create (User data);
}
