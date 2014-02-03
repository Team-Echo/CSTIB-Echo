package uk.ac.cam.echo.data.resources;

import javax.ws.rs.core.Response;

public interface RestResource<T> extends Resource {
    public T get(long id);
    public Response delete(long id);

    /*
    public void update(T item);
    public T create(T item);
    */
}
