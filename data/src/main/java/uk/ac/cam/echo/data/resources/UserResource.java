package uk.ac.cam.echo.data.resources;

import uk.ac.cam.echo.data.User;

import javax.ws.rs.*;
import java.util.List;

@Path("/users")
@Produces("application/json")
public interface UserResource extends RestResource<User>{
    @GET
    public List<User> getAll();

    @POST
    public User create(@FormParam("username") String username, @FormParam("currentConversationId") Long id);
}
