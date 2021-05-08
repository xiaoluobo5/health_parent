package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    //批量添加
    public void add(List<OrderSetting> list) {
        if (list != null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                //检查此数据（日期）是否存在
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (count > 0) {
                    //已经存在，执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    //不存在，执行添加操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    //根据日期查询预约设置数据
    public List<Map> getOrderSettingByMonth(String date) {//2021-5
        String[] strDate = date.split("-");
        int year = Integer.parseInt(strDate[0]);
        int month = Integer.parseInt(strDate[1]);
        int days = getDays(year, month);
        String dateBegin = date + "-1";//2021-5-1
        String dateEnd = date + "-" +days;//2021-5-31
        Map map = new HashMap();
        map.put("dateBegin", dateBegin);
        map.put("dateEnd", dateEnd);
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> data = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map orderSettingMap = new HashMap();
            orderSettingMap.put("date", orderSetting.getOrderDate().getDate());//获得日期（几号）
            orderSettingMap.put("number", orderSetting.getNumber());//可预约人数
            orderSettingMap.put("reservations", orderSetting.getReservations());//已预约人数
            data.add(orderSettingMap);
        }
        return data;
    }


    public void editNumberByDate(OrderSetting orderSetting) {
        int number = orderSetting.getNumber();
        //检查此数据（日期）是否存在
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {
            //已经存在，执行更新操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        } else {
            //不存在，执行添加操作
            orderSettingDao.add(orderSetting);
        }
    }

    //判断大小月，以获取当月天数
    private static int getDays(int year, int month) {
        int days;
        switch (month) {
            case 2:
                if ((year % 4 == 0 && year % 100 != 100) || year % 400 == 0) {
                    days = 29;
                } else {
                    days = 28;
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            default:
                days = 31;
                break;
        }
        return days;
    }
}