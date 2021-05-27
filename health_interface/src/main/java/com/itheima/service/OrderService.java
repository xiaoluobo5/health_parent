package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.Order;

import java.util.Map;

public interface OrderService {
    Result addOrder(Map<String, String> map);

    Result findById4Detail(Integer id);
}
