package com.itheima.service;

import com.itheima.pojo.Order;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderService {

    int oder(Map map);

    Map findOrderdById(Integer id);

    List<Order> fandOrderOfDate(String today);

    List<Order> fandOrderOfDateByStatus(String today, String status);

    Integer countOrderOfWeek(String thisWeekMonday,String thisWeekLastday);

    Integer countWeekOrderBystatus(String thisWeekMonday, String 已到诊);

    Integer countOrderOfMonth(String begin, String after);

    Integer countOrderOfMonthByStatus(String begin, String after, String 已到诊);
}
