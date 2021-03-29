package com.eomcs.pms.handler;

import com.eomcs.pms.domain.Task;
import com.eomcs.util.Prompt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TaskDetailHandler implements Command {

  @Override
  public void service() throws Exception {
    System.out.println("[작업 상세보기]");

    int no = Prompt.inputInt("번호? ");

    try (Connection con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
        PreparedStatement stmt = con.prepareStatement(
            "select "
                + "   t.no,"//번호와
                + "   t.content,"//컨텐츠와
                + "   t.deadline,"//납기
                + "   t.status,"//현황
                + "   m.no as owner_no,"//pms_member에 있는 no를 owner_no로 선언
                + "   m.name as owner_name"//pms_member에 있는 name를 owner_name으로 선언
                + " from pms_task t "//t는 pms_task를 말하고
                + "   inner join pms_member m on t.owner=m.no"//pms_member의 no와 pms_task의 owner의 값이 값다면 같이 표현한다
                + " where t.no=?")) {//task에 있는 no와 입력되는 값을 비교한다

      stmt.setInt(1, no);//no를 보내서 no인덱스에 있는 값을 가져온다

      try (ResultSet rs = stmt.executeQuery()) {
        if (!rs.next()) {
          System.out.println("해당 번호의 작업이 없습니다.");
          return;
        }

        System.out.printf("내용: %s\n", rs.getString("content"));//결과가 있는부분에서만 getXxxx로 리턴받는다
        System.out.printf("마감일: %s\n", rs.getDate("deadline"));
        System.out.printf("상태: %s\n", Task.getStatusLabel(rs.getInt("status")));
        System.out.printf("담당자: %s\n", rs.getString("owner_name"));
      }
    }
  }
}
