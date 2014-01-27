package uk.ac.cam.echo.server.resources;

import org.hibernate.Session;
import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.models.ConferenceModel;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/conferences")
@Produces("application/json")
public class ConferenceResource {
    @GET
    public List<Conference> getAll() {
        List<Conference> res = HibernateUtil.getTransaction().createCriteria(ConferenceModel.class).list();
        Session s = HibernateUtil.getSession();
        res.get(0).getConversationSet();
        System.out.println(res.size());
        return res;
    }

    @GET
    @Path("/{conferenceId}")
    public Conference get(@PathParam("conferenceId") long id) {
        return (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, id);
    }

    @POST
    public Conference create(@FormParam("name") String name) {
        ConferenceModel conference = new ConferenceModel();
        conference.setName(name);

        HibernateUtil.getTransaction().save(conference);
        return conference;
    }

    @DELETE
    @Path("/{conferenceId}")
    public Response deleteConference(@PathParam("conferenceId") long id) {
        Conference u = get(id);
        HibernateUtil.getTransaction().delete(u);
        return Response.ok().build();
    }
}
