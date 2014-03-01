package uk.ac.cam.echo.client.data;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.client.ProxyResource;
import uk.ac.cam.echo.data.Interest;
import uk.ac.cam.echo.data.User;
import uk.ac.cam.echo.data.resources.UserResource;

public class InterestData extends BaseData implements Interest {
    private String name;
    private ProxyResource<User, UserResource> userProxy = new ProxyResource<User, UserResource>();

    @Override
    public void setApi(ClientApi api) {
        super.setApi(api);
        userProxy.setResource(api.userResource);
    }

    @Override
    protected void configureResource() {
        try
        {
            setResource(getApi().userResource.getInterestResource(getUserId()));
        }
        catch (Exception e) { }
    }

    public void configureResource(long userId) {
        setResource(getApi().userResource.getInterestResource(userId));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public User getUser() {
        return userProxy.getData();
    }

    @JsonProperty
    public void setUser(User user) {
        userProxy.setData(user);
    }

    public Long getUserId() {
        return userProxy.getId();
    }

    public void setUserId(Long id) {
        userProxy.setId(id);
    }

}
