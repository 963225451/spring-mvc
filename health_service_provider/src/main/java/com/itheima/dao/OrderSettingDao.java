package com.itheima.dao;

import com.itheima.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    public void add(OrderSetting orderSetting);

    void del_OrderSetting_By_orderDates_And_NoIn_ids(@Param("Orders_List")List<OrderSetting> list);

    List<Integer> fand_Id_By_orderDates_And_number(@Param("OrderSetting")OrderSetting orderSetting);

    void del_By_Id(Integer integer);

    List<OrderSetting> getOrderSettingByMonth(Map map);

    void updateNumberByDate(@Param("orderSetting") OrderSetting orderSetting);


    void addOne(OrderSetting orderSetting);

    List<OrderSetting> fandOrderSettingByderDate(Date orderDate);

    void editReservationsByOrderDate(OrderSetting orderSetting);

    long findCountByOrderDate(Date orderDate);

    void editNumberByOrderDate(OrderSetting orderSetting);
}
