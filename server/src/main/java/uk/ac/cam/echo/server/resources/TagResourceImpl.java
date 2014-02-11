package uk.ac.cam.echo.server.resources;

import org.hibernate.criterion.Restrictions;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Tag;
import uk.ac.cam.echo.data.resources.TagResource;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.models.TagModel;

import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;

public class TagResourceImpl implements TagResource {
    private Conversation conversation;
    public TagResourceImpl(Conversation conversation) {
        this.conversation = conversation;
    }

    public Collection<Tag> getAll() {
        return conversation.getTags();
    }

    public Tag get(long id) {
        return (Tag) HibernateUtil.getTransaction().get(TagModel.class, id);
    }

    public Response update(Tag m) {
        HibernateUtil.getTransaction().update(m);
        return Response.ok().build();
    }

    @Override
    public Tag create(Tag data) {
        List<TagModel> result = HibernateUtil.getTransaction().createCriteria(TagModel.class)
                     .add(Restrictions.eq("name", data.getName()))
                     .list();

        TagModel tag;
        if (result.size() > 0)
            tag = result.get(0);
        else
            tag = (TagModel) data;

        HibernateUtil.getTransaction().save(tag);

        conversation.getTags().add(tag);
        HibernateUtil.getTransaction().save(conversation);
        return tag;
    }

    public Response delete(long id) {
        Tag u = get(id);
        HibernateUtil.getTransaction().delete(u);
        return Response.ok().build();
    }
}
