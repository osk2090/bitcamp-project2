package com.eomcs.pms.handler;

import com.eomcs.pms.dao.TaskDao;
import com.eomcs.util.Prompt;

public class TaskDeleteHandler implements Command {

  TaskDao taskDao;

  public TaskDeleteHandler(TaskDao taskDao) {
    this.taskDao = taskDao;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[작업 삭제]");

    int no = Prompt.inputInt("번호? ");

    String input = Prompt.inputString("정말 삭제하시겠습니까?(y/N) ");
    if (!input.equalsIgnoreCase("Y")) {
      System.out.println("작업 삭제를 취소하였습니다.");
      return;
    }

    if (taskDao.delete(no) == 0) {
      System.out.println("ㅎㅐ당 번호의 프로젝트가 없습니다.");
    } else {
      System.out.println("프로젝트를 삭제하였습니다.");
    }
  }
}
