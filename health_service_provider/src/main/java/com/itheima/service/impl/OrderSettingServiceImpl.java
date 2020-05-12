package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    @Transactional
    public int add(List<OrderSetting> list) {

        try {
            if(list != null && list.size() > 0){
                for (OrderSetting orderSetting : list) {
                    //判断当前日期是否已经进行了预约设置
                    long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                    if (countByOrderDate > 0) {
                        //已经进行了预约设置，执行更新操作
                        orderSettingDao.editNumberByOrderDate(orderSetting);
                    } else {
                        //没有进行预约设置，执行插入操作
                        orderSettingDao.add(orderSetting);
                    }
                }
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {//这里传递过来的是2020-5   没有日期
        String dateBegin = date + "-1";//2019-3-1 起始时间
        String dateEnd = date + "-31";//2019-3-31  截止时间
        Map map = new HashMap();
        map.put("dateBegin", dateBegin);
        map.put("dateEnd", dateEnd);
        //将要查询的日期段 封装 查询
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> data = new ArrayList<>();//得到在日期段内设置过预约人数的数据
        if(list!=null&&list.size()>0){
            for (OrderSetting orderSetting : list) {
                Map orderSettingMap = new HashMap();
                orderSettingMap.put("date",orderSetting.getOrderDate().getDate());
                orderSettingMap.put("number",orderSetting.getNumber());
                orderSettingMap.put("reservations",orderSetting.getReservations());
                data.add(orderSettingMap);
            }
        }

        return data;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        List<OrderSetting> list =orderSettingDao.fandOrderSettingByderDate(orderSetting.getOrderDate());
        if(list.size()>0){//有了
            orderSettingDao.updateNumberByDate(orderSetting);
        }else {//没有
            orderSettingDao.addOne(orderSetting);
        }

    }
}
