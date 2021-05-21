package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.CheckItemDao;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Autowired
    private CheckItemDao checkItemDao;
    @Autowired
    private JedisPool jedisPool;
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //1.套餐表新增数据
        setmealDao.add(setmeal);
        //2.关系表加数据
        if (checkgroupIds != null & checkgroupIds.length > 0){
            reAssociation(setmeal.getId(),checkgroupIds);
        }
        //3.图片名字放入dbredishkey
        savePic2Redis(setmeal.getImg());
    }

    private void savePic2Redis(String pic) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }

    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findById(Integer id) {
        //调用dao, 基于套餐Id 获取指定的套餐对象
        Setmeal setmeal =  setmealDao.findById(id);
        //调用dao, 基于套餐ID, 获取对应所关联的所有检查组信息
        List<CheckGroup> checkGroupList = checkGroupDao.findCheckGroupById(setmeal.getId());
        setmeal.setCheckGroups(checkGroupList);

        //调用dao, 基于检查组id, 获取对应所关联的所有检查项信息
        for (CheckGroup checkGroup : checkGroupList) {
            //基于检查组id, 获取对应所关联的所有检查项信息
            List<CheckItem> checkItemList = checkItemDao.findCheckItemById(checkGroup.getId());
            checkGroup.setCheckItems(checkItemList);
        }
        return setmeal;
    }

    private void reAssociation(Integer setmealId, Integer[] checkgroupIds) {
        for (Integer checkgroupId: checkgroupIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("setmeal_id",setmealId);
            map.put("checkgroup_id",checkgroupId);
            setmealDao.setSetmealAndCheckGroup(map);
        }
    }
}
