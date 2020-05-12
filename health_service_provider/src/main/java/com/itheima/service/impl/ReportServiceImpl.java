package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.Utils.DateUtils;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService  {
    @Autowired
    private MemberService memberServiceImpl;
    @Autowired
    private SetmealService setmealServiceImpl;
    @Autowired
    private OrderService orderServiceImpl;
    @Autowired
    private HostSetmeal hostSetmealImpl;

    @Override
    public Map<String, Object> getBusinessReportData() {
        //最终需要返回的数据
        Map<String, Object> map = new HashMap<>();
        List<Map> list=new ArrayList<>();
        Date thisWeekMonday = DateUtils.getThisWeekMonday();//当前时间星期的周一那天
        Date thisWeekLastDay = DateUtils.getLastDayOfWeek(new Date());//当前时间这周的最后一天
        String today = null;
        String beginDate = null;
        String lastWeekDay = null;
        String afterDate = null;
        try {
            today = DateUtils.parseDate2String(new Date());
            beginDate = DateUtils.parseDate2String(thisWeekMonday);
            lastWeekDay = DateUtils.parseDate2String(thisWeekMonday);
            afterDate = DateUtils.parseDate2String(thisWeekLastDay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("reportDate",today);//当前时间=========
        //今日新增会员数
        List<Member> MemberList=memberServiceImpl.todayNewMember(today);
        map.put("todayNewMember",MemberList==null? 0:MemberList.size());//今日新增会员数==========
        List list2=new ArrayList<String>();
        list2.add(today);
        Integer totalMember = (Integer) memberServiceImpl.findMemberCountByMonths(list2).get(0);//总会员数
        map.put("totalMember",totalMember);//总会员数===============
//本周新增会员数
        //        //这里只要查找大于这周一的数据就好了
        List<Member> thisWeekNewMemberList = setmealServiceImpl.findMemberAfterDate(lastWeekDay);
        map.put("thisWeekNewMember",thisWeekNewMemberList.size());//本周新增会员数===========
        //本月新增会员数
        String toDayExclusiveDay = new SimpleDateFormat("yyyy-MM").format(new Date());
        List<Member> thisMonthNewMemberList = setmealServiceImpl.findMemberOfMonth(toDayExclusiveDay+"-1");
        map.put("thisMonthNewMember",thisMonthNewMemberList.size());  //本月新增会员数 ============
        //今日预约数
        List<Order> Orderlist= orderServiceImpl.fandOrderOfDate(today);
        map.put("todayOrderNumber",Orderlist.size());//今日预约数 ==================
        //今日到诊数
        List<Order> OrderStatuslist= orderServiceImpl.fandOrderOfDateByStatus(today,"已到诊");
        map.put("todayVisitsNumber",OrderStatuslist.size());//今日到诊数  ==============
        //本周预约数
        //大于周一即可
   Integer weekCount=orderServiceImpl.countOrderOfWeek(beginDate,afterDate);
        map.put("thisWeekOrderNumber",weekCount);  //本周预约数 =============
        //本周到诊数
        String weekMonday = null;
        try {
            weekMonday = DateUtils.parseDate2String(thisWeekMonday);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer weekStatusCount=  orderServiceImpl.countWeekOrderBystatus(weekMonday,"已到诊");
        map.put("thisWeekVisitsNumber",weekStatusCount); //本周到诊数 ==================
        // 本月预约数      thisMonthOrderNumber
        String begin=toDayExclusiveDay+ "-1";
        String after=toDayExclusiveDay+ "-31";
        Integer thisMonthOrderNumber=orderServiceImpl.countOrderOfMonth(begin,after);
        map.put("thisMonthOrderNumber",thisMonthOrderNumber);
// 本月到诊数
        Integer MonthStatusCount= orderServiceImpl.countOrderOfMonthByStatus(begin,after,"已到诊");
        map.put("thisMonthVisitsNumber",MonthStatusCount);// 本月到诊数===============
//热门套餐
        List<Map> hotSetmeal=hostSetmealImpl.findHotSetmeal(4);
        map.put("hotSetmeal",hotSetmeal);
        return map;
    }
}
