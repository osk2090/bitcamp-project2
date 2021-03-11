package com.eomcs.pms.handler;

import com.eomcs.pms.domain.Task;
import com.eomcs.util.Prompt;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class TaskAddHandler implements Command {

  @Override
  public void service(DataInputStream in, DataOutputStream out) throws Exception {
    System.out.println("[작업 등록]");

    Task t = new Task();
    t.setNo(Prompt.inputInt("번호? "));
    t.setContent(Prompt.inputString("내용? "));
    t.setDeadline(Prompt.inputDate("마감일? "));
    t.setStatus(Prompt.inputInt("상태?\n0: 신규\n1: 진행중\n2: 완료\n> "));
    if (t.getOwner() == null) {
      System.out.println("작업 등록을 취소하였습니다.");
      return;
    }

    out.writeUTF("task/insert");
    out.writeInt(1);
    out.writeUTF(String.format("%s,%s,%s,%s", t.getContent(), t.getDeadline(), t.getStatus(), t.getOwner()));
    out.flush();

    String status = in.readUTF();
    in.readInt();

    if (status.equals("error")) {
      System.out.println(in.readUTF());
      return;
    }
    System.out.println("작업을 등록했습니다.");
  }
}
