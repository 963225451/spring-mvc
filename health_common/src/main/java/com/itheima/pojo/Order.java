package com.itheima.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 体检预约信息
 */
public class Order implements Serializable{
    public static final String ORDERTYPE_TELEPHONE = "电话预约";
    public static final String ORDERTYPE_WEIXIN = "微信预约";
    public static final String ORDERSTATUS_YES = "已到诊";
    public static final String ORDERSTATUS_NO = "未到诊";
    private Integer id;
    private Integer member_Id;//会员id
    private Date orderDate;//预约日期
    private String orderType;//预约类型 电话预约/微信预约
    private String orderStatus;//预约状态（是否到诊）
    private Integer setmeal_Id;//体检套餐id

    public Order() {
    }

    public Order(Integer id) {
        this.id = id;
    }

    public Order(Integer memberId, Date orderDate, String orderType, String orderStatus, Integer setmealId) {
        this.member_Id = memberId;
        this.orderDate = orderDate;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.setmeal_Id = setmealId;
    }

    public Order(Integer id, Integer memberId, Date orderDate, String orderType, String orderStatus, Integer setmealId) {
        this.id = id;
        this.member_Id = memberId;
        this.orderDate = orderDate;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.setmeal_Id = setmealId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMember_Id() {
        return member_Id;
    }

    public void setMember_Id(Integer member_Id) {
        this.member_Id = member_Id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getSetmeal_Id() {
        return setmeal_Id;
    }

    public void setSetmeal_Id(Integer setmeal_Id) {
        this.setmeal_Id = setmeal_Id;
    }
}
