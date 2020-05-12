package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Member;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    public PageResult findPage(QueryPageBean queryPageBean);

    int add(Setmeal setmeal, Integer[] checkgroupIds);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Integer> fandGroupIdBySetmralId(Integer id);

    Setmeal finAllRelationdById(Integer id);

    List<Map<String,Object>> findSetmealCount();

    List<Member> findMemberAfterDate(String parseDate2String);

    List<Member> findMemberOfMonth(String s);
}
