package com.eomcs.pms.dao;

import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;

import java.util.List;

public interface ProjectDao {

  int insert(Project project) throws Exception;

  Project findByNo(int no) throws Exception;

  int update(Project project) throws Exception;

  int delete(int no) throws Exception;

  int insertMember(int projectNo, int memberNo) throws Exception;

  int insertMembers(int projectNo, List<Member> members) throws Exception;

  List<Member> findAllMembers(int projectNo) throws Exception;

  int deleteMembers(int projectNo) throws Exception;

  List<Project> findByKeyword(String item, String keyword) throws Exception;

  List<Project> findByKeywords(String title, String owner, String member) throws Exception;
}












