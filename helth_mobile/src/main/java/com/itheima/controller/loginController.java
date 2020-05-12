package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class loginController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;

    @RequestMapping("/check.do")
    public Result check(@RequestBody Map map, HttpServletResponse response) {
        String validateCode = (String) map.get("validateCode");
        String telephone = (String) map.get("telephone");
        //判断验证码是否正确
        Jedis resource = jedisPool.getResource();
        String s = resource.get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (s != null && s.equals(validateCode)) {//验证码正确
            //验证码输入正确
            // 判断当前用户是否为会员
            Member member = memberService.findByTelephone(telephone);
            if (member == null) {
                //当前用户不是会员，自动完成注册
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }//不是会员也是会员了
            Cookie cookie = new Cookie("number_telephone",telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);//有效期30天 response.addCookie(cookie);
            // 保存会员信息到Redis中
            response.addCookie(cookie);
            String json = JSON.toJSON(member).toString();
            resource.setex(telephone,1800,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        } else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

    }
}
