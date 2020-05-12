package com.itheima.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Setmeal_CheckGroupDao {

   public List<Integer> find_By_CheckGroup_Id(Integer checkGroupId);

   void add(@Param("setmealId")Integer id,@Param("checkgroupIds") Integer[] checkgroupIds);

   List<Integer> fandGroupIdBySetmralId(Integer id);
}
