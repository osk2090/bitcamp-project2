package com.eomcs.pms.service;

import com.eomcs.pms.domain.Task;

import java.util.List;

public interface TaskService {

    public int add(Task task) throws Exception;

    public List<Task> list() throws Exception;

    public Task detail(int no) throws Exception;

    public List<Task> search(int projectNo) throws Exception;

    public int update(Task task) throws Exception;

    public int delete(int no) throws Exception;

    public int deleteByProjectNo(int no) throws Exception;
}