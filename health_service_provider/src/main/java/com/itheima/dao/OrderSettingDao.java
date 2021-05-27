package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    long findCountByOrderDate(Date orderDate);

    void editNumberByOrderDate(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    public List<OrderSetting> getOrderSettingByMonth(Map date);

    OrderSetting findByOrderDate(Date orderDate);

    void updateReservations(OrderSetting orderSetting);
}
