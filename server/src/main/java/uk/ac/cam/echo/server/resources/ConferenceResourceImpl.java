package uk.ac.cam.echo.server.resources;

import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.resources.ConferenceResource;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.models.ConferenceModel;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class ConferenceResourceImpl implements ConferenceResource {
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
        throw  new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public List<Conversation> mostUsers(int n) {
        throw  new UnsupportedOperationException("Not Implemented yet");
    }

    @Override
    public List<Conversation> mostActiveRecently(int minutes, int n) {
        throw  new UnsupportedOperationException("Not Implemented yet");
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
