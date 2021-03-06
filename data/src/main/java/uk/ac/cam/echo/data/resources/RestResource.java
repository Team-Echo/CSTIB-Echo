package uk.ac.cam.echo.data.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public interface RestResource<T> extends Resource {
    @GET
    @Path("/{id}")
    public T get(@PathParam("id") long id);

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") long id);

    @PUT
    @Consumes("application/json")
    public Response update(T item);


    @POST
    @Consumes("application/json")
    public Object create (T data);
//    public T create(T item);
}
