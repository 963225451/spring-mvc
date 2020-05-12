package com.itheima.dao;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Member;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    public List<Setmeal> findPage(String s);

    void add(Setmeal setmeal);

    List<Setmeal> fandAll();

    Setmeal fandById(Integer id);

    Setmeal finAllRelationdById(Integer id);

    List<Map<String, Object>> findSetmealCount();

}
