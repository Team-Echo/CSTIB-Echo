package uk.ac.cam.echo.server.resources;

import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.resources.ConferenceResource;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.analysis.DataAnalyst;
import uk.ac.cam.echo.server.analysis.ServerDataAnalyst;
import uk.ac.cam.echo.server.models.ConferenceModel;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class ConferenceResourceImpl implements ConferenceResource {
    private ServerDataAnalyst analyst = null;

    @Override
    public Conference create(Conference data) {
        HibernateUtil.getTransaction().save(data);
        analyst = new DataAnalyst(data);
        return data;
    }

    public List<Conference> getAll() {
        return HibernateUtil.getTransaction().createCriteria(ConferenceModel.class).list();
    }

    @Override
    public List<Conversation> getConversations(long id) {
        ConferenceModel conf = (ConferenceModel) get(id);
        return new ArrayList<Conversation>(conf.getConversationSet());
    }

    @Override
    public List<Conversation> search(String keyword, int n) {
        throw  new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public List<Conversation> onlyTagSearch(String keyword, int n) {
        return analyst.onlyTagSearch(keyword, n);
    }

    @Override
    public List<Conversation> mostUsers(int n) {
        return analyst.mostUsers(n);
    }

    @Override
    public List<Conversation> mostActiveRecently(long minutes, int n) {
        return analyst.mostActiveRecently(minutes, n);
    }

    @Override
    public List<Conversation> recommend(User user, int n) {
        throw  new UnsupportedOperationException("Not Implemented yet");
    }

    public Conference get(long id) {
        return (Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, id);
    }

    public Response update(Conference conference) {
        HibernateUtil.getTransaction().update(conference);
        return Response.ok().build();
    }

    public Response delete(long id) {
        Conference u = get(id);
        HibernateUtil.getTransaction().delete(u);
        return Response.ok().build();
    }
}
