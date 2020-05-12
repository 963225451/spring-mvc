package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderDao;
import com.itheima.dao.SetmealDao;
import com.itheima.pojo.Order;
import com.itheima.pojo.Setmeal;
import com.itheima.service.HostSetmeal;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = HostSetmeal.class)
public class HotSetmeal implements HostSetmeal {
@Autowired
private SetmealDao setmealDaoImpl;
@Autowired
private OrderDao orderDaoImpl;
    @Override
    public List<Map> findHotSetmeal(int number) {//传递过来要的热门前几
        // {"proportion":0.1818,"name":"珍爱高端升级肿瘤12项筛查","setmeal_count":2},
        // 获取到总预约数以及分组排序数 以及套餐id
List<Map> lists= new ArrayList<>();

     List<Map<Integer,Object>> list=orderDaoImpl.fandCountBySetmeal();
        //根据套餐id查name
        //循环 拿到前 number 个name 算占比 封map
        float allCount=0;//计算总预约数
        for (Map<Integer, Object> map : list) {
            Long count = (Long) map.get("count");
            allCount+=count;//增加
        }
        int i1 = number <= list.size() ? number : list.size();
        for (int i = 0; i <i1;i++) {
            Map<Integer, Object> Map = list.get(i);
            Long count = (Long) Map.get("count");
            Integer setmealId = (Integer)Map.get("setmealId");
            Setmeal setmeal = setmealDaoImpl.fandById(setmealId);
            java.util.Map<String, Object> map = new HashMap<>();
           BigDecimal proportio= new BigDecimal(count/allCount);
            map.put("proportion",proportio);
            map.put("name",setmeal.getName());
            map.put("setmeal_count",count);
            lists.add(map);
        }
        return lists;
    }

}
