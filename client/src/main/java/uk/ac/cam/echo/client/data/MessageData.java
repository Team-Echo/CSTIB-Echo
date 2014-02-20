package uk.ac.cam.echo.client.data;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.client.ProxyResource;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.resources.ConversationResource;
import uk.ac.cam.echo.data.resources.UserResource;

public class MessageData extends BaseData implements Message {
    private String contents;
    private long timeStamp;

    private ProxyResource<User, UserResource> senderProxy = new ProxyResource<User, UserResource>();
    private ProxyResource<Conversation, ConversationResource> conversationProxy =
            new ProxyResource<Conversation, ConversationResource>();


    @Override
    public void setApi(ClientApi api) {
        super.setApi(api);
        senderProxy.setResource(api.userResource);
        conversationProxy.setResource(api.conversationResource);
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    @JsonIgnore
    public User getSender() {
        return senderProxy.getData();
    }

    @Override
    public String getSenderName() {
        User u = senderProxy.getData();
        if (u.getDisplayName() != null) return u.getDisplayName();
        else return u.getUsername();
    }

    public Long getSenderId() {
        return senderProxy.getId();
    }

    @JsonProperty
    public void setSenderId(long id) {
        senderProxy.setId(id);
    }

    @JsonProperty
    public void setSender(User u) {
        senderProxy.setData(u);
    }

    @Override
    @JsonIgnore
    public Conversation getConversation() {
        return conversationProxy.getData();
    }

    public Long getConversationId() {
        return conversationProxy.getId();
    }

    @JsonProperty
    public void setConversationId(long id) {
        conversationProxy.setId(id);
    }

    @JsonProperty
    public void setConversation(Conversation conv) {
        conversationProxy.setData(conv);
    }

    @Override
    protected void configureResource() {
        setResource(getApi().conversationResource.getMessageResource(conversationProxy.getId()));
    }
}
