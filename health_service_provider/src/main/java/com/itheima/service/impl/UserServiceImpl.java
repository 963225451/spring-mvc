package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public User fandByUserName(String s) {
        if (s == null && s.equals("")) {
        //用户名为空
        return null;
        }
            User user = userDao.fandByUserName(s);
            if (user == null) {
                return null;//没有查询到该用户信息
            }
            Integer id = user.getId();//得到UserId
            //            去查关系表得到用户的角色信息
            Set<Role> role = roleDao.findRoleByUserId(id);
            if (role == null || role.size() == 0) {
                return user;
            }

            for (Role role1 : role) {
                Integer id1 = role1.getId();//得到RoleId
                //            去查关系表得到角色的权限信息
                Set<Permission> set = permissionDao.fandPermissionByRoleId(id1);
                if (set != null && set.size() > 0) {
                    role1.setPermissions(set);//写回角色拥有的权限set
                }
            }

            user.setRoles(role);//写回用户拥有的角色set
            //返回用户以及关联信息
            return user;

    }
}
