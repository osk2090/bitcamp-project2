package com.eomcs.pms.dao;

import com.eomcs.pms.domain.Board;

import java.util.List;

// BoardDao 의 규칙 정의
public interface BoardDao {

  int insert(Board board) throws Exception;

  List<Board> findByKeyword(String keyword) throws Exception;

  Board findByNo(int no) throws Exception;

  int update(Board board) throws Exception;

  int updateViewCount(int no) throws Exception;

  int delete(int no) throws Exception;

}












