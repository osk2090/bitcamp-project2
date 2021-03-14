package com.eomcs.pms;

import com.eomcs.pms.driver.Statement;
import com.eomcs.pms.handler.*;
import com.eomcs.util.Prompt;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class ClientApp {
    String serverAddress;
    int port;

    // 사용자가 입력한 명령을 저장할 컬렉션 객체 준비
    ArrayDeque<String> commandStack = new ArrayDeque<>();
    LinkedList<String> commandQueue = new LinkedList<>();

    public static void main(String[] args) {
        ClientApp app = new ClientApp("localhost", 8888);

        try {
            app.excute();//실행
        } catch (Exception e) {
            System.out.println("클라이언트 실행 중 오류 발생!");
            e.printStackTrace();
        }
    }

    public ClientApp(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public void excute() throws Exception {
        //서버와 통신하는 것을 대행해 줄 객체를 준비한다
        Statement stmt = new Statement(serverAddress, port);

        // 사용자 명령을 처리하는 객체를 맵에 보관한다.
        HashMap<String, Command> commandMap = new HashMap<>();

        commandMap.put("/board/add", new BoardAddHandler(stmt));
        commandMap.put("/board/list", new BoardListHandler(stmt));
        commandMap.put("/board/detail", new BoardDetailHandler(stmt));
        commandMap.put("/board/update", new BoardUpdateHandler(stmt));
        commandMap.put("/board/delete", new BoardDeleteHandler(stmt));
        commandMap.put("/board/search", new BoardSearchHandler(stmt));

        commandMap.put("/member/add", new MemberAddHandler(stmt));
        commandMap.put("/member/list", new MemberListHandler(stmt));
        commandMap.put("/member/detail", new MemberDetailHandler(stmt));
        commandMap.put("/member/update", new MemberUpdateHandler(stmt));
        commandMap.put("/member/delete", new MemberDeleteHandler(stmt));

        MemberValidatorHandler memberValidatorHandler = new MemberValidatorHandler(stmt);

        commandMap.put("/project/add", new ProjectAddHandler(stmt, memberValidatorHandler));
        commandMap.put("/project/list", new ProjectListHandler(stmt));
        commandMap.put("/project/detail", new ProjectDetailHandler(stmt));
        commandMap.put("/project/update", new ProjectUpdateHandler(stmt, memberValidatorHandler));
        commandMap.put("/project/delete", new ProjectDeleteHandler(stmt));

        commandMap.put("/task/add", new TaskAddHandler(stmt, memberValidatorHandler));
        commandMap.put("/task/list", new TaskListHandler(stmt));
        commandMap.put("/task/detail", new TaskDetailHandler(stmt));
        commandMap.put("/task/update", new TaskUpdateHandler(stmt, memberValidatorHandler));
        commandMap.put("/task/delete", new TaskDeleteHandler(stmt));

        try {

            while (true) {

                String command = Prompt.inputString("명령> ");

                if (command.length() == 0) {
                    continue;
                }

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
                            //서버에게 종료한다고 메시지를 보낸다
                            stmt.executeUpdate("quit");
                            System.out.println("안녕!");
                            return;
                        default:
                            Command commandHandler = commandMap.get(command);

                            if (commandHandler == null) {
                                System.out.println("실행할 수 없는 명령입니다.");
                            } else {
                                commandHandler.service();
                                // 이제 명령어와 그 명령어를 처리하는 핸들러를 추가할 때 마다
                                // case 문을 추가할 필요가 없다.
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
            System.out.println("서버와 통신하는 중에 오류 발생!");
        }
        stmt.close();
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