package uk.ac.cam.echo.data;

import java.io.Serializable;
import java.util.Set;

public interface Conference extends Serializable
{
    public long getID();
    public String getName();
    public String setName();
    public Set<Conversation> getConversations();
    public void addConversation();
}
