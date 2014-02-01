package uk.ac.cam.echo.data.resources;

import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/conferences")
@Produces("application/json")
public interface ConferenceResource extends RestResource<Conference> {
    @GET
    public List<Conference> getAll();

    @Path("/search")
    @GET
    public List<Conversation> search(@QueryParam("keyword") String keyword, @QueryParam("num") int n);

    @Path("/tag-search")
    @GET
    public List<Conversation> onlyTagSearch(@QueryParam("keyword") String keyword, @QueryParam("num") int n);

    @Path("/most-users")
    @GET
    public List<Conversation> mostUsers(@QueryParam("num") int n);

    @Path("/most-active")
    @GET
    public List<Conversation> mostActiveRecently(@QueryParam("minutes") int minutes, @QueryParam("num") int n);

    @GET
    @Path("/{conferenceId}")
    public Conference get(@PathParam("conferenceId") long id);

    @POST
    public Conference create(@FormParam("name") String name);

    @DELETE
    @Path("/{conferenceId}")
    public Response deleteConference(@PathParam("conferenceId") long id);
}
