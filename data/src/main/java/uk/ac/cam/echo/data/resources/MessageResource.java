package uk.ac.cam.echo.data.resources;

import uk.ac.cam.echo.data.Message;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import java.util.List;

@Produces("application/json")
public interface MessageResource extends RestResource<Message>{

    @GET
    public List<Message> getAll();

    @POST
    public Message create(@FormParam("name") String name, @FormParam("sender") long user_id);
}
