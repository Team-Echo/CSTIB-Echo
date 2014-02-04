package uk.ac.cam.echo.client.data;

import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import uk.ac.cam.echo.client.ClientApi;
import uk.ac.cam.echo.data.resources.RestResource;

import java.util.Map;

public abstract class BaseData {
    private Long id = null;
    private ClientApi api;
    private RestResource resource;

    public void save() {
        if (hasId()) {
            getResource().update(this);
        } else {
            Map<String, Object> result = (Map) getResource().create(this);
            for (String key: result.keySet()) {
                try {
                    BeanUtils.setProperty(this, key, result.get(key));
                } catch (Exception e) {
                   // Just Ignore
                }

            }
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

    @JsonProperty("id")
    public Long getID() {
        return id;
    }

    @JsonIgnore
    public long getId() {
        return id;
    }

    public boolean hasId() {
        return id != null;
    }

    @JsonProperty
    public void setId(long id) {
        this.id = id;
    }
}
