package uk.ac.cam.echo.data;

import java.util.Collection;

public interface Conference
{
    public long getId();
    public String getName();
    public void setName(String name);
    // this should be a List as the conversations will need to be ordered by relevance
    public Collection<Conversation> getConversationSet();
    public void addConversation(Conversation conv);
}
