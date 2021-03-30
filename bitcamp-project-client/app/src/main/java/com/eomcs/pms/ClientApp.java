package com.eomcs.pms;

import com.eomcs.pms.dao.BoardDao;
import com.eomcs.pms.handler.*;
import com.eomcs.util.Prompt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class ClientApp {

  // 사용자가 입력한 명령을 저장할 컬렉션 객체 준비
  ArrayDeque<String> commandStack = new ArrayDeque<>();
  LinkedList<String> commandQueue = new LinkedList<>();

  String serverAddress;
  int port;

  public static void main(String[] args) {
    ClientApp app = new ClientApp("localhost", 8888);

    try {
      app.execute();

    } catch (Exception e) {
      System.out.println("클라이언트 실행 중 오류 발생!");
      e.printStackTrace();
    }
  }

  public ClientApp(String serverAddress, int port) {
    this.serverAddress = serverAddress;
    this.port = port;
  }

  public void execute() throws Exception {

    //핸들러가 사용할 DAO객체 준비
    BoardDao boardDao = new BoardDao();

      //dao중 객체가 사용할 connection 객체를 생성하여 주입
    try {
      Connection con = DriverManager.getConnection(
              "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
      BoardDao = con;
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // 사용자 명령을 처리하는 객체를 맵에 보관한다.
    HashMap<String,Command> commandMap = new HashMap<>();

    commandMap.put("/board/add", new BoardAddHandler(boardDao));
    commandMap.put("/board/list", new BoardListHandler(boardDao));
    commandMap.put("/board/detail", new BoardDetailHandler(boardDao));
    commandMap.put("/board/update", new BoardUpdateHandler(boardDao));
    commandMap.put("/board/delete", new BoardDeleteHandler(boardDao));
    commandMap.put("/board/search", new BoardSearchHandler(boardDao));

    commandMap.put("/member/add", new MemberAddHandler());
    commandMap.put("/member/list", new MemberListHandler());
    commandMap.put("/member/detail", new MemberDetailHandler());
    commandMap.put("/member/update", new MemberUpdateHandler());
    commandMap.put("/member/delete", new MemberDeleteHandler());

    MemberValidator memberValidator = new MemberValidator();

    commandMap.put("/project/add", new ProjectAddHandler(memberValidator));
    commandMap.put("/project/list", new ProjectListHandler());
    commandMap.put("/project/detail", new ProjectDetailHandler());
    commandMap.put("/project/delete", new ProjectDeleteHandler());
    commandMap.put("/project/update", new ProjectUpdateHandler(memberValidator));

    commandMap.put("/task/add", new TaskAddHandler(memberValidator));
    commandMap.put("/task/list", new TaskListHandler());
    commandMap.put("/task/detail", new TaskDetailHandler());
    commandMap.put("/task/update", new TaskUpdateHandler(memberValidator));
    commandMap.put("/task/delete", new TaskDeleteHandler());

    try {

      while (true) {

        String command = com.eomcs.util.Prompt.inputString("명령> ");

        if (command.length() == 0) {
          continue;
        }

        // 사용자가 입력한 명령을 보관해둔다.
        commandStack.push(command);
        commandQueue.offer(command);

        try {
          switch (command) {
            case "history":
              printCommandHistory(commandStack.iterator());
              break;
            case "history2": 
              printCommandHistory(commandQueue.iterator());
              break;
            case "quit":
            case "exit":
              System.out.println("안녕!");
              return;
            default:
              Command commandHandler = commandMap.get(command);

              if (commandHandler == null) {
                System.out.println("실행할 수 없는 명령입니다.");
              } else {
                commandHandler.service();
              }
          }
        } catch (Exception e) {
          System.out.println("------------------------------------------");
          System.out.printf("명령어 실행 중 오류 발생: %s\n", e.getMessage());
          System.out.println("------------------------------------------");
        }
        System.out.println(); // 이전 명령의 실행을 구분하기 위해 빈 줄 출력
      }

    } catch (Exception e) {
      System.out.println("서버와 통신 하는 중에 오류 발생!");
    }

    Prompt.close();
  }

  private void printCommandHistory(Iterator<String> iterator) {
    int count = 0;
    while (iterator.hasNext()) {
      System.out.println(iterator.next());
      if ((++count % 5) == 0) {
        String input = Prompt.inputString(": ");
        if (input.equalsIgnoreCase("q")) {
          break;
        }
      }
    }
  }
}
