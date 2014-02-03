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
    @Path("/{userId}")
    public User get(@PathParam("userId") long id);

    @POST
    public User create(@FormParam("username") String username, @FormParam("currentConversationId") Long id);

    @DELETE
    @Path("/{userId}")
    public Response delete(@PathParam("userId") long id);
}
