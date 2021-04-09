package com.eomcs.pms.service;

import com.eomcs.pms.domain.Member;

import java.util.List;

//서비스 객체
//비즈니스 로직을 담고 있다
//업무에 따라 트랜잭션을 제어하는 일을 한다
//
public interface MemberService {

    int add(Member member) throws Exception;

    List<Member> list() throws Exception;

    Member get(int no) throws Exception;

    Member search(String name) throws Exception;

    int update(Member member) throws Exception;

    int delete(int no) throws Exception;
}