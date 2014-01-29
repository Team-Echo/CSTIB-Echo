package uk.ac.cam.echo.data;

public interface User
{
    public long getId();
    public String getUsername();
    public Conversation getCurrentConversation();
    public void setCurrentConversation(Conversation conv);
}
