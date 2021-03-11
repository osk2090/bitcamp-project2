package com.eomcs.pms.handler;

import com.eomcs.driver.Statement;
import com.eomcs.util.Prompt;

import java.util.Iterator;

public class BoardDetailHandler implements Command {

    @Override
    public void service(Statement stmt) throws Exception {

        System.out.println("[게시글 상세보기]");

        int no = Prompt.inputInt("번호? ");

        Iterator<String> results = stmt.excuteQuery("board/select", Integer.toString(no));

        String[] fields = results.next().split(",");

        System.out.printf("제목: %s\n", fields[1]);
        System.out.printf("내용: %s\n", fields[2]);
        System.out.printf("작성자: %s\n", fields[3]);
        System.out.printf("등록일: %s\n", fields[4]);
        System.out.printf("조회수: %s\n", fields[5]);
    }
}






