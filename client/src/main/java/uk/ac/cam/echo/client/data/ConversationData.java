package uk.ac.cam.echo.client.data;

import uk.ac.cam.echo.client.ProxyResource;
import uk.ac.cam.echo.data.*;
import uk.ac.cam.echo.data.resources.ConferenceResource;

import java.util.List;
import java.util.Set;

public class ConversationData extends BaseData implements Conversation {
    private long id;
    private String name;
    private ProxyResource<Conference, ConferenceResource> conferenceProxy =
            new ProxyResource<Conference, ConferenceResource>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Conference getConference() {
        return conferenceProxy.getData();
    }

    public void setConference(Conference data) {
        conferenceProxy.setData(data);
    }

    public void setConferenceId(long id) {
        conferenceProxy.setId(id);
    }

    @Override
    public Set<Tag> getTags() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Message> getMessages() {
        return getApi().conversationResource.getMessageResource(getId()).getAll();
    }

    @Override
    public List<User> getUsers() {
        return (List<User>) getApi().conversationResource.getUsers(getId());
    }

    @Override
    public void addUser(User u) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void removeUser(User u) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void addMessage(Message m) {
        throw new UnsupportedOperationException("Not implemented yet. Use api.conversationResource.getMessageResource(conv_id).create(...)");
    }
}
