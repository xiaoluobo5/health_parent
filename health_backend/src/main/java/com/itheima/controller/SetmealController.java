package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    @Autowired
    private JedisPool jedisPool;
    //图片文件上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        //文件得到原始名称
        String originalFilename = imgFile.getOriginalFilename();
        //通过字符串截取最后一个"."来获取图片格式
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        //为了防止服务器文件重复使用UUID重新生成文件名并拼接上图片格式
        String fileName = UUID.randomUUID().toString() + ext;
        try {
            //文件上传到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS);
//            result.setData(fileName);
//            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }

    }
    //
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setmealService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
    }

}
