package uk.ac.cam.echo.data;

public interface Message
{
    public long getId();
    public long getTimeStamp();
    public User getSender();
    public Conversation getConversation();
    public String getContents();
}
