package com.eomcs.pms.handler;

import com.eomcs.pms.driver.Statement;

import java.util.Iterator;

public class BoardListHandler implements Command {

  Statement stmt;

  public BoardListHandler(Statement stmt) {
    this.stmt = stmt;
  }

  @Override
  public void service() throws Exception {

    System.out.println("[게시글 목록]");

    Iterator<String> results = stmt.executeQuery("board/selectall");

    while (results.hasNext()) {
      String[] fields = results.next().split(",");//,콤마로 자른다음에 배열로 저장된 데이터를 재나열한다

      System.out.printf("%s, %s, %s, %s, %s\n",
              fields[0],
              fields[1],
              fields[2],
              fields[3],
              fields[4]);
    }
  }
}