package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @Author maohao
 * @Date 2021-05-20 16:26
 * @param null
 * @return 
 * @Version v1.0.0
 * @Description 会员业务控制器
*/
@RestController
@RequestMapping("/mobile/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;

    @RequestMapping("/smsLogin")
    public Result smsLogin(@RequestBody Map map) {
        try {
            //获取页面发来的手机号和验证码
            String telephone = (String) map.get("telephone");
            String validateCode = (String) map.get("validateCode");
            //获取Redis中的值
            Jedis jedis = jedisPool.getResource();
            String code4Redis = jedis.get(telephone + RedisConstant.SENDTYPE_LOGIN);
            //对比
            if (code4Redis == null || code4Redis.length() == 0) {
                return new Result(false, "验证码超时，请重新获取");
            }
            if (!validateCode.equals(code4Redis)) {
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            memberService.smsLogin(telephone);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.LOGIN_FAIL);
        }
    }
}
