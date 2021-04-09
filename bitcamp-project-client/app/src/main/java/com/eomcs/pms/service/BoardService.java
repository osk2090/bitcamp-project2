package com.eomcs.pms.service;

import com.eomcs.pms.domain.Board;

import java.util.List;

// 서비스 객체
// - 비즈니스 로직을 담고 있다.
// - 업무에 따라 트랜잭션을 제어하는 일을 한다.
// - 서비스 객체의 메서드는 가능한 업무 관련 용어를 사용하여 메서드를 정의한다.
//
public interface BoardService {

    int add(Board board) throws Exception;

    List<Board> list() throws Exception;

    Board get(int no) throws Exception;

    int update(Board board) throws Exception;

    int delete(int no) throws Exception;

    List<Board> search(String keyword) throws Exception;
}







