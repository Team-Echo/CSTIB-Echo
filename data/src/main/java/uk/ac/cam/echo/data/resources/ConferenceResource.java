package uk.ac.cam.echo.data.resources;

import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/conferences")
@Produces("application/json")
public interface ConferenceResource extends RestResource<Conference> {
    @GET
    public List<Conference> getAll();

    @GET
    @Path("/{id}")
    public Conference get(@PathParam("id") long id);

    @Path("/search")
    @GET
    public List<Conversation> search(@QueryParam("id") long id, @QueryParam("keyword") String keyword, @QueryParam("num") int n);

    @Path("/tag-search")
    @GET
    public List<Conversation> onlyTagSearch(@QueryParam("id") long id, @QueryParam("keyword") String keyword, @QueryParam("num") int n);

    @Path("/most-users")
    @GET
    public List<Conversation> mostUsers(@QueryParam("id") long id, @QueryParam("num") int n);

    @Path("/most-active")
    @GET
    public List<Conversation> mostActiveRecently(@QueryParam("id") long id, @QueryParam("minutes") long minutes, @QueryParam("num") int n);

    @Path("/recommend")
    @GET
    public List<Conversation> recommend(@QueryParam("id") long id, @QueryParam("uid") long userID, @QueryParam("num") int n);

    @GET
    @Path("/{conferenceId}/conversations")
    public List<Conversation> getConversations(@PathParam("conferenceId") long id);


    @PUT
    @Consumes("application/json")
    public Response update(Conference item);


    @POST
    @Consumes("application/json")
    public Object create (Conference data);

}
