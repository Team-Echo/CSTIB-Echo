package uk.ac.cam.echo.client.data;

import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.data.resources.RestResource;

public abstract class BaseData {
    private long id;
    private ClientApi api;
    private RestResource resource;

    public void delete() {
        resource.delete(getId());
    }

    public ClientApi getApi() {
        return api;
    }

    public void setApi(ClientApi api) {
        this.api = api;
        configureResource();
    }

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

    public void setId(long id) {
        this.id = id;
    }
}
