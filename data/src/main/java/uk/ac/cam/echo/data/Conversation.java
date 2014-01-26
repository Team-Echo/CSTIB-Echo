package uk.ac.cam.echo.data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface Conversation extends Serializable
{
    public long getId();
    public String getName();
    public void setName(String name);
    public Set<Tag> getTags();
    public Set<Message> getMessages();
    public List<Message> getSortedMessages();
    public Set<User> getUsers();
    public void addUser(User u);
    public void removeUser(User u);
    public void addMessage(Message m);
}
