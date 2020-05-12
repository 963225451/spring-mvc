package com.itheima.dao;

import com.itheima.pojo.Member;
import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

public interface MemberDao {

    Member fandByPhoneNumber(String telephone);

    void add(Member member);

    Member fandById(Integer id);

    Integer findMemberCountBeforeDate(String months);

    List<Member> todayNewMember(String today);

    List<Member> findMemberAfterDate(String dateString);

    List<Member> findMemberOfMonth(String s);
}
