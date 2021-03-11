package com.eomcs.pms.handler;

import com.eomcs.pms.domain.Task;
import com.eomcs.util.Prompt;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class TaskDetailHandler implements Command {
  @Override
  public void service(DataInputStream in, DataOutputStream out) throws Exception {
      System.out.println("[작업 상세보기]");

      int no = Prompt.inputInt("번호? ");

      out.writeUTF("task/select");
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

      System.out.printf("내용: %s\n", fields[1]);
      System.out.printf("마감일: %s\n", fields[2]);
      System.out.printf("상태: %s\n", Task.getStatusLabel(Integer.parseInt(fields[3])));
      System.out.printf("담당자: %s\n", fields[4]);
  }
}
