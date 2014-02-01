package uk.ac.cam.echo.data.resources;

public interface RestResource<T> extends Resource {
    public T get(long id);

    /*
    public void delete(long id);
    public void update(T item);
    public T create(T item);
    */
}
