package com.itheima.dao;

import com.itheima.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author radish
 */
public interface RoleDao {

    Set<Role> findByUserId(@Param("userId") Integer userId);
}
