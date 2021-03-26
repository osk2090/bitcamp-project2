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

    try (Connection con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/studydb?user=study&password=1111"
    );
         PreparedStatement stmt = con.prepareStatement(
                 "delete from pms_project where no=?"
         )) {

      stmt.setInt(1, no);
      if (stmt.executeUpdate() == 0) {
        System.out.println("해당 번호의 프로젝트가 없습니다.");
      } else {
        System.out.println("프로젝트를 삭제하였습니다.");
      }
    }
  }
}








