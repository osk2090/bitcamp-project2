package com.eomcs.pms.handler;

import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;
import com.eomcs.util.Prompt;

import java.util.List;

public class ProjectSearchHandler implements Command {

    ProjectDao projectDao;

    public ProjectSearchHandler(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    @Override
    public void service() throws Exception {
        System.out.println("[프로젝트 검색]");

        String item = Prompt.inputString("항목(1:프로젝트명, 2:관리자명, 3:팀원, 그 외: 전체)? ");
        String keyword = null;

        if (item.equals("1") ||
                item.equals("2") ||
                item.equals("3")) {
            keyword = Prompt.inputString("검색어? ");
        }

        List<Project> projects = projectDao.findByKeyword(item, keyword);

        for (Project p : projects) {

            StringBuilder strBuilder = new StringBuilder();
            List<Member> members = p.getMembers();
            for (Member m : members) {
                if (strBuilder.length() > 0) {
                    strBuilder.append("/");
                }
                strBuilder.append(m.getName());
            }

            System.out.printf("%d, %s, %s, %s, %s, [%s]\n",
                    p.getNo(),
                    p.getTitle(),
                    p.getStartDate(),
                    p.getEndDate(),
                    p.getOwner().getName(),
                    strBuilder.toString());
        }
    }
}