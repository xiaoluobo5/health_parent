package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;

public interface SetmealService {

    void add(Setmeal setmeal, Integer[] checkgroupIds);

    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);
}
