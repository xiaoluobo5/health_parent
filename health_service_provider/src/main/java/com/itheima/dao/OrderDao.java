package com.itheima.dao;

import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    void add(Order order);

    List<Order> findByCondition(Order order);

    Map<String, Object> findById4Detail(Integer id);
}
