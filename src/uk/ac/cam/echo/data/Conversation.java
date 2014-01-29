package uk.ac.cam.echo.data;

import java.io.Serializable;
import java.util.Set;

public interface Conversation implements Serializable
{
    public long getID();
    public String getName();
    public void setName();
    public String[] getTags();
    public List<Message> getMessages();
    public Set<User> getUsers();
    public void addUser(User);
    public void removeUser(User);
    public void addMessage(Message);
}
