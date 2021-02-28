package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;
    private void clearImg() {
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        for (String filename : sdiff) {
            //七牛云删除垃圾图片
            QiniuUtils.deleteFileFromQiniu(filename);
            //从Redis删除垃圾图片名称
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,filename);
        }

    }
}
