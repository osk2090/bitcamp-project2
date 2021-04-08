package com.eomcs.pms.handler;

import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;
import com.eomcs.pms.service.ProjectService;
import com.eomcs.util.Prompt;

import java.util.List;

public class ProjectMemberUpdateHandler implements Command {

  ProjectService projectService;
  MemberValidator memberValidator;

  public ProjectMemberUpdateHandler(ProjectService projectService, MemberValidator memberValidator) {
    this.projectService = projectService;
    this.memberValidator = memberValidator;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[프로젝트 멤버 변경]");

    int no = Prompt.inputInt("프로젝트 번호? ");

    Project project = projectService.findByNo(no);

    if (project == null) {
      System.out.println("해당 번호의 프로젝트가 없습니다.");
      return;
    }

    System.out.printf("프로젝트 명: %s\n", project.getTitle());
    System.out.println("멤버:");
    for (Member m : project.getMembers()) {
      System.out.printf("  %s(%d)\n", m.getName(), m.getNo());
    }
    System.out.println();


    //프로젝트 팀원 정보를 입력받는다
    System.out.println("프로젝트의 멤버를 새로 등록하세요.");
    List<Member> members = memberValidator.inputMembers("팀원?(완료: 빈 문자열) ");

    String input = Prompt.inputString("정말 변경하시겠습니까?(y/N) ");
    if (!input.equalsIgnoreCase("Y")) {
      System.out.println("프로젝트의 멤버 변경을 취소하였습니다.");
      return;
    }

    //프로젝트의 기존 멤버를 모두 삭제한다
    projectService.deleteMembers(no);

    //새 프로젝트 멤버를 등록한다
    projectService.insertMembers(no, members);

    System.out.println("프로젝트 멤버를 변경하였습니다.");
  }
}