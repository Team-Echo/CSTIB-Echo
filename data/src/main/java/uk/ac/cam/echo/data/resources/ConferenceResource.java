package uk.ac.cam.echo.data.resources;

import uk.ac.cam.echo.data.Conference;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/conferences")
@Produces("application/json")
public interface ConferenceResource {
    @GET
    public List<Conference> getAll();

    @GET
    @Path("/{conferenceId}")
    public Conference get(@PathParam("conferenceId") long id);

    @POST
    public Conference create(@FormParam("name") String name);

    @DELETE
    @Path("/{conferenceId}")
    public Response deleteConference(@PathParam("conferenceId") long id);
}
