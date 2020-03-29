package net.lamgc.cgj.cache;

import java.util.Date;
import java.util.Hashtable;
import java.util.Objects;

public class LocalHashCacheStore<T> implements CacheStore<T> {

    private final Hashtable<String, CacheObject<T>> cache = new Hashtable<>();

    @Override
    public void update(String key, T value, Date expire) {
        if(cache.containsKey(key)) {
            cache.get(key).update(value, expire);
        } else {
            CacheObject<T> cacheObject = new CacheObject<>(value, expire);
            cache.put(key, cacheObject);
        }
    }

    @Override
    public T getCache(String key) {
        if(!cache.containsKey(key)) {
            return null;
        }
        CacheObject<T> cacheObject = cache.get(key);
        if(cacheObject.isExpire(new Date())) {
            cache.remove(key);
            return null;
        }
        return cacheObject.get();
    }

    @Override
    public boolean exists(String key) {
        return exists(key, null);
    }

    @Override
    public boolean exists(String key, Date date) {
        if(!cache.containsKey(key)) {
            return false;
        }
        CacheObject<T> cacheObject = cache.get(key);
        if(cacheObject.isExpire(Objects.isNull(date) ? new Date() : date)) {
            cache.remove(key);
            return false;
        }
        return true;
    }

    @Override
    public boolean clear() {
        cache.clear();
        return true;
    }
}