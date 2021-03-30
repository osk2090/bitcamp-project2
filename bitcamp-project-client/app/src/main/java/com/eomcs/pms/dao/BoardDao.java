package com.eomcs.pms.dao;

import com.eomcs.pms.domain.Board;
import com.eomcs.pms.domain.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/*
메서드를 호출 할 때 마다 Connection 객체 생성
즉 DBMS에 연결
클래스가 로딩될 때 미리 Connection
 */

public class BoardDao {

    public static Connection con;
//    Connection con;

    public BoardDao() throws Exception {
        this.con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
    }

    // 이제 메서드들은 인스턴스 필드에 들어있는 Connection 객체를 사용해야 하기 때문에
    // 스태틱 메서드가 아닌 인스턴스 메서드로 선언해야 한다.
    public int insert(Board board, Connection con) throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(
                "insert into pms_board(title, content, writer) values(?,?,?)");) {

            stmt.setString(1, board.getTitle());
            stmt.setString(2, board.getContent());
            stmt.setInt(3, board.getWriter().getNo());

            return stmt.executeUpdate();
        }
    }

    public List<Board> findAll() throws Exception {
        ArrayList<Board> list = new ArrayList<>();//객체로 넘겨주기 위해

        try (PreparedStatement stmt = con.prepareStatement(
                     "select"
                             + " b.no,"
                             + " b.title,"
                             + " b.cdt,"
                             + " b.vw_cnt,"
                             + " b.like_cnt,"
                             + " m.no as writer_no,"
                             + " m.name as writer_name"
                             + " from pms_board b"
                             + "   inner join pms_member m on m.no=b.writer"
                             + " order by b.no desc");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Board board = new Board();
                board.setNo(rs.getInt("no"));
                board.setTitle(rs.getString("title"));
                board.setRegisteredDate(rs.getDate("cdt"));
                board.setViewCount(rs.getInt("vw_cnt"));

                Member writer = new Member();
                writer.setNo(rs.getInt("writer_no"));
                writer.setName(rs.getString("writer_name"));
                board.setWriter(writer);

                list.add(board);
            }
        }

        return list;
    }

    public Board findByNo(int no) throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(
                     "select"
                             + " b.no,"
                             + " b.title,"
                             + " b.content,"
                             + " b.cdt,"
                             + " b.vw_cnt,"
                             + " b.like_cnt,"
                             + " m.no as writer_no,"//m.no는 writer_no로 선언
                             + " m.name as writer_name"//m.name는 writer_name로 선언
                             + " from pms_board b"
                             + "   inner join pms_member m on m.no=b.writer"//두 컬럼의 값이 일치하는 경우에만 두 데이터를 연결하여 결과로 리턴한다
                             + " where b.no = ?")) {

            stmt.setInt(1, no);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;//선택한 자리에 데이터가 없으면 null을 리턴
                }

                Board board = new Board();
                board.setNo(rs.getInt("no"));
                board.setTitle(rs.getString("title"));
                board.setContent(rs.getString("content"));
                board.setRegisteredDate(new Date(rs.getTimestamp("cdt").getTime()));
                board.setViewCount(rs.getInt("vw_cnt"));
                board.setLike(rs.getInt("like_cnt"));

                Member writer = new Member();//멤버 객체를 접근
                writer.setNo(rs.getInt("writer_no"));//거기서 writer_no로 즉 m.no로 접근하여 해당 데이터를 삽입
                writer.setName(rs.getString("writer_name"));//동일
                writer.setEmail(rs.getString("writer_email"));
                board.setWriter(writer);//위에 만든 객체를 통째로 넘긴다

                return board;
            }
        }
    }

    public int update(Board board) throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(
                     "update pms_board set title=?, content=? where no=?")) {

            stmt.setString(1, board.getTitle());
            stmt.setString(2, board.getContent());
            stmt.setInt(3, board.getNo());
            return stmt.executeUpdate();
        }
    }

    public int updateViewCount(int no) throws Exception {
        try (
             PreparedStatement stmt = con.prepareStatement(
                     "update pms_board set vw_cnt=vw_cnt+1 where no=?")) {

            stmt.setInt(1, no);
            return stmt.executeUpdate();
        }
    }

    public int delete(int no)throws Exception {
        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
             PreparedStatement stmt = con.prepareStatement(
                     "delete from pms_board where no=?")) {
            stmt.setInt(1, no);
            return stmt.executeUpdate();
        }
    }


    public List<Board> findByKeyword(String keyword) throws Exception {
        ArrayList<Board> list = new ArrayList<>();//객체로 넘겨주기 위해

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
             PreparedStatement stmt = con.prepareStatement(
                     "select"
                             + " b.no,"
                             + " b.title,"
                             + " b.cdt,"
                             + " b.vw_cnt,"
                             + " b.like_cnt,"
                             + " m.no as writer_no,"
                             + " m.name as writer_name"
                             + " from pms_board b"
                             + "   inner join pms_member m on m.no=b.writer"
                             + " where title like concat('%',?,'%')"
                             + " or content like concat('%',?,'%')"
                             + " or writer like concat('%',?,'%')"
                             + " order by b.no desc")) {

            stmt.setString(1, keyword);
            stmt.setString(2, keyword);
            stmt.setString(3, keyword);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Board board = new Board();
                board.setNo(rs.getInt("no"));
                board.setTitle(rs.getString("title"));
                board.setRegisteredDate(rs.getDate("cdt"));
                board.setViewCount(rs.getInt("vw_cnt"));

                Member writer = new Member();
                writer.setNo(rs.getInt("writer_no"));
                writer.setName(rs.getString("writer_name"));
                board.setWriter(writer);

                list.add(board);
            }
        }
        return list;
    }

    public void insert(Board b) {
    }
}