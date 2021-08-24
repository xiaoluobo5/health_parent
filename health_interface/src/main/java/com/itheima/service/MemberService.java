package com.itheima.service;

/**
 * @Author maohao
 * @Date 2021-05-20 21:10
 * @return
 * @Version v1.0.0
 * @Description 会员业务服务接口
*/
public interface MemberService {
    /**
     * 通过手机号码快速登录
     * @param telephone
     */
    void smsLogin(String telephone);
}
