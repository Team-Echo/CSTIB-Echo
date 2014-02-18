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
    public void setPassword(String password);
    public void setFirstName(String name);
    public void setLastName(String name);
    public void setPhoneNumber(String number);
    public void setAvatarLink(String link);
    public void setEmail(String email);
    // public Location setCurrentLocation();
    // public List<Interest> setInterests();
    public void setJobTitle(String jobTitle);
    public void setCompany(String company);
    public void setGender(String gender);

    public boolean authenticate(String password);
}
