package uk.ac.cam.echo.client.data;

import org.codehaus.jackson.annotate.JsonIgnore;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.data.resources.RestResource;

public abstract class BaseData {
    private Long id = null;
    private ClientApi api;
    private RestResource resource;

    public void save() {
        if (hasId()) {
            getResource().update(this);
        } else {
            // TODO
        }
    }

    public void delete() {
        resource.delete(getId());
    }

    @JsonIgnore
    public ClientApi getApi() {
        return api;
    }

    public void setApi(ClientApi api) {
        this.api = api;
        configureResource();
    }

    @JsonIgnore
    public RestResource getResource() {
        return resource;
    }

    public void setResource(RestResource resource) {
        this.resource = resource;
    }

    protected abstract void configureResource();


    public long getId() {
        return id;
    }
    public boolean hasId() {
        return id != null;
    }
    public void setId(long id) {
        this.id = id;
    }
}
