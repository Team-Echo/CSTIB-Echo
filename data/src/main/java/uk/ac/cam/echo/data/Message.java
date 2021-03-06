package uk.ac.cam.echo.data;

public interface Message extends Base
{
    public long getId();
    public long getTimeStamp();
    public User getSender();
    public String getSenderName();
    public void setSenderName(String senderName);
    public void setSender(User user);
    public Conversation getConversation();
    public void setConversation(Conversation conversation);
    public String getContents();
    public void setContents(String contents);
}
