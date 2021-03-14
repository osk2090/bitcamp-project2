package com.eomcs.pms.handler;

import com.eomcs.pms.domain.Board;
import com.eomcs.pms.driver.Statement;
import com.eomcs.util.Prompt;

import java.sql.Date;

public class BoardAddHandler implements Command {

  Statement stmt;

  public BoardAddHandler(Statement stmt) {
    this.stmt = stmt;
  }

  @Override
  public void service() throws Exception {

    System.out.println("[게시글 등록]");

    Board b = new Board();

    b.setTitle(Prompt.inputString("제목? "));
    b.setContent(Prompt.inputString("내용? "));
    b.setWriter(Prompt.inputString("작성자? "));
    b.setRegisteredDate(new Date(System.currentTimeMillis()));

    stmt.executeUpdate("board/insert",
            String.format("%s,%s,%s", b.getTitle(), b.getContent(), b.getWriter()));

    System.out.println("게시글을 등록하였습니다.");
  }
}






