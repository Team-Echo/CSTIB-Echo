package uk.ac.cam.echo.data.resources;

import uk.ac.cam.echo.data.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Produces("application/json")
public interface TagResource extends RestResource<Tag> {
    @GET
    public Collection<Tag> getAll();

    @GET
    @Path("/{id}")
    public Tag get(@PathParam("id") long id);

    @PUT
    @Consumes("application/json")
    public Response update(Tag item);

    @POST
    @Consumes("application/json")
    public Object create (Tag data);

}
