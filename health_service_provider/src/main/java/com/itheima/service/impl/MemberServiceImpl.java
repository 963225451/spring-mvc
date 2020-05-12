package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.Utils.MD5Utils;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberdao;

    @Override
    public Member findByTelephone(String telephone) {
        Member member = memberdao.fandByPhoneNumber(telephone);
        return member;
    }

    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if(password!=null){
            String s = MD5Utils.md5(password);
            member.setPassword(s);

        }
        memberdao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonths(List<String> months) {
        List list =new ArrayList();
        for (String month : months) {
            Integer count=memberdao.findMemberCountBeforeDate(month);
            list.add(count);
        }

        return list;
    }

    @Override
    public List<Member> todayNewMember(String today) {
        return  memberdao.todayNewMember(today);

    }

//    public static void main(String[] args) {
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
////        String encode = bCryptPasswordEncoder.encode("admin");
//        boolean admin = bCryptPasswordEncoder.matches("admin", "$2a$10$eJm6UUTPsV3mm1AKxxIf3.//mGbz2pgsKTetR9HjnJmwn60Hoyz/e");
////        System.out.println(encode);
//        System.out.println(admin);
//    }
}
