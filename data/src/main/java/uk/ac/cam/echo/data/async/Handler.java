package uk.ac.cam.echo.data.async;

public interface Handler<T> {
    public void handle(T data);
}
