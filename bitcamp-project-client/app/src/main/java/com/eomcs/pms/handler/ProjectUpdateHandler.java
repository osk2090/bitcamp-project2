package com.eomcs.pms.handler;

import com.eomcs.driver.Statement;
import com.eomcs.util.Prompt;

import java.sql.Date;

public class ProjectUpdateHandler implements Command {
  Statement stmt;
  MemberValidatorHandler memberValidatorHandler;

  public ProjectUpdateHandler(Statement stmt, MemberValidatorHandler memberValidatorHandler) {
    this.stmt = stmt;
    this.memberValidatorHandler = memberValidatorHandler;
  }

  @Override
  public void service() throws Exception {

    System.out.println("[프로젝트 변경]");

    int no = Prompt.inputInt("번호? ");

    String[] fields = stmt.excuteQuery("project/select", Integer.toString(no)).next().split(",");

    String title = Prompt.inputString(String.format("프로젝트명(%s)? ", fields[1]));
    String content = Prompt.inputString(String.format("내용(%s)? ", fields[2]));
    Date startDate = Prompt.inputDate(String.format("시작일(%s)? ", fields[3]));
    Date endDate = Prompt.inputDate(String.format("종료일(%s)? ", fields[4]));

    String owner = memberValidatorHandler.inputMember(String.format("만든이(%s)?(취소: 빈 문자열) "));

    if (owner == null) {
      System.out.println("프로젝트 변경을 취소합니다.");
      return;
    }

    String members = memberValidatorHandler.inputMembers(
            String.format("팀원(%s)?(완료: 빈 문자열) "));

    String input = Prompt.inputString("정말 변경하시겠습니까?(y/N) ");

    if (!input.equalsIgnoreCase("Y")) {
      System.out.println("프로젝트을 변경하였습니다.");
      return;
    }
    stmt.excuteUpdate("project/update", String.format("%d,%s,%s,%s,%s,%s,%s", no, title, content, startDate, endDate, owner, members));
    System.out.println("프로젝트를 변경하였습니다.");
  }
}








