package com.eomcs.pms.handler;

import com.eomcs.util.Prompt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.sql.Date;

public class ProjectUpdateHandler implements Command {
  @Override
  public void service(DataInputStream in, DataOutputStream out) throws Exception {
    System.out.println("[프로젝트 변경]");

    int no = Prompt.inputInt("번호? ");

    out.writeUTF("project/select");
    out.writeInt(1);
    out.writeUTF(Integer.toString(no));
    out.flush();

    String status = in.readUTF();
    in.readInt();

    if (status.equals("error")) {
      System.out.println(in.readUTF());
      return;
    }

    String[] fields = in.readUTF().split(",");

    String title = Prompt.inputString(String.format("프로젝트명(%s)? ", fields[1]));
    String content = Prompt.inputString(String.format("내용(%s)? ", fields[2]));
    Date startDate = Prompt.inputDate(String.format("시작일(%s)? ", fields[3]));
    Date endDate = Prompt.inputDate(String.format("종료일(%s)? ", fields[4]));

    String owner = MemberValidatorHandler.inputMember(String.format("만든이(%s)?(취소: 빈 문자열) ", fields[5]), in, out);
    if (owner == null) {
      System.out.println("프로젝트 변경을 취소합니다.");
      return;
    }

    String members = MemberValidatorHandler.inputMembers(
            String.format("팀원(%s)?(완료: 빈 문자열) ", fields[6]), in, out);

    String input = Prompt.inputString("정말 변경하시겠습니까?(y/N) ");

    if (!input.equalsIgnoreCase("Y")) {
      System.out.println("프로젝트을 변경하였습니다.");
      return;
    }

    out.writeUTF("project/update");
    out.writeInt(1);
    out.writeUTF(String.format("%d,%s,%s,%s,%s,%s,%s", no, title, content, startDate, endDate, owner, members));
    out.flush();

    status = in.readUTF();
    in.readInt();

    if (status.equals("error")) {
      System.out.println(in.readUTF());
      return;
    }
    System.out.println("프로젝트를 변경하였습니다.");
  }
}








