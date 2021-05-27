package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import com.itheima.util.DateUtils;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.MarshalledObject;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;


    @Override
    public Result addOrder(Map<String, String> map) {
        try {
            String telephone = map.get("telephone");
            Integer setmealId = Integer.valueOf(map.get("setmealId"));
            Date orderDate = DateUtils.parseString2Date(map.get("orderDate"));
            OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
            if (orderSetting == null) {
                return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            }
            //获取当日可预约人数与已预约人数
            int number = orderSetting.getNumber();
            int reservations = orderSetting.getReservations();
            if (reservations >= number) {
                return new Result(false, MessageConstant.ORDER_FULL);
            }
            //查询会员是否已注册
            Member member = memberDao.findByTelephone(telephone);
            if (member != null) {
                //如果已有预约，则获取预约日期
                Order order = new Order(null, member.getId(), orderDate, null, null, setmealId);
                List<Order> orderList = orderDao.findByCondition(order);
                if (orderList != null || orderList.size() > 0) {
                    return new Result(false, MessageConstant.HAS_ORDERED);
                }
            } else {
                //会员信息不存在，基于当前的手机号码创建会员
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberDao.add(member);
            }
            //4.更新会员信息
            System.out.println("会员信息："+map.get("name")+"+"+map.get("sex")+"+"+map.get("idCard"));
            member.setName(map.get("name")); //体检人姓名
            member.setSex(map.get("sex")); //性别
            member.setIdCard(map.get("idCard")); //身份证号
            memberDao.update(member);
            //封装预约对象，包含会员Id 、预约日期、预约类型、预约状态、套餐ID
            Order order = new Order(null, member.getId(), orderDate, Order.ORDERTYPE_WEIXIN, Order.ORDERSTATUS_NO, setmealId);
            //调用dao, 保存预约订单对象
            orderDao.add(order);
            //更新已预约人数
            orderSetting.setReservations(reservations+1);
            orderSettingDao.updateReservations(orderSetting);
            return new Result(true, MessageConstant.ORDER_SUCCESS, order);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }

    /**
     * 查询预约信息
     * @param id
     * @return
     */
    @Override
    public Result findById4Detail(Integer id) {
        System.out.println("预约的id = " + id);
        try {
            Map<String, Object> map = orderDao.findById4Detail(id);
            String orderDate = DateUtils.parseDate2String((Date) map.get("orderDate"));
            map.put("orderDate", orderDate);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
