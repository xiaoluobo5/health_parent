package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.Result;
import com.itheima.util.SMSUtils;
import com.qiniu.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static com.itheima.util.SMSUtils.sendShortMessage;
import static com.itheima.util.ValidateCodeUtils.generateValidateCode;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result Send4Order(@RequestParam String telephone) {
        try {
            Integer code = generateValidateCode(4);
            Jedis jedis = jedisPool.getResource();
            jedis.setex(telephone + RedisConstant.SENDTYPE_ORDER, 5 * 60, code.toString());
//            sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
    //预约体检时发送手机验证码
    @RequestMapping("/send4Login")
    public Result Send4Login(@RequestParam String telephone) {
        Integer code = generateValidateCode(4);
        try {
            Jedis jedis = jedisPool.getResource();
            jedis.setex(telephone + RedisConstant.SENDTYPE_LOGIN, 5 * 60, code.toString());
//            sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }
}
