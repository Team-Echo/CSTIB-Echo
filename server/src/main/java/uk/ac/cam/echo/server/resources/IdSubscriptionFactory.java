package uk.ac.cam.echo.server.resources;

import java.util.concurrent.ConcurrentHashMap;

public class IdSubscriptionFactory<K, T> {
    private ConcurrentHashMap<K, SubscriptionResourceImpl<T>> resources =
            new ConcurrentHashMap<K, SubscriptionResourceImpl<T>> ();

    public SubscriptionResourceImpl<T> get(K key) {
        if (!resources.containsKey(key))
            resources.putIfAbsent(key, new SubscriptionResourceImpl<T>());
        return resources.get(key);
    }
}
