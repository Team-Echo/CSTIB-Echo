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
    @Path("/{messageId}")
    public Message get(@PathParam("messageId") long id);

    @POST
    public Message create(@FormParam("name") String name, @FormParam("sender") long user_id);

    @DELETE
    @Path("/{messageId}")
    public Response delete(@PathParam("messageId") long id);

    @PUT
    @Consumes("application/json")
    public Response update(Message m);
}
