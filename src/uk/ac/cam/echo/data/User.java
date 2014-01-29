package uk.ac.cam.echo.data;

import java.io.Serializable;

public interface User implements Serializable
{
public String getUsername();
public Conversation getConversation();
public void setConversation();
}
