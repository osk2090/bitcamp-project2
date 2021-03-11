package com.eomcs.pms.handler;

import com.eomcs.driver.Statement;
import com.eomcs.util.Prompt;

public class MemberUpdateHandler implements Command {
    @Override
    public void service(Statement stmt) throws Exception {

        System.out.println("[회원 변경]");

        int no = Prompt.inputInt("번호? ");

        String[] fields = stmt.excuteQuery("member/select", Integer.toString(no)).next().split(",");

        String name = Prompt.inputString(String.format("이름(%s)? ", fields[1]));
        String email = Prompt.inputString(String.format("이메일(%s)? ", fields[2]));
        String photo = Prompt.inputString(String.format("사진(%s)? ", fields[4]));
        String tel = Prompt.inputString(String.format("전화(%s)? ", fields[5]));

        String input = Prompt.inputString("정말 변경하시겠습니까?(y/N) ");
        if (!input.equalsIgnoreCase("Y")) {
            System.out.println("회원 변경을 취소하였습니다.");
            return;
        }
        stmt.excuteUpdate("member/select", String.format("%d,%s,%s,%s,%s", no, name, email, photo, tel));
        System.out.println("회원을 변경하였습니다.");
    }
}






