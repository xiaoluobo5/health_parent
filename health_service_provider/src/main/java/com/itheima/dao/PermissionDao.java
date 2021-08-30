package com.itheima.dao;

import com.itheima.pojo.Permission;

import java.util.Set;

/**
 * @author radish
 */
public interface PermissionDao {
    Set<Permission> findByRoleId(Integer roleId);
}
