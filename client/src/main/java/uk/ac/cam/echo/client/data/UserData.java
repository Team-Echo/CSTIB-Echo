package uk.ac.cam.echo.client.data;

import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.User;

public class UserData extends BaseData implements User{
    private long id;
    private String username;

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
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void setCurrentConversation(Conversation conv) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }
}
