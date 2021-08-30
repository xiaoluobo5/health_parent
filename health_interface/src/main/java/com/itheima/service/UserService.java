package com.itheima.service;

import com.itheima.pojo.User;

/**
 * @author radish
 */
public interface UserService {
    User findByUsername(String username);
}
