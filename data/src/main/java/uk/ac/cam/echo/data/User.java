package uk.ac.cam.echo.data;

import java.io.Serializable;

public interface User extends Serializable
{
public String getUsername();
public Conversation getCurrentConversation();
public void setCurrentConversation(Conversation conv);
}
