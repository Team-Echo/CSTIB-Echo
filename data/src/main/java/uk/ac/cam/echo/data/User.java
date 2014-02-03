package uk.ac.cam.echo.data;

public interface User extends Base {
    public long getId();
    public String getUsername();
    public Conversation getCurrentConversation();
    public void setCurrentConversation(Conversation conv);
}
