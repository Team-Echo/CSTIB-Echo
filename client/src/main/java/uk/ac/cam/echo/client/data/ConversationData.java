package uk.ac.cam.echo.client.data;

import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.Tag;
import uk.ac.cam.echo.data.User;

import java.util.List;
import java.util.Set;

public class ConversationData implements Conversation {
    private long id;
    private String name;

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

    @Override
    public Set<Tag> getTags() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Set<Message> getMessages() {
        throw new UnsupportedOperationException("Not implemented yet. Use api.conversationResource.getMessageResource(conv_id).getAll()");
    }

    @Override
    public List<Message> getSortedMessages() {
        throw new UnsupportedOperationException("Not implemented yet. Use api.conversationResource.getMessageResource(conv_id).getAll()");
    }

    @Override
    public Set<User> getUsers() {
        throw new UnsupportedOperationException("Not implemented yet");
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
