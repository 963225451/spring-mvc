package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    public PageResult findPage(QueryPageBean queryPageBean);

    public void add(CheckGroup checkGroup, Integer[] checkitemIds);

    public  PageResult find_CheckItem_By_CheckGroupId(Integer checkGroupId);

    public void update(CheckGroup checkGroup, Integer[] checkitemIds);

    public int delCheckGroupById(Integer checkGroupId);

    CheckGroup fandById(Integer integer);

    List<Integer> fandCheckItemIdByCheckGroupId(Integer id);
}
