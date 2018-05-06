package com.shiro.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

@Component
public class JedisUtil {

    @Resource
    private JedisPool jedisPool;

    private Jedis getResource(){
        return jedisPool.getResource();
    }

    public byte[] set(byte[] key, byte[] value) {
        Jedis  jedis = getResource();
        try {
            jedis.set(key,value );
            return value;
        } finally {
            jedis.close();;
        }
    }

    public void expire(byte[] key, int i) {
        Jedis jedis = getResource();
        try {
            jedis.expire(key,i );
        } finally {
            jedis.close();
        }
    }

    public byte[] getkey(byte[] key) {
        Jedis jedis = getResource();
        try {
            byte[] value = jedis.get(key);
            return value;
        } finally {
            jedis.close();
        }
    }

    public void del(byte[] getkey) {
        Jedis jedis = getResource();
        try {
            jedis.del(getkey);
        } finally {
            jedis.close();
        }
    }

    public Set<byte[]> keys(String shiro_session_prefix) {
        Jedis jedis = getResource();
        try {
            Set<byte[]> value = jedis.keys((shiro_session_prefix+"*").getBytes());
            return value;
        } finally {
            jedis.close();
        }
    }
}
