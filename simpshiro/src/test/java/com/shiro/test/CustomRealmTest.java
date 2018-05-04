package com.shiro.test;

import com.shiro.reaml.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class CustomRealmTest {
    @Test
    public void  testAuthentication(){
        //新建IniReam
        CustomRealm customRealm = new CustomRealm();
        //1.构建SecurtyManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);
        //2.构建主体提交认证请求
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");//加密方式
        matcher.setHashIterations(1);//加密次数
        customRealm.setCredentialsMatcher(matcher);
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        //构建认证请求
        UsernamePasswordToken token = new UsernamePasswordToken("cjl", "testcjl");
        //进行认证
        subject.login(token);
        //判断认证是否成功，认证失败会报异常
        System.out.println("isAuthentication :"+subject.isAuthenticated());
        //判断是否存在角色
        subject.checkRole("admin");
        subject.checkPermission("user:delete");
    }
}
