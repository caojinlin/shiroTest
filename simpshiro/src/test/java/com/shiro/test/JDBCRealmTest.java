package com.shiro.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import javax.sql.DataSource;

public class JDBCRealmTest {
    DruidDataSource dateSource = new DruidDataSource();
    {
        dateSource.setUrl("jdbc:mysql://localhost:3306/test");
        dateSource.setPassword("root");
        dateSource.setUsername("root");
    }
    @Test
    public void  testAuthentication(){
        //新建JDBCRealm
        JdbcRealm jdbcRealm = new JdbcRealm();
        //添加数据源
        jdbcRealm.setDataSource(dateSource);
        //开启jdbcReaml的权限功能
        jdbcRealm.setPermissionsLookupEnabled(true);
        //1.构建SecurtyManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);
        //2.构建主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        //构建认证请求
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "testadmin");
        //进行认证
        subject.login(token);
        //判断认证是否成功，认证失败会报异常
        System.out.println("isAuthentication :"+subject.isAuthenticated());
        //判断是否存在角色
        subject.checkRole("admin");
        //判断是否具有某个权限
        subject.checkPermission("user:delete");
    }
}
