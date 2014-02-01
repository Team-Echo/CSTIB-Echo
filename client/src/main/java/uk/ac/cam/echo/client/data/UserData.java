package uk.ac.cam.echo.client.data;

import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.client.ProxyResource;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.resources.ConversationResource;

public class UserData extends BaseData implements User{
    private long id;
    private String username;
    private ProxyResource<Conversation, ConversationResource> conversationProxy =
            new ProxyResource<Conversation, ConversationResource>();


    @Override
    public void setApi(ClientApi api) {
        super.setApi(api);
        conversationProxy.setResource(api.conversationResource);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Conversation getCurrentConversation() {
        return conversationProxy.getData();
    }

    @Override
    public void setCurrentConversation(Conversation conv) {
        conversationProxy.setData(conv);
    }

    public void setCurrentConversationId(long id) {
        conversationProxy.setId(id);
    }
}
