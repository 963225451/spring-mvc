package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.Utils.SMSUtils;
import com.itheima.Utils.ValidateCodeUtils;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/validateCode")
public class validateCode {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService OrderServiceImpl;

    @RequestMapping("/send4Order.do")
    public Result send4Order(String telephone) {
        Integer integer = ValidateCodeUtils.generateValidateCode(4);
//        发送验证码
        String s = String.valueOf(integer);
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, s);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //保存验证码到缓存
        Jedis resource = jedisPool.getResource();
        resource.setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 300, s);
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/submit.do")
    public Result submit(@RequestBody Map map) {
        Jedis resource = jedisPool.getResource();
        String telephone = (String) map.get("telephone");
        String s = resource.get(telephone + RedisMessageConstant.SENDTYPE_ORDER);

        if (!(s.equals((String) map.get("validateCode")))) {//验证码错误
            return new Result(false, "验证码错误");
        }
        map.put("orderType", Order.ORDERTYPE_WEIXIN);
        int cc = OrderServiceImpl.oder(map);
        if(cc>0){
            return new Result(true, "预约成功",cc);
        }else{
            return new Result(false, "预约失败");
        }

    }
    @RequestMapping("/send4Login.do")
    public Result submit(String telephone){
        Integer integer = ValidateCodeUtils.generateValidateCode(6);
        String s = integer.toString();
        try {
            //发送登录验证码
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,s);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        Jedis resource = jedisPool.getResource();
        resource.setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,300,s);
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
