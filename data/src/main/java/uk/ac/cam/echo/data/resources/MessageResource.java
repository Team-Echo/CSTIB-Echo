package uk.ac.cam.echo.data.resources;

import uk.ac.cam.echo.data.Message;

import javax.ws.rs.*;
import java.util.List;

@Produces("application/json")
public interface MessageResource extends RestResource<Message>{

    @GET
    public List<Message> getAll();

    @GET
    @Path("/{id}")
    public Message get(@PathParam("id") long id);

    @POST
    public Message create(@FormParam("name") String name, @FormParam("sender") long user_id);
}
