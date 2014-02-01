package uk.ac.cam.echo.client.data;

import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.client.ProxyResource;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.resources.ConversationResource;
import uk.ac.cam.echo.data.resources.UserResource;

public class MessageData extends BaseData implements Message {
    private long id;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
    public User getSender() {
        return senderProxy.getData();
    }

    public void setSenderId(long id) {
        senderProxy.setId(id);
    }

    public void setSender(User u) {
        senderProxy.setData(u);
    }

    @Override
    public Conversation getConversation() {
        return conversationProxy.getData();
    }

    public void setConversationId(long id) {
        conversationProxy.setId(id);
    }

    public void setConversation(Conversation conv) {
        conversationProxy.setData(conv);
    }
}
