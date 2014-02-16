package uk.ac.cam.echo.data;

public interface User extends Base {
    public long getId();
    public String getUsername();
    public Conversation getCurrentConversation();
    public String getFirstName();
    public String getLastName();
    public String getDisplayName();
    public String getPhoneNumber();
    public String getAvatarLink();
    public String getEmail();
    // public Location getCurrentLocation();
    // public List<Interest> getInterests();
    public String getJobTitle();
    public String getCompany();
    public String getGender();

    public void setUsername(String username);
    public void setCurrentConversation(Conversation conv);
}
