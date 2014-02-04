package uk.ac.cam.echo.data.resources;

import uk.ac.cam.echo.data.Message;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Produces("application/json")
public interface MessageResource extends RestResource<Message>{

    @GET
    public List<Message> getAll();

    @GET
    @Path("/{id}")
    public Message get(@PathParam("id") long id);

    @PUT
    @Consumes("application/json")
    public Response update(Message item);

    @POST
    @Consumes("application/json")
    public Object create (Message data);
}
