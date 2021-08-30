package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SecurityUserDetailsService implements UserDetailsService {
    @Reference
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户标识（用户名），从数据库中读取用户信息
        com.itheima.pojo.User user = userService.findByUsername(username);
        if (user == null) {
            return null;
        }
        //2.提取用户信息的角色与权限关键词信息
        //3.把用户角色与权限关键词封装为List<GrantedAuthority>列表
        List<GrantedAuthority> authorityList = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //授予角色
            authorityList.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //授权
                authorityList.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        UserDetails userDetails = new User(username, user.getPassword(), authorityList);
        return userDetails;
    }
}
