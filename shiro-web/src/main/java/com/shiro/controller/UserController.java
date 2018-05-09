package com.shiro.controller;

import com.shiro.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @RequestMapping(value = "/sublogin",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String subLogin(User user){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),user.getPassword());
        try {
            //保存登陆状态
            token.setRememberMe(true);
            subject.login(token);
        } catch (AuthenticationException e) {
            return e.getMessage();
        }
        try {
            subject.checkPermission("user:delete");
        } catch (AuthorizationException e) {
            return String.format("没有权限%s", e.getMessage());
        }
        return "登录成功,拥有权限";
    }
    @RequiresRoles("admin")
    @ResponseBody
    @RequestMapping(value = "/testrole" ,method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    public String testrole(){
        return "拥有权限 success";
    }
    @RequiresPermissions("user:delete")
    @ResponseBody
    @RequestMapping(value = "/testprom" ,method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    public String testprom(){
        return "拥有权限 success";
    }

    @ResponseBody
    @RequestMapping(value = "/testrolesor" ,method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    public String testroleor(){
        return "拥有权限 success";
    }


}
