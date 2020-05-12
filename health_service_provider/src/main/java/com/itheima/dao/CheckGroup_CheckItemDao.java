package com.itheima.dao;

import com.itheima.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface CheckGroup_CheckItemDao {
    public List<Integer> findByCheckitemId(int id);

    public void setCheckGroupAndCheckItem(@Param("checkGroupId") Integer checkGroupId,@Param("checkitemIds") Integer[] checkitemIds);

    public  List<Integer> find_CheckItem_By_CheckGroupId(Integer checkGroupId);

    public void delByCheckGroupId(@Param("CheckGroupId") Integer id,@Param("list") Integer[] all);

    public void addByCheckGroupId(@Param("CheckGroupId") Integer id,@Param("list") Integer[] all2);
}
