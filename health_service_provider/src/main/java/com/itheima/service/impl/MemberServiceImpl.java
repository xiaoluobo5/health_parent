package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.itheima.pojo.Member;
import java.util.Date;


/**
 * @Author maohao
 * @Date 2021-05-20 21:15
 * @return
 * @Version v1.0.0
 * @Description 会员接口实现类
 *
*/
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;
    /**
     * 手机号码快速登录
     * @param telephone
     */
    @Override
    public void smsLogin(String telephone) {
        //基于手机号码获取当前会员信息
       Member member = memberDao.findByTelephone(telephone);
        if (null != member) {

        } else {
            //会员信息不存在，基于当前的手机号码创建会员
            Member newMember = new Member();
            newMember.setPhoneNumber(telephone);
            newMember.setRegTime(new Date());
            memberDao.add(newMember);
        }
    }
}
