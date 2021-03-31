package com.eomcs.pms.handler;

import com.eomcs.pms.dao.TaskDao;
import com.eomcs.pms.domain.Task;
import com.eomcs.util.Prompt;

public class TaskUpdateHandler implements Command {

  TaskDao taskDao;
  MemberValidator memberValidator;

  public TaskUpdateHandler(MemberValidator memberValidator, TaskDao taskDao) {
    this.taskDao = taskDao;
    this.memberValidator = memberValidator;
  }


  @Override
  public void service() throws Exception {
    System.out.println("[작업 변경]");

    int no = Prompt.inputInt("번호? ");

    Task task = taskDao.findByNo(no);

    if (task == null) {
      System.out.println("해당 번호의 작업이 없습니다.");
      return;
    }
    task.setContent(Prompt.inputString(String.format("내용(%s)? ", task.getContent())));
    task.setDeadline(Prompt.inputDate(String.format("마감일(%s)? ", task.getDeadline())));
    task.setStatus(Prompt.inputInt(String.format(
            "상태(%s)?\n0: 신규\n1: 진행중\n2: 완료\n> ",
            Task.getStatusLabel(task.getStatus()))));
    task.setOwner(memberValidator.inputMember(
            String.format("담당자(%s)?(취소: 빈 문자열) ", task.getOwner().getName())));

    if (task.getOwner() == null) {
      System.out.println("작업 변경을 취소합니다.");
      return;
    }

    String input = Prompt.inputString("정말 변경하시겠습니까?(y/N) ");
    if (!input.equalsIgnoreCase("Y")) {
      System.out.println("작업 변경을 취소하였습니다.");
      return;
    }

    taskDao.updqte(task);

    System.out.println("작업을 변경하였습니다.");
  }
}
