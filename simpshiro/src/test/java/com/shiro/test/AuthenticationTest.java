package com.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class AuthenticationTest {

    SimpleAccountRealm realm = new SimpleAccountRealm();

    @Before
    public void adduser(){
        realm.addAccount("mark", "12345","admin");
    }


    @Test
    public void  testAuthentication(){
        //1.构建SecurtyManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(realm);
        //2.构建主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        //构建认证请求
        UsernamePasswordToken token = new UsernamePasswordToken("mark", "12345");
        //进行认证
        subject.login(token);
        //判断认证是否成功，认证失败会报异常
        System.out.println("isAuthentication :"+subject.isAuthenticated());
        //判断是否存在角色
        subject.checkRole("admin");
    }
}
