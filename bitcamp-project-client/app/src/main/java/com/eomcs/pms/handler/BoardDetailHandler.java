package com.eomcs.pms.handler;

import com.eomcs.pms.dao.BoardDao;
import com.eomcs.pms.domain.Board;
import com.eomcs.util.Prompt;

import java.text.SimpleDateFormat;

public class BoardDetailHandler implements Command {

  //시간 객체를 나중에는 출력할 때 있는 코드에 배치해야 된다
  //이유는 지금처럼 위에 배치하면 thread를 사용할시에 safe하지 못하기 때문이다
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  //핸들러가 사용할 DAO:의존객체(dependency)
  BoardDao boardDao;

  //DAO객체는 이 클래스가 작업하는데 필수 객체이기 때문에
  // 생성자를 통해 반드시 주입받도록 한다


  public BoardDetailHandler(BoardDao boardDao) {
    this.boardDao = boardDao;
  }

  @Override
  public void service() throws Exception {

    System.out.println("[게시글 상세보기]");

    int no = Prompt.inputInt("번호? ");

    Board b = boardDao.findByNo(no);

    if (b == null) {
      System.out.println("해당 번호의 게시글이 없습니다.");
      return;
    }

    System.out.printf("제목: %s\n", b.getTitle());
    System.out.printf("내용: %s\n", b.getContent());
    System.out.printf("작성자: %s\n", b.getWriter().getName());
    System.out.printf("등록일: %s\n", formatter.format(b.getRegisteredDate()));
    System.out.printf("조회수: %s\n", b.getViewCount());
    System.out.printf("좋아요: %s\n", b.getLike());

    boardDao.updateViewCount(no);
  }
}