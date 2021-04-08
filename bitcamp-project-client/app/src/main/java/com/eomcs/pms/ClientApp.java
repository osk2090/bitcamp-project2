package com.eomcs.pms;

import com.eomcs.pms.dao.BoardDao;
import com.eomcs.pms.dao.MemberDao;
import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.dao.TaskDao;
import com.eomcs.pms.dao.mariadb.BoardDaoImpl;
import com.eomcs.pms.dao.mariadb.MemberDaoImpl;
import com.eomcs.pms.dao.mariadb.ProjectDaoImpl;
import com.eomcs.pms.dao.mariadb.TaskDaoImpl;
import com.eomcs.pms.handler.*;
import com.eomcs.pms.service.BoardService;
import com.eomcs.pms.service.MemberService;
import com.eomcs.pms.service.ProjectService;
import com.eomcs.util.Prompt;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
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

    // Mybatis 설정 파일을 읽을 입력 스트림 객체 준비
    InputStream mybatisConfigStream = Resources.getResourceAsStream(
            "com/eomcs/pms/conf/mybatis-config.xml");

    // SqlSessionFactory 객체 준비
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(mybatisConfigStream);

    // DAO가 사용할 SqlSession 객체 준비
    // => 수동 commit 으로 동작하는 SqlSession 객체를 준비한다.
    SqlSession sqlSession = sqlSessionFactory.openSession(false);

    // DB Connection 객체 생성
    Connection con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");


    // 핸들러가 사용할 DAO 객체 준비
    BoardDao boardDao = new BoardDaoImpl(sqlSession);
    MemberDao memberDao = new MemberDaoImpl(sqlSession);
    ProjectDao projectDao = new ProjectDaoImpl(sqlSession);
    TaskDao taskDao = new TaskDaoImpl(sqlSession);

    BoardService boardService = new BoardService(sqlSession, boardDao);
    MemberService memberService = new MemberService(sqlSession, memberDao);
    ProjectService projectService = new ProjectService(sqlSession, projectDao);


    // 사용자 명령을 처리하는 객체를 맵에 보관한다.
    HashMap<String,Command> commandMap = new HashMap<>();

    commandMap.put("/board/add", new BoardAddHandler(boardService));
    commandMap.put("/board/list", new BoardListHandler(boardService));
    commandMap.put("/board/detail", new BoardDetailHandler(boardService));
    commandMap.put("/board/update", new BoardUpdateHandler(boardService));
    commandMap.put("/board/delete", new BoardDeleteHandler(boardService));
    commandMap.put("/board/search", new BoardSearchHandler(boardService));

    commandMap.put("/member/add", new MemberAddHandler(memberService));
    commandMap.put("/member/list", new MemberListHandler(memberService));
    commandMap.put("/member/detail", new MemberDetailHandler(memberService));
    commandMap.put("/member/update", new MemberUpdateHandler(memberService));
    commandMap.put("/member/delete", new MemberDeleteHandler(memberService));

    MemberValidator memberValidator = new MemberValidator(memberService);

    commandMap.put("/project/add", new ProjectAddHandler(projectService, memberValidator));
    commandMap.put("/project/list", new ProjectListHandler(projectService));
    commandMap.put("/project/detail", new ProjectDetailHandler(projectService));
    commandMap.put("/project/update", new ProjectUpdateHandler(projectService, memberValidator));
    commandMap.put("/project/memberUpdate", new ProjectMemberUpdateHandler(projectService, memberValidator));
    commandMap.put("/project/memberDelete", new ProjectMemberDeleteHandler(projectService));
    commandMap.put("/project/delete", new ProjectDeleteHandler(projectService, taskDao));
    commandMap.put("/project/search", new ProjectSearchHandler(projectService));
    commandMap.put("/project/detailSearch", new ProjectDetailSearchHandler(projectService));

    commandMap.put("/task/add", new TaskAddHandler(taskDao, projectDao, memberValidator));
    commandMap.put("/task/list", new TaskListHandler(taskDao));
    commandMap.put("/task/detail", new TaskDetailHandler(taskDao));
    commandMap.put("/task/update", new TaskUpdateHandler(taskDao, projectDao, memberValidator));
    commandMap.put("/task/delete", new TaskDeleteHandler(taskDao));

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

    con.close();
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
