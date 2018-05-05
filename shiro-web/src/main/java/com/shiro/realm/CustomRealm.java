package com.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm{
    /**
     * 授权方法
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
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //从主体转过来的认证信息中获取用户名
        String username = (String) authenticationToken.getPrincipal();
        //通过用户名到数据库中获取凭证
        String password = getpasswordbyUserName(username);
        if(password==null){
            return null;
        }
        //构造返回值
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo("cjl",password,"customRealm");
        return authenticationInfo;
    }

    Map<String,String> usermap = new HashMap<String, String>(16);
    {
        usermap.put("cjl", "be32a325bd611bbb845327892ef65885");
        super.setName("customRealm");
    }

    private String getpasswordbyUserName(String username) {
        return usermap.get(username);
    }

    private Set<String> getPermissionsByUsername(String username) {
        Set<String> permission = new HashSet<String>();
        permission.add("user:delete");
        permission.add("user:add");
        return  permission;
    }

    private Set<String> getRolesByUsername(String username) {
        Set<String> roles =  new HashSet<String>();
        roles.add("admin");
        roles.add("test");
        return roles;
    }


    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("testcjl");
        System.out.println(md5Hash.toString());
    }
}
