package com.itheima.dao;

import com.itheima.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderDao {
    List<Order> fandSetmealIdByMemberId(Integer id);

    void add(Order order);

    Order fandById(Integer id);

    Map fandOrderInfoById(Integer id);

    List<Order> fandOrderOfDate(String today);

    List<Order> fandOrderOfDateByStatus(@Param("date") String today,@Param("status") String status);

    Integer countOrderOfWeek(@Param("thisWeekMonday") String thisWeekMonday,@Param("thisWeekLastday")String thisWeekLastday);

    Integer countWeekOrderBystatus(@Param("date")String thisWeekMonday,@Param("status") String status);

    Integer countOrderOfMonth(@Param("begin")String begin,@Param("after") String after);

    Integer countOrderOfMonthByStatus(@Param("begin")String begin,@Param("after") String after,@Param("status") String status);

    List<Map<Integer,Object>> fandCountBySetmeal();
}
