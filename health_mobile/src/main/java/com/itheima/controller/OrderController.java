package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @Author maohao
 * @Date 2021-05-22 14:06
 * @return
 * @Version v1.0.0
 * @Description
*/
@RestController
@RequestMapping("/mobile/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    @RequestMapping("/findById")
    public Result findById(@RequestParam("id") Integer Id) {
        System.out.println("Controller的Id = " + Id);
        try {
            Result result= orderService.findById4Detail(Id);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/submitOrder")
    public Result submitOrder(@RequestBody Map<String, String> map) {
        System.out.println("map = " + map);
        String telephone = map.get("telephone");
        String validateCode = map.get("validateCode");

        String code4Redis = jedisPool.getResource().get(telephone + RedisConstant.SENDTYPE_ORDER);
        System.out.println("code4Redis = " + code4Redis);
        if (code4Redis == null || code4Redis.length() == 0) {
            return new Result(false, "验证码超时，请重新获取");
        } else if (!code4Redis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result = orderService.addOrder(map);
        System.out.println("result = " + result.getData());
//        if (result.getFlag()) {
//            //发送短信
//        }
        return result;
    }
}
