package uk.ac.cam.echo.client;

import uk.ac.cam.echo.client.data.BaseData;
import uk.ac.cam.echo.data.resources.RestResource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

        try {
            return resource.get(id);
        } catch (AbstractMethodError error) {

            try {
                Method m = resource.getClass().getMethod("get", long.class);
                Object args[] = {id};
                return (T) Proxy.getInvocationHandler(resource).invoke((Object) resource, m, args);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return null;
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
