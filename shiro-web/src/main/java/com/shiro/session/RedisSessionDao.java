package com.shiro.session;

import com.shiro.util.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.util.CollectionUtils.*;
import static org.springframework.util.SerializationUtils.deserialize;

public class RedisSessionDao extends AbstractSessionDAO {

    @Resource
    private JedisUtil jedisUtil;

    private final String SHIRO_SESSION_PREFIX= "test-session";

    private byte[] getkey(String key){
        return (String.format("%s%s", SHIRO_SESSION_PREFIX, key)).getBytes();
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        //绑定sessionId和session
        assignSessionId(session,sessionId );
        redisSaveSession(session);
        return sessionId;
    }



    private void redisSaveSession(Session session) {
        if (session!=null&&session.getId()!=null) {
            byte[] key = getkey(session.getId().toString());
            byte[] value = SerializationUtils.serialize(session);
            jedisUtil.set(key,value);
            jedisUtil.expire(key,600);
        }
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId==null) {
            return null;
        }
        byte[] key = getkey(sessionId.toString());
        byte[] value = jedisUtil.getkey(key);
        return (Session) deserialize(value);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        redisSaveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session==null || session.getId()==null) {
            return;
        }
        jedisUtil.del(getkey(session.getId().toString()));
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys = jedisUtil.keys(SHIRO_SESSION_PREFIX);
        Set<Session> sessions = new HashSet<>();
        if (!isEmpty(keys)) {
            for (byte[] key : keys) {
                Session session = (Session) SerializationUtils.deserialize(key);
                sessions.add(session);
            }
            return sessions;
        } else {
            return sessions;
        }
    }
}
