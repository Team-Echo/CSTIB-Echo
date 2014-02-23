package uk.ac.cam.echo.data;

public interface Interest extends Base{

    public String getName();
    public User getUser();

    public void setName(String name);
    public void setUser(User user);
}
