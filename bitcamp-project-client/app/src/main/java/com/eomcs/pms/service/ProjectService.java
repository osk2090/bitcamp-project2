package com.eomcs.pms.service;

import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class ProjectService {

    SqlSession sqlSession;

    ProjectDao projectDao;

    public ProjectService(SqlSession sqlSession, ProjectDao projectDao) {
        this.sqlSession = sqlSession;
        this.projectDao = projectDao;
    }

    //프로젝트 등록 업무
    public int add(Project project)throws Exception {
        int count = projectDao.insert(project);
        sqlSession.commit();
        return count;
    }

    //프로젝트 변경 업무
    public int update(Project project)throws Exception {
        int count = projectDao.update(project);
        sqlSession.commit();
        return count;
    }

    //프로젝트 삭제 업무
    public int delete(int no) throws Exception {
        int count = projectDao.delete(no);
        sqlSession.commit();
        return count;
    }

    public int insertMember(int projectNo,int memberNo)throws Exception {
        int count = projectDao.insertMember(projectNo, memberNo);
        sqlSession.commit();
        return count;
    }

    public int insertMembers(int projectNo, List<Member> members) throws Exception {
        int count = projectDao.insertMembers(projectNo, members);
        sqlSession.commit();
        return count;
    }

    public int deleteMembers(int projectNo) throws Exception {
        int count = projectDao.deleteMembers(projectNo);
        sqlSession.commit();
        return count;
    }


    public List<Project> findByKeyword(String item, String keyword) throws Exception {

        HashMap<String,Object> params = new HashMap<>();
        params.put("item", item);
        params.put("keyword", keyword);

        List<Project> count = sqlSession.selectList("ProjectMapper.findByKeyword", params);
        sqlSession.commit();
        return count;
    }

    public List<Project> findByKeywords(String title, String owner, String member) throws Exception {

        HashMap<String,Object> params = new HashMap<>();
        params.put("title", title);
        params.put("owner", owner);
        params.put("member", member);

        List<Project> count = sqlSession.selectList("ProjectMapper.findByKeywords", params);
        sqlSession.commit();
        return count;
    }

    public Project findByNo(int no) throws Exception {
        Project count = sqlSession.selectOne("ProjectMapper.findByNo", no);
        sqlSession.commit();
        return count;
    }
}