package com.eomcs.pms.handler;

import com.eomcs.pms.domain.Task;
import com.eomcs.util.Prompt;

import java.sql.*;

public class TaskAddHandler implements Command {

  MemberValidator memberValidator;

  public TaskAddHandler(MemberValidator memberValidator) {
    this.memberValidator = memberValidator;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[작업 등록]");

    Task t = new Task();
    t.setContent(Prompt.inputString("내용? "));
    t.setDeadline(Prompt.inputDate("마감일? "));
    t.setStatus(Prompt.inputInt("상태?\n0: 신규\n1: 진행중\n2: 완료\n> "));

    t.setOwner(memberValidator.inputMember("담당자?(취소: 빈 문자열) "));
    if (t.getOwner() == null) {
      System.out.println("작업 등록을 취소하였습니다.");
      return;
    }

    try (Connection con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
         PreparedStatement stmt = con.prepareStatement(
                 "insert into pms_task(content,deadline,owner,status) values (?,?,?,?)",
                 Statement.RETURN_GENERATED_KEYS);
         PreparedStatement stmt2 = con.prepareStatement(
                 "insert into pms_memer_project(member_no,task_no) values (?,?)")) {

      //수동 커밋으로 설정한다
      //-pms-task 테이블과 pms_member_task 테이블에 모두 성공적으로 데이터를 저장했을 때
      //작업을 완료한다
      con.setAutoCommit(false);

      //프로젝트 추가
      stmt.setString(1, t.getContent());
      stmt.setDate(2, t.getDeadline());
      stmt.setInt(3, t.getOwner().getNo());
      stmt.setInt(4, t.getStatus());

      stmt.executeUpdate();

      //프로젝트 데이터의 PK값 알아내기
      try (ResultSet keyRs = stmt.getGeneratedKeys()) {
        keyRs.next();
        t.setNo(keyRs.getInt(1));
      }

      //프로그램 정보 뿐만 아니라 팀원 정보도 정상적으로 입력되었다면
      //실제 데이블에 데이터를 적용한다
      con.commit();//의미:트렉잭션 종료

      System.out.println("작업을 등록했습니다.");
    }
  }
}
