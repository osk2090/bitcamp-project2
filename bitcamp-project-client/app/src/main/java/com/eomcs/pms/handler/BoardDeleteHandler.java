package com.eomcs.pms.handler;

import com.eomcs.driver.Statement;
import com.eomcs.util.Prompt;

public class BoardDeleteHandler implements Command {

    Statement stmt;

    public BoardDeleteHandler(Statement stmt) {
        this.stmt = stmt;
    }

    public void service() throws Exception {

        System.out.println("[게시글 삭제]");

        int no = Prompt.inputInt("번호? ");

        stmt.excuteQuery("board/select", Integer.toString(no));

        String input = Prompt.inputString("정말 삭제하시겠습니까?(y/N) ");
        if (!input.equalsIgnoreCase("Y")) {
            System.out.println("게시글 삭제를 취소하였습니다.");
            return;
        }
        stmt.excuteUpdate("board/delete", Integer.toString(no));
        System.out.println("게시글을 삭제하였습니다.");
    }
}