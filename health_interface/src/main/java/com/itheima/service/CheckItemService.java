package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

public interface CheckItemService {
    public void add(CheckItem checkItem);
    public PageResult findPage(QueryPageBean queryPageBean);
    public int delById(int id);
    public void updateById(CheckItem checkItem);
    public PageResult findAll();

    CheckItem fandById(Integer integer);
}
