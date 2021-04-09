package com.eomcs.pms.service.impl;

import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.dao.TaskDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;
import com.eomcs.pms.service.ProjectService;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class DefaultProjectService implements ProjectService {

    SqlSession sqlSession;
    ProjectDao projectDao;
    TaskDao taskDao;

    public DefaultProjectService(SqlSession sqlSession, ProjectDao projectDao, TaskDao taskDao) {
        this.sqlSession = sqlSession;
        this.projectDao = projectDao;
        this.taskDao = taskDao;
    }

    //프로젝트 등록 업무
    public int add(Project project) throws Exception {
        try {
            // 1) 프로젝트 정보를 입력한다.
            int count = projectDao.insert(project);
            projectDao.insertMembers(project.getNo(), project.getMembers());

            sqlSession.commit();
            return count;
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }

    public List<Project> list() throws Exception {
        return projectDao.findByKeyword(null, null);
    }

    public Project get(int no) throws Exception {
        return projectDao.findByNo(no);
    }

    //프로젝트 변경 업무
    public int update(Project project) throws Exception {
        try {
            int count = projectDao.update(project);
            projectDao.deleteMembers(project.getNo());
            projectDao.insertMembers(project.getNo(), project.getMembers());
            sqlSession.commit();
            return count;
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }

    //프로젝트 삭제 업무
    public int delete(int no) throws Exception {
        try {
            //프로젝트의 모든 작업 삭제
            taskDao.findByProjectNo(no);

            //프로젝트 멤버 삭제
            projectDao.deleteMembers(no);

            //프로젝트 삭제
            int count = projectDao.delete(no);
            sqlSession.commit();
            return count;
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }

    // 찾기
    public List<Project> search(String title, String owner, String member) throws Exception {
        return projectDao.findByKeywords(title, owner, member);
    }

    public List<Project> search(String item, String keyword) throws Exception {
        return projectDao.findByKeyword(item, keyword);
    }

    public int deleteMembers(int projectNo) throws Exception {
        int count = projectDao.deleteMembers(projectNo);
        sqlSession.commit();
        return count;
    }

    public int updateMembers(int projectNo, List<Member> members) throws Exception {
        try {
            projectDao.deleteMembers(projectNo);
            int count = projectDao.insertMembers(projectNo, members);
            sqlSession.commit();
            return count;

        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }
}