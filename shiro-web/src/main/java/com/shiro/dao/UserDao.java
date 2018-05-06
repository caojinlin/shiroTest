package com.shiro.dao;

import com.shiro.vo.User;

import java.util.List;

public interface UserDao {
    User getUserByUserName(String username);

    List<String> getPermissionsByUsername(String username);

    List<String> getRolesByUserName(String username);
}
