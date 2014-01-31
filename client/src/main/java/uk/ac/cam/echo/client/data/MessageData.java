package uk.ac.cam.echo.client.data;

import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.User;

public class MessageData extends BaseData implements Message {
    private long id;
    private String contents;
    private long timeStamp;

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

        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Conversation getConversation() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
