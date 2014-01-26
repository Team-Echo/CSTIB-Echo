package uk.ac.cam.echo.data;

import java.io.Serializable;
import java.util.Set;

public interface Conference extends Serializable
{
    public long getId();
    public String getName();
    public void setName(String name);
    public Set<Conversation> getConversationSet();
    public void addConversation(Conversation conv);
}
