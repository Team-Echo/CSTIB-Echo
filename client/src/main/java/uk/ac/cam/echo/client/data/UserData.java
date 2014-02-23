package uk.ac.cam.echo.client.data;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.client.ProxyResource;
import uk.ac.cam.echo.data.Conversation;
import uk.ac.cam.echo.data.Interest;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.resources.ConversationResource;

import java.util.Collection;

public class UserData extends BaseData implements User{
    private String username;
    private ProxyResource<Conversation, ConversationResource> conversationProxy =
            new ProxyResource<Conversation, ConversationResource>();

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String avatarLink;
    private String email;
    private String jobTitle;
    private String company;
    private String gender;
    private String displayName;

    private String password;

    @Override
    public void setApi(ClientApi api) {
        super.setApi(api);
        conversationProxy.setResource(api.conversationResource);
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    @JsonIgnore
    public Conversation getCurrentConversation() {
        return conversationProxy.getData();
    }

    public Long getCurrentConversationId() {
        return conversationProxy.getId();
    }

    @JsonProperty
    @Override
    public void setCurrentConversation(Conversation conv) {
        conversationProxy.setData(conv);
    }

    @JsonProperty
    public void setCurrentConversationId(long id) {
        conversationProxy.setId(id);
    }

    @Override
    protected void configureResource() {
        setResource(getApi().userResource);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonIgnore
    public Collection<Interest> getInterests() {
        return getApi().userResource.getInterestResource(getId()).getAll();
    }
}
