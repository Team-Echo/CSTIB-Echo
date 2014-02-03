package uk.ac.cam.echo.client.data;

import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.data.Conversation;

import java.util.List;

public class ConferenceData extends BaseData implements Conference{
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Conference: " + getName();
    }

    @Override
    public List<Conversation> getConversationSet() {
        return getApi().conferenceResource.getConversations(getId());
    }

    @Override
    public void addConversation(Conversation conv) {
        throw new UnsupportedOperationException("Not implemented yet use api.conversationResource.create(...)");
    }

    @Override
    protected void configureResource() {
        setResource(getApi().conferenceResource);
    }
}
