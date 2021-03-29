package com.eomcs.pms.handler;

import com.eomcs.pms.domain.Project;
import com.eomcs.pms.domain.Task;
import com.eomcs.util.Prompt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaskAddHandler implements Command {

  MemberValidator memberValidator;

  public TaskAddHandler(MemberValidator memberValidator) {
    this.memberValidator = memberValidator;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[작업 등록]");

    //현재 등록된 프로젝트 목록을 가져온다
    List<Project> projects = new ArrayList<>();//그 목록을 한번에 가져올 배열을 만들어준다
    try (Connection con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
         PreparedStatement stmt = con.prepareStatement(
                 "select no,title from oms_project order by title asc");
         ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        Project p = new Project();
        p.setNo(rs.getInt("no"));
        p.setTitle(rs.getString("title"));
        projects.add(p);//no와 title을 배열에 담아 넣는다
      }

      //프로젝트 목록을 출력한다
      System.out.println("프로젝트들:");
      if (projects.size() == 0) {//아까 그 배열에 아무것도 없다면
        System.out.println("현재 등록된 프로젝트가 없습니다.");
        return;
      }
      for (Project p : projects) {//배열에 내용물이 있다면 출력
        System.out.printf("   %d, %s\n", p.getNo(), p.getTitle());
      }

      //작업을 등록할 프로젝트를 선택한다
      int selectProjectNo = 0;
      loop:
      while (true) {
        String input = Prompt.inputString("프로젝트 번호?(취소: 빈 문자열) ");
        if (input.length() == 0) {//input을 그냥 엔터만 눌렀다면
          System.out.println("작업 등록을 취소합니다.");
          return;
        }
        try {//숫자인지 아닌지 확인하는 구간
          selectProjectNo = Integer.parseInt(input);
        } catch (Exception e) {
          System.out.println("숫자를 입력하세요!");
          continue;
        }
        for (Project p : projects) {
          if (p.getNo() == selectProjectNo) {//가져온 배열의 no와 선택한 번호가 같다면
            break loop;//while문을 멈춘다
          }
        }
//        System.out.println("유효하지 않은 프로젝트 번호 입니다.");
      }

      //작업 정보를 입력 받는다
      Task t = new Task();
      t.setContent(Prompt.inputString("내용? "));
      t.setDeadline(Prompt.inputDate("마감일? "));
      t.setStatus(Prompt.inputInt("상태?\n0: 신규\n1: 진행중\n2: 완료\n> "));

      t.setOwner(memberValidator.inputMember("담당자?(취소: 빈 문자열) "));
      if (t.getOwner() == null) {
        System.out.println("작업 등록을 취소하였습니다.");
        return;
      }

      try (PreparedStatement stmt2 = con.prepareStatement(//sql에 데이터를 넣는다
              "insert into pms_task(content,deadline,owner,status,project_no) values (?,?,?,?,?)")) {

        stmt.setString(1, t.getContent());
        stmt.setDate(2, t.getDeadline());
        stmt.setInt(3, t.getOwner().getNo());
        stmt.setInt(4, t.getStatus());
        stmt.setInt(5, selectProjectNo);//위에서 입력받은 숫자가 곧 포함되는 프로젝트의 번호이다
        stmt.executeUpdate();

        System.out.println("작업을 등록했습니다.");
      }
    }
  }
}
