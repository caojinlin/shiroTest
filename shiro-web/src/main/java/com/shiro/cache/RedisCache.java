package com.shiro.cache;

import com.shiro.util.JedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

@Component
public class RedisCache<K,V> implements Cache<K,V> {

    @Resource
    private JedisUtil jedisUtil;

    private final static String CACHE_PREFIX = "test-cache:";

    private byte[] getkey(K k){
       if(k instanceof String){
           return (CACHE_PREFIX + k).getBytes();
       }else{
           return SerializationUtils.serialize(k);
       }
    }

    @Override
    public V get(K k) throws CacheException {
        byte[] value = jedisUtil.getkey(getkey(k));
        if(value!=null){
            System.out.println("从redis中获取数据");
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        byte[] key = getkey(k);
        byte[] value = SerializationUtils.serialize(v);
        jedisUtil.set(key,value );
        jedisUtil.expire(key,600 );
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        byte[] key = getkey(k);
        byte[] value = jedisUtil.getkey(key);
        jedisUtil.del(key);
        if(value!=null){
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
