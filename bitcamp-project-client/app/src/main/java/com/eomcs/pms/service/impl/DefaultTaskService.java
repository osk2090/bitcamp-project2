package com.eomcs.pms.service.impl;

import com.eomcs.pms.dao.TaskDao;
import com.eomcs.pms.domain.Task;
import com.eomcs.pms.service.TaskService;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class DefaultTaskService implements TaskService {

    SqlSession sqlSession;
    TaskDao taskDao;

    public DefaultTaskService(SqlSession sqlSession, TaskDao taskDao) {
        this.sqlSession = sqlSession;
        this.taskDao = taskDao;
    }

    public int add(Task task) throws Exception {
        int count = taskDao.insert(task);
        sqlSession.commit();
        return count;
    }

    public List<Task> list() throws Exception {
        return taskDao.findAll();
    }

    public Task detail(int no) throws Exception {
        return taskDao.findByNo(no);
    }

    public List<Task> search(int projectNo) throws Exception {
        return taskDao.findByProjectNo(projectNo);
    }

    public int update(Task task) throws Exception {
        int count = taskDao.update(task);
        sqlSession.commit();
        return count;
    }

    public int delete(int no) throws Exception {
        int count = taskDao.delete(no);
        sqlSession.commit();
        return count;
    }

    public int deleteByProjectNo(int no) throws Exception {
        int count = taskDao.deleteByProjectNo(no);
        sqlSession.commit();
        return count;
    }
}