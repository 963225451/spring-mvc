package com.itheima.dao;

import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    public List<CheckGroup> findPage(String PageString);

    public void add(CheckGroup checkGroup);


    public void update(CheckGroup checkGroup);

   public void delById(Integer checkGroupId);

    CheckGroup fandById(Integer integer);
}
