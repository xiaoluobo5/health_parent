package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;

public interface OrderSettingDao {

    long findCountByOrderDate(Date orderDate);

    void editNumberByOrderDate(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);
}
