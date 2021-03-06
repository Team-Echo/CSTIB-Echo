package uk.ac.cam.echo.server.resources;

import org.hibernate.criterion.Restrictions;
import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.resources.ConferenceResource;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.analysis.internal.ForceGraphUtil;
import uk.ac.cam.echo.server.analysis.internal.GraphUtil;
import uk.ac.cam.echo.server.analysis.internal.GraphUtil2;
import uk.ac.cam.echo.server.analysis.internal.NPForceGraph;
import uk.ac.cam.echo.server.models.ConferenceModel;
import uk.ac.cam.echo.server.models.ConversationModel;
import uk.ac.cam.echo.server.models.ForceNodeModel;
import uk.ac.cam.echo.server.models.UserModel;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConferenceResourceImpl implements ConferenceResource {

    @Override
    public Conference create(Conference data) {
        HibernateUtil.getTransaction().save(data);

        return data;
    }

    public List<Conference> getAll() {
        return HibernateUtil.getTransaction().createCriteria(ConferenceModel.class).list();
    }

    @Override
    public Map<String, Long> getKeywords(long id, long conversationId, long lastTS) {
        Conversation conversation = (Conversation) HibernateUtil.getTransaction().get(ConversationModel.class, conversationId);
        return AnalystFactory.get(id).getKeywords(conversation, lastTS);
    }

    @Override
    public Map<String, Long> getKeywords(long id, long lastTS) {
        return AnalystFactory.get(id).getKeywords(lastTS);
    }

    @Override
    public String getKeywordGraph(long id) {
        AnalystFactory.get(id).updateGraph();
        return GraphUtil.getJSONGraph();
    }

    @Override
    public String getKeywordGraph(long id, int lim) {
        GraphUtil2.flush();
        AnalystFactory.get(id).updateGraph(lim);
        return GraphUtil2.getJSONGraph();
    }

    @Override
    public String getForceGraph(long id) {
        NPForceGraph.flush();
        AnalystFactory.get(id).updateFGraph();
        return NPForceGraph.getJSONFGraph();
    }

    @Override
    public List<Object> getForceNodes(long id) {
        return new ArrayList<Object>(ForceGraphUtil.getAll());
    }

    @Override
    public List<Object> getGraphNodes(long id) {
        return new ArrayList<Object>(GraphUtil.getAll());
    }

    @Override
    public List<Object> findForceNode(long id, long type, long iid) {
        return new ArrayList<Object>(HibernateUtil.getTransaction().createCriteria(ForceNodeModel.class)
                .add(Restrictions.eq("type", type))
                .add(Restrictions.eq("internalId", iid)).list());
    }

    @Override
    public List<Conversation> getConversations(long id) {
        ConferenceModel conf = (ConferenceModel) get(id);
        return new ArrayList<Conversation>(conf.getConversationSet());
    }

    @Override
    public List<Conversation> search(long id, String keyword, int n) {
        return AnalystFactory.get(id).search(keyword, n);
    }

    @Override
    public List<Conversation> onlyKeywordSearch(long id, String keyword, int n) {
        return AnalystFactory.get(id).onlyKeywordSearch(keyword, n);
    }

    @Override
    public List<Conversation> onlyTagSearch(long id, String keyword, int n) {
        return AnalystFactory.get(id).onlyTagSearch(keyword, n);
    }

    @Override
    public List<Conversation> onlyNameSearch(long id, String keyword, int n) {
        return AnalystFactory.get(id).onlyNameSearch(keyword, n);
    }

    @Override
    public List<Conversation> nameAndTagSearch(long id, String keyword, int n) {
        return AnalystFactory.get(id).nameAndTagSearch(keyword, n);
    }

    @Override
    public List<Conversation> mostUsers(long id, int n) {
        return AnalystFactory.get(id).mostUsers(n);
    }

    @Override
    public List<Conversation> mostActiveRecently(long id, long millis, int n) {
        return AnalystFactory.get(id).mostActiveRecently(millis, n);
    }

    @Override
    public List<Conversation> recommend(long id, long userID, int n) {
        User user = (User) HibernateUtil.getTransaction().get(UserModel.class, userID);
        return AnalystFactory.get(id).recommend(user, n);
    }

    @Override
    public Message notify(long id, long userID, long currConversationID, long millis) {
        User user = (User) HibernateUtil.getTransaction().get(UserModel.class, userID);
        return AnalystFactory.get(id).notify(user, currConversationID, millis);
    }

    @Override
    public List<Conversation> mostMessages(long id, int n) {
        return AnalystFactory.get(id).mostMessages(n);
    }

    @Override
    public List<User> mostActiveUsers(long id, int n) {
        return AnalystFactory.get(id).mostActiveUsers(n);
    }

    @Override
    public int activity(long id, long millis) {
        return AnalystFactory.get(id).hail(millis);
    }

    @Override
    public double maleToFemaleRatio(long id) {
        return AnalystFactory.get(id).maleToFemaleRatio();
    }

    @Override
    public double maleToFemaleRatio(long id, long conversationId) {
        Conversation conversation = (Conversation) HibernateUtil.getTransaction().get(ConversationModel.class, conversationId);
        return AnalystFactory.get(id).maleToFemaleRatio(conversation);
    }

    @Override
    public int messageCount(long id, long convoId) {
        return AnalystFactory.get(id).messageCount(convoId);
    }

    @Override
    public int messageCount(long id) {
        return AnalystFactory.get(id).messageCount();
    }

    @Override
    public int userCount(long id, long convoId) {
        return AnalystFactory.get(id).userCount(convoId);
    }

    @Override
    public int userCount(long id) {
        return AnalystFactory.get(id).userCount();
    }

    @Override
    public int contributingUsers(long id, long convoId, boolean current) {
        return AnalystFactory.get(id).contributingUsers(convoId, current);
    }

    @Override
    public long lastTimeActive(long id, long userId) {
        User user = (User) HibernateUtil.getTransaction().get(UserModel.class, userId);
        return AnalystFactory.get(id).lastTimeActive(user);
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
