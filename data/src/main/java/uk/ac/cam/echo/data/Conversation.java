package uk.ac.cam.echo.data;

import java.util.Collection;

public interface Conversation extends Base
{
    public long getId();
    public String getName();
    public void setName(String name);
    public Conference getConference();
    public void setConference(Conference conf);
    public Collection<Tag> getTags();
    public Collection<Message> getMessages();
    public Collection<Message> getMessages(int n);
    //public Collection<Message> getSortedMessages();
    public Collection<User> getUsers();
    public void addUser(User u);
    public void removeUser(User u);
    public void addMessage(Message m);
}
