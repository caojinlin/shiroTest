package com.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

public class CustomSessionManager extends DefaultWebSessionManager{
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {

        Serializable sessionid = getSessionId(sessionKey);
        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }
        if (request!=null && sessionid!=null) {
            Session session = (Session) request.getAttribute(sessionid.toString());
            if(session!=null){
                return  session;
            }
        }
        Session session = super.retrieveSession(sessionKey);
        if(request!=null && sessionid!=null){
            request.setAttribute(sessionid.toString(),session );
        }
        return session;
    }
}
