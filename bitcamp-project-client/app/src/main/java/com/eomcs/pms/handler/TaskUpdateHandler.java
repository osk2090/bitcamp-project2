package com.eomcs.pms.handler;

import com.eomcs.pms.domain.Task;
import com.eomcs.util.Prompt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TaskUpdateHandler implements Command {//

  MemberValidator memberValidator;

  public TaskUpdateHandler(MemberValidator memberValidator) {
    this.memberValidator = memberValidator;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[작업 변경]");

    int no = Prompt.inputInt("번호? ");

    try (Connection con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
         PreparedStatement stmt = con.prepareStatement(
                 "select no,content,deadline,owner,status from pms_task where no=?");
         PreparedStatement stmt2 = con.prepareStatement(
                 "update pms_task set content = ?,deadline = ?,owner= ? ,status= ?")) {
      Task task = new Task();

      stmt.setInt(1, no);
      try (ResultSet rs = stmt.executeQuery()) {
        if (!rs.next()) {
          System.out.println("해당 번호의 작업이 없습니다.");
          return;
        }
        task.setNo(no);
        task.setContent(rs.getString("content"));
        task.setDeadline(rs.getDate("deadline"));
        task.setOwner(rs.getString("owner"));
        task.setStatus(rs.getInt("status"));
      }
      task.setContent(Prompt.inputString(String.format("내용(%s)? ", task.getContent())));
      task.setDeadline(Prompt.inputDate(String.format("마감일(%s)? ", task.getDeadline())));
      task.setOwner(memberValidator.inputMember(String.format("담당자(%s)?(취소: 빈 문자열) ", task.getOwner())));

      if (task.getOwner() == null) {
        System.out.println("작업 변경을 취소합니다.");
        return;
      }

      task.setStatus(Prompt.inputInt(String.format(
              "상태(%s)?\n0: 신규\n1: 진행중\n2: 완료\n> ",
              Task.getStatusLabel(task.getStatus()))));

      String input = Prompt.inputString("정말 변경하시겠습니까?(y/N) ");
      if (!input.equalsIgnoreCase("Y")) {
        System.out.println("작업 변경을 취소하였습니다.");
        return;
      }

      stmt2.setString(1, task.getContent());
      stmt2.setDate(2, task.getDeadline());
      stmt2.setString(3, task.getOwner());
      stmt2.setInt(4, task.getStatus());

      System.out.println("작업을 변경하였습니다.");
    }
  }
}
