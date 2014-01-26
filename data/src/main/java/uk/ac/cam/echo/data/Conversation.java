package uk.ac.cam.echo.data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface Conversation extends Serializable
{
    public long getID();
    public String getName();
    public void setName();
    public String[] getTags();
    public List<Message> getMessages();
    public Set<User> getUsers();
    public void addUser(User u);
    public void removeUser(User u);
    public void addMessage(Message m);
}
