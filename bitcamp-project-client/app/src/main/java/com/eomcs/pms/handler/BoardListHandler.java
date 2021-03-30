package com.eomcs.pms.handler;

import com.eomcs.pms.dao.BoardDao;
import com.eomcs.pms.domain.Board;

import java.util.List;

public class BoardListHandler implements Command {
  //핸들러가 사용할 DAO:의존객체(dependency)
  BoardDao boardDao;

  //DAO객체는 이 클래스가 작업하는데 필수 객체이기 때문에
  // 생성자를 통해 반드시 주입받도록 한다


  public BoardListHandler(BoardDao boardDao) {
    this.boardDao = boardDao;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[게시글 목록]");

    List<Board> boards = boardDao.findAll();
    for (Board b : boards) {
      System.out.printf("%d, %s, %s, %s, %d\n",
              b.getNo(),
              b.getTitle(),
              b.getWriter().getName(),
              b.getRegisteredDate(),
              b.getViewCount());
    }
  }
}





