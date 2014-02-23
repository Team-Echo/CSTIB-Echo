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

    @POST
    @Path("/authenticate")
    public User authenticate(@FormParam("username") String username,
                             @FormParam("password") String password);
    @GET
    @Path("/{id}")
    public User get(@PathParam("id") long id);

    @Path("/{id}/interests")
    public InterestResource getInterestResource(@PathParam("id") long id);

    @PUT
    @Consumes("application/json")
    public Response update(User item);

    @POST
    @Consumes("application/json")
    public User create (User data);



}
