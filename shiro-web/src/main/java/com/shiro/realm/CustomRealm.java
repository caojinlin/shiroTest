package com.shiro.realm;

import com.shiro.dao.UserDao;
import com.shiro.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.*;

public class CustomRealm extends AuthorizingRealm {

    @Resource
    private UserDao userDao;

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("testcjl");
        System.out.println(md5Hash.toString());
    }

    /**
     * 授权方法
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取主体传来的已认证信息
        String username = (String) principalCollection.getPrimaryPrincipal();
        //获取对象的角色
        Set<String> roles = getRolesByUsername(username);
        //获取对象的权限
        Set<String> permissions = getPermissionsByUsername(username);
        //构造返回值
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    /**
     * 认证方法
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        //从主体转过来的认证信息中获取用户名
        String username = (String) authenticationToken.getPrincipal();
        //通过用户名到数据库中获取凭证
        String password = getpasswordbyUserName(username);
        if (password == null) {
            return null;
        }
        //构造返回值
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username,
                password, "customRealm");
        return authenticationInfo;
    }

    private String getpasswordbyUserName(String username) {
        System.out.println("从数据库中获取数据");
        User user = userDao.getUserByUserName(username);
        if (user != null) {
            return user.getPassword();
        }
        return null;
    }

    private Set<String> getPermissionsByUsername(String username) {
        Set<String> roles = getRolesByUsername(username);
        Set<String> list = new HashSet<String>();
        for (String role : roles) {
            List<String> perms = userDao.getPermissionsByUsername(role);
            if (perms != null) {
               list.addAll(perms);
             }
        }
        return list;
    }

    private Set<String> getRolesByUsername(String username) {
        List<String> list = userDao.getRolesByUserName(username);
        if (list != null) {
            return new HashSet<String>(list);
        }
        return null;
    }

}
