package com.eomcs.pms.handler;

import com.eomcs.driver.Statement;
import com.eomcs.util.Prompt;

public class ProjectDeleteHandler implements Command {
  Statement stmt;

  public ProjectDeleteHandler(Statement stmt) {
    this.stmt = stmt;
  }

  @Override
  public void service() throws Exception {

    System.out.println("[프로젝트 삭제]");

    int no = Prompt.inputInt("번호? ");
    stmt.excuteQuery("project/select", Integer.toString(no));

    String input = Prompt.inputString("정말 삭제하시겠습니까?(y/N) ");

    if (!input.equalsIgnoreCase("Y")) {
      System.out.println("프로젝트을 삭제하였습니다.");
      return;
    }
    stmt.excuteUpdate("project/delete", Integer.toString(no));
    System.out.println("프로젝트를 삭제하였습니다.");
  }
}








