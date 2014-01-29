package uk.ac.cam.echo.server.resources;

import org.hibernate.Session;
import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.resources.ConferenceResource;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.models.ConferenceModel;

import javax.ws.rs.core.Response;
import java.util.List;

public class ConferenceResourceImpl implements ConferenceResource {
    public List<Conference> getAll() {
        List<Conference> res = HibernateUtil.getTransaction().createCriteria(ConferenceModel.class).list();
        Session s = HibernateUtil.getSession();
        res.get(0).getConversationSet();
        System.out.println(res.size());
        return res;
    }

    public Conference get(long id) {
        return (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, id);
    }

    public Conference create(String name) {
        ConferenceModel conference = new ConferenceModel();
        conference.setName(name);

        HibernateUtil.getTransaction().save(conference);
        return conference;
    }

    public Response deleteConference(long id) {
        Conference u = get(id);
        HibernateUtil.getTransaction().delete(u);
        return Response.ok().build();
    }
}
