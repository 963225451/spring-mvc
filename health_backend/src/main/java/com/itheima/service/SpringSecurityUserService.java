package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    private UserService userServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        操作数据库查询用户信息
        User user = userServiceImpl.fandByUserName(username);
        if (user == null) {
            return null;
        }
        List<GrantedAuthority> list = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        if (roles != null && roles.size() > 0) {//遍历拥有的角色
            for (Role role : roles) {
                list.add(new SimpleGrantedAuthority(role.getKeyword()));// 关键词放入list
                Set<Permission> permissions = role.getPermissions();//拿到角色拥有的权限
                for (Permission permission : permissions) {
                    list.add(new SimpleGrantedAuthority(permission.getKeyword()));// 权限放入list
                }
            }
            //返回用户细节到框架
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
            return userDetails;
        }
        //返回用户细节到框架
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
        return userDetails;
    }
}