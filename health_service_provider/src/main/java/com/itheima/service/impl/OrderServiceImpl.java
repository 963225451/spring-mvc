package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.*;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Autowired
    private CheckGroup_CheckItemDao checkGroup_checkItemDao;
    @Autowired
    private Setmeal_CheckGroupDao setmeal_checkgroupDao;
    @Autowired
    private CheckItemDao checkItemDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private SetmealDao setmealDao;

    @Override
    @Transactional
    public int oder(Map map) {
        Date orderDate = DateUtil.parseYYYYMMDDDate((String) map.get("orderDate"));
        List<OrderSetting> orderSettings = orderSettingDao.fandOrderSettingByderDate(orderDate);
        if (!(orderSettings.size() > 0)) {//没有设置预约数
            return -1;
        }
        OrderSetting orderSetting = orderSettings.get(0);
        int reservations = orderSetting.getReservations();//已预约数
        int number = orderSetting.getNumber();//设置的预约数
        if ((number - reservations) < 0) {
            //预约已满
            return -1;
        } else {
            //检查当前用户是否为会员，根据手机号判断
            Member member = memberDao.fandByPhoneNumber((String) map.get("telephone"));
            if (member == null) {
                //注册
                Member member1 = new Member();
                member1.setName((String) map.get("name"));
                member1.setPhoneNumber((String) map.get("telephone"));
                member1.setIdCard((String) map.get("idCard"));
                member1.setSex((String) map.get("sex"));
                member1.setRegTime(new Date());
                memberDao.add(member1);
//                预约
                orderSetting.setReservations(orderSetting.getReservations() + 1);
                orderSettingDao.editReservationsByOrderDate(orderSetting);
                Order order = new Order(member.getId(), orderDate, (String) map.get("orderType"), Order.ORDERSTATUS_NO, Integer.parseInt((String) map.get("setmealId")));
                orderDao.add(order);
                return order.getId();
            } else {
                //是否已经预约套餐
                List<Order> SetmealIds = orderDao.fandSetmealIdByMemberId(member.getId());
                if (SetmealIds != null || SetmealIds.size() > 0) {//套餐是否重复预约
                    for (Order setmealId : SetmealIds) {
                        String setmealId1 = setmealId.getSetmeal_Id().toString();
                        Date date = setmealId.getOrderDate();
                        SimpleDateFormat sj = new SimpleDateFormat("yyyy-MM-dd");
                        String format = sj.format(date);
                        if (map.get("setmealId").equals(setmealId1) && map.get("orderDate").equals(format)) {//预约过了
                            return -1;
                        }
                    }
                    orderSetting.setReservations(orderSetting.getReservations() + 1);
                    orderSettingDao.editReservationsByOrderDate(orderSetting);
                    Order order = new Order(member.getId(), orderDate, (String) map.get("orderType"), Order.ORDERSTATUS_NO, Integer.parseInt((String) map.get("setmealId")));
                    orderDao.add(order);
                    return order.getId();
                }
                orderSetting.setReservations(orderSetting.getReservations() + 1);
                orderSettingDao.editReservationsByOrderDate(orderSetting);
                Order order = new Order(member.getId(), orderDate, (String) map.get("orderType"), Order.ORDERSTATUS_NO, Integer.parseInt((String) map.get("setmealId")));
                orderDao.add(order);
                return order.getId();
            }
        }
    }

    @Override
    public Map findOrderdById(Integer id) {
//        Order order = orderDao.fandById(id);//查询预约详情
//        Integer member_id = order.getMember_Id();//查修预约对应用户id
//        Member member = MemberDao.fandById(member_id);//查询用户详情
//        Integer setmeal_id = order.getSetmeal_Id();//查修预约对应套餐
//        Setmeal setmeal = setmealDao.fandById(setmeal_id);//查询套餐详情
//        Map map = new HashMap();
//        map.put("member", member.getName());
//        map.put("setmeal", setmeal.getName());
//        map.put("orderDate", order.getOrderDate());
//        map.put("orderType", order.getOrderType());
        Map map= orderDao.fandOrderInfoById(id);
        return map;
    }

    @Override//查询当天预约数
    public List<Order> fandOrderOfDate(String today) {
        List<Order> list=  orderDao.fandOrderOfDate(today);
        return list;
    }

    @Override
    public List<Order> fandOrderOfDateByStatus(String today, String status) {
        List<Order> list= orderDao.fandOrderOfDateByStatus(today,status);
        return list;
    }

    @Override
    public Integer countOrderOfWeek(String thisWeekMonday,String thisWeekLastday) {
        return  orderDao.countOrderOfWeek(thisWeekMonday,thisWeekLastday);

    }

    @Override
    public Integer countWeekOrderBystatus(String thisWeekMonday, String status) {
        return orderDao.countWeekOrderBystatus(thisWeekMonday,status);
    }

    @Override
    public Integer countOrderOfMonth(String begin, String after) {
        return orderDao.countOrderOfMonth(begin,after);
    }

    @Override
    public Integer countOrderOfMonthByStatus(String begin, String after, String status) {
        return orderDao.countOrderOfMonthByStatus(begin,after,status);
    }
}
