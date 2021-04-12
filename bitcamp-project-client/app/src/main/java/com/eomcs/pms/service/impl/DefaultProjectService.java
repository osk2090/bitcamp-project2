package com.eomcs.pms.service.impl;

import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.dao.TaskDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;
import com.eomcs.pms.service.ProjectService;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
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

            HashMap<String,Object> params = new HashMap<>();
            params.put("projectNo", project.getNo());
            params.put("members", project.getMembers());

            projectDao.insertMembers(params);

            sqlSession.commit();
            return count;
        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }

    public List<Project> list() throws Exception {
        return projectDao.findByKeyword(null);
    }

    public Project get(int no) throws Exception {
        return projectDao.findByNo(no);
    }

    //프로젝트 변경 업무
    public int update(Project project) throws Exception {
        try {
            int count = projectDao.update(project);
            projectDao.deleteMembers(project.getNo());

            HashMap<String,Object> params = new HashMap<>();
            params.put("projectNo", project.getNo());
            params.put("members", project.getMembers());

            projectDao.insertMembers(params);

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
        HashMap<String,Object> params = new HashMap<>();
        params.put("title", title);
        params.put("owner", owner);
        params.put("member", member);
        return projectDao.findByKeywords(params);
    }

    public List<Project> search(String item, String keyword) throws Exception {
        HashMap<String,Object> params = new HashMap<>();
        params.put("item", item);
        params.put("keyword", keyword);
        return projectDao.findByKeyword(params);
    }

    public int deleteMembers(int projectNo) throws Exception {
        int count = projectDao.deleteMembers(projectNo);
        sqlSession.commit();
        return count;
    }

    public int updateMembers(int projectNo, List<Member> members) throws Exception {
        try {
            projectDao.deleteMembers(projectNo);
            HashMap<String,Object> params = new HashMap<>();
            params.put("projectNo", projectNo);
            params.put("members", members);

            int count = projectDao.insertMembers(params);
            sqlSession.commit();
            return count;

        } catch (Exception e) {
            sqlSession.rollback();
            throw e;
        }
    }
}