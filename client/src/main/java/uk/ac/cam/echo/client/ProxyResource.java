package uk.ac.cam.echo.client;

import uk.ac.cam.echo.client.data.BaseData;
import uk.ac.cam.echo.data.resources.RestResource;

public class ProxyResource<T, R extends RestResource<T>> {
    private Long id = null;
    private T data = null;
    private R resource = null;

    public void setResource(R r) {
        resource = r;
    }

    public T getData() {
        if (data != null)
            return data;

        if (id == null)
            return null;

        // TODO implement some form of caching
        return resource.get(id);
    }

    public Long getId() {
        //TODO make this more typesafe
        if (data != null)
            return ((BaseData) data).getId();

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setData(T data) {
        this.data = data;
    }
}
