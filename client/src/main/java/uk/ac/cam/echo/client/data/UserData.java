package uk.ac.cam.echo.client.data;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.client.ProxyResource;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.resources.ConversationResource;

public class UserData extends BaseData implements User{
    private String username;
    private ProxyResource<Conversation, ConversationResource> conversationProxy =
            new ProxyResource<Conversation, ConversationResource>();


    @Override
    public void setApi(ClientApi api) {
        super.setApi(api);
        conversationProxy.setResource(api.conversationResource);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    @JsonIgnore
    public Conversation getCurrentConversation() {
        return conversationProxy.getData();
    }

    public Long getCurrentConversationId() {
        return conversationProxy.getId();
    }

    @JsonProperty
    @Override
    public void setCurrentConversation(Conversation conv) {
        conversationProxy.setData(conv);
    }

    @JsonProperty
    public void setCurrentConversationId(long id) {
        conversationProxy.setId(id);
    }

    @Override
    protected void configureResource() {
        setResource(getApi().userResource);
    }
}
