package com.shiro.dao.impl;

import com.shiro.dao.UserDao;
import com.shiro.vo.User;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class UserDaoImpl implements UserDao{

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public User getUserByUserName(String username) {
        String sql = "select username,password from users where username=?";
        List<User> query = jdbcTemplate.query(sql, new String[]{username}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        });
        if(CollectionUtils.isEmpty(query)){
            return null;
        }
        return query.get(0);
    }

    @Override
    public List<String> getPermissionsByUsername(String rolename) {
        String sql = "select permission from roles_permissions where role_name = ?";
        List<String> permissions = jdbcTemplate.query(sql, new String[]{rolename}, new
                RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("permission");
            }
        });
        if (CollectionUtils.isEmpty(permissions)) {
            return null;
        }
        return permissions;
    }


    @Override
    public List<String> getRolesByUserName(String username) {
        String sql = "select role_name from user_roles where username = ?";
        List<String> role_names = jdbcTemplate.query(sql, new String[]{username}, new
                RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("role_name");
            }
        });
        if (CollectionUtils.isEmpty(role_names)) {
            return null;
        }
        return role_names;
    }
}
