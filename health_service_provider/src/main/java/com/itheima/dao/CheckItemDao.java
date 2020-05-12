package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    public void add(CheckItem checkItem);
    public  List<CheckItem> pageQuery(String s);
    public void delById(int id);
    public void updateById(CheckItem checkItem);

    CheckItem fandById(Integer integer);
}
