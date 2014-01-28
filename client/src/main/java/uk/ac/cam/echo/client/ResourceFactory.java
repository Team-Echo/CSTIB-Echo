package uk.ac.cam.echo.client;

import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.glassfish.jersey.internal.util.ReflectionHelper;
import org.glassfish.jersey.media.sse.EventOutput;
import uk.ac.cam.echo.data.Message;
import uk.ac.cam.echo.data.async.Handler;
import uk.ac.cam.echo.data.async.Subscription;
import uk.ac.cam.echo.data.resources.Resource;

import javax.ws.rs.client.WebTarget;
import java.lang.reflect.*;
import java.security.AccessController;

public class ResourceFactory implements InvocationHandler{

    private final Object subResource;
    private ResourceFactory(Object c) {
        subResource = c;
    }

    public static Object newResource(Class<?> resourceInterface, Object resource) {
        return  Proxy.newProxyInstance(AccessController.doPrivileged(ReflectionHelper.getClassLoaderPA(resourceInterface)),
                new Class[]{resourceInterface},
                new ResourceFactory(resource));
    }

    public static Object newResource(Class<?> resourceInterface, WebTarget target) {
        return newResource(resourceInterface,
                WebResourceFactory.newResource(resourceInterface, target));
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(method.getReturnType());

        if (method.getReturnType() == EventOutput.class)
            throw new RuntimeException("You can't call this from the client");

        if (method.getReturnType() == Subscription.class) {
            ParameterizedType type = (ParameterizedType) args[0].getClass().getGenericInterfaces()[0];
            Class clazz = (Class) type.getActualTypeArguments()[0];

            InvocationHandler  ih = Proxy.getInvocationHandler(subResource);

            Field targetField = ih.getClass().getDeclaredField("target");
            targetField.setAccessible(true);
            return new ClientSubscription(
                    (WebTarget) targetField.get(ih), (Handler<Message>)args[0], clazz);
        }

        Object res = method.invoke(subResource, args);

        if (res instanceof Resource)
            res = newResource(res.getClass().getInterfaces()[0], res);

        return res;
    }

}
