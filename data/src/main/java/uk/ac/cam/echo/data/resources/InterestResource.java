package uk.ac.cam.echo.data.resources;

import uk.ac.cam.echo.data.Interest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Produces("application/json")
public interface InterestResource extends RestResource<Interest>{

    @GET
    public Collection<Interest> getAll();

    @GET
    @Path("/{id}")
    public Interest get(@PathParam("id") long id);

    @PUT
    @Consumes("application/json")
    public Response update(Interest item);

    @POST
    @Consumes("application/json")
    public Object create (Interest data);
}
