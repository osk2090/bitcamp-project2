package com.eomcs.pms.handler;

import com.eomcs.util.Prompt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ProjectDeleteHandler implements Command {

  @Override
  public void service() throws Exception {
    System.out.println("[프로젝트 삭제]");

    int no = Prompt.inputInt("번호? ");

    String input = Prompt.inputString("정말 삭제하시겠습니까?(y/N) ");
    if (!input.equalsIgnoreCase("Y")) {
      System.out.println("프로젝트 삭제를 취소하였습니다.");
      return;
    }

    try (Connection con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
         PreparedStatement stmt = con.prepareStatement(
                 "delete from pms_member_project where no=?");
         PreparedStatement stmt2 = con.prepareStatement(
                 "delete from pms_project where no=?")) {

      con.setAutoCommit(false);

      //프로젝트에 소속된 팀원 정보 삭제
      stmt.setInt(1, no);
      stmt.executeUpdate();

      stmt2.setInt(1, no);
      if (stmt.executeUpdate() == 0) {
        System.out.println("해당 번호의 프로젝트가 없습니다.");
      } else {
        con.commit();
        System.out.println("프로젝트를 삭제하였습니다.");
      }
    }
  }
}








