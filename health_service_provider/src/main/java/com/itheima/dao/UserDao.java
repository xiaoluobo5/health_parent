package com.itheima.dao;

import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author radish
 */
public interface UserDao {
    /**
     * 通过用户名 查询指定的用户信息
     * @param username
     * @return
     */
    User findByUsername(@Param("username") String username);
}
