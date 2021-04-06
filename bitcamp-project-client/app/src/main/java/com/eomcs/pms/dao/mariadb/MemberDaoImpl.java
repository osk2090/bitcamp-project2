package com.eomcs.pms.dao.mariadb;

import com.eomcs.pms.dao.MemberDao;
import com.eomcs.pms.domain.Member;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MemberDaoImpl implements MemberDao {

  SqlSession sqlSession;

  public MemberDaoImpl(SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  @Override
  public int insert(Member member) throws Exception {
    return sqlSession.insert("MemberMapper.insert", member);
  }

  @Override
  public List<Member> findAll() throws Exception {
    return sqlSession.selectList("MemberMapper.findAll");
  }

  @Override
  public Member findByNo(int no) throws Exception {
    return sqlSession.selectOne("MemberMapper.findByNo", no);
  }

  @Override
  public int update(Member member) throws Exception {
    return sqlSession.update("MemberMapper.update", member);
  }

  @Override
  public int delete(int no) throws Exception {
    return sqlSession.delete("MemberMapper.delete", no);
  }

  @Override
  public Member findByName(String name) throws Exception {
    return sqlSession.selectOne("MemberMapper.findByName", name);
  }
}












