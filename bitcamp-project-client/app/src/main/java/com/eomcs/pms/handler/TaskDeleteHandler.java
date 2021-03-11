package com.eomcs.pms.handler;

import com.eomcs.util.Prompt;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class TaskDeleteHandler implements Command {

  @Override
  public void service(DataInputStream in, DataOutputStream out) throws Exception {

      System.out.println("[작업 삭제]");

      int no = Prompt.inputInt("번호? ");

      out.writeUTF("task/select");
      out.writeInt(1);
      out.writeUTF(Integer.toString(no));
      out.flush();

      String status = in.readUTF();
      in.readInt();
      String data = in.readUTF();

      if (status.equals("error")) {
        System.out.println(data);
        return;
      }

      String input = Prompt.inputString("정말 삭제하시겠습니까?(y/N) ");
      if (!input.equalsIgnoreCase("Y")) {
        System.out.println("작업을 삭제하였습니다.");
        return;
      }

      out.writeUTF("task/delete");
      out.writeInt(1);
      out.writeUTF(Integer.toString(no));
      out.flush();

      status = in.readUTF();
      in.readInt();

      if (status.equals("error")) {
        System.out.println(in.readUTF());
        return;
      }
      System.out.println("작업을 삭제하였습니다.");
  }
}
