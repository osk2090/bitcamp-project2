package com.eomcs.pms.dao;

import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {

    Connection con;

    public TaskDao() throws Exception {
        this.con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
    }

    public int insert(Task task) throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(
                "insert into pms_task(content,deadline,owner,status,project_no) values(?,?,?,?,?)")) {

            stmt.setString(1, task.getContent());
            stmt.setDate(2, task.getDeadline());
            stmt.setInt(3, task.getOwner().getNo());
            stmt.setInt(4, task.getStatus());
            stmt.setInt(5, task.getProjectNo());

            return stmt.executeUpdate();

        }
    }

    public List<Task> findAll()throws Exception {
        ArrayList<Task> list = new ArrayList<>();

        try (PreparedStatement stmt2 = con.prepareStatement(
                "select "
                        + "   t.no,"
                        + "   t.content,"
                        + "   t.deadline,"
                        + "   t.status,"
                        + "   m.no as owner_no,"
                        + "   m.name as owner_name,"
                        + "   p.no as project_no,"
                        + "   p.title as project_title"
                        + " from pms_task t "
                        + "   inner join pms_member m on t.owner=m.no"
                        + "   inner join pms_project p on t.project_no=p.no"
                        + " order by p.no desc, t.content asc");
             ResultSet rs = stmt2.executeQuery()) {
            while (rs.next()) {
                Task task = new Task();
                task.setNo(rs.getInt("no"));
                task.setContent(rs.getString("content"));
                task.setDeadline(rs.getDate("deadline"));

                Member owner = new Member();
                owner.setNo(rs.getInt("owner_no"));
                owner.setName(rs.getString("owner_name"));
                task.setOwner(owner);

                task.setStatus(rs.getInt("status"));

                task.setProjectNo(rs.getInt("project_no"));
                task.setProjectTitle(rs.getString("project_title"));

                list.add(task);
            }
            return list;
        }
    }



    public List<Task> findByProjectNo(int projectNo)throws Exception {
        ArrayList<Task> list = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(
                "select "
                        + "   t.no,"
                        + "   t.content,"
                        + "   t.deadline,"
                        + "   t.status,"
                        + "   m.no as owner_no,"
                        + "   m.name as owner_name,"
                        + "   p.no as project_no,"
                        + "   p.title as project_title"
                        + " from pms_task t "
                        + "   inner join pms_member m on t.owner=m.no"
                        + "   inner join pms_project p on t.project_no=p.no"
                        + " where t.project_no=?"
                        + " order by p.no desc, t.content asc")) {

            stmt.setInt(1, projectNo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Task task = new Task();
                task.setNo(rs.getInt("no"));
                task.setContent(rs.getString("content"));
                task.setDeadline(rs.getDate("deadline"));

                Member owner = new Member();
                owner.setNo(rs.getInt("onwer_no"));
                owner.setName(rs.getString("owner_name"));
                task.setOwner(owner);

                task.setStatus(rs.getInt("status"));

                task.setProjectNo(rs.getInt("project_mo"));
                task.setProjectTitle(rs.getString("project_title"));

                list.add(task);
            }
            return list;
        }
    }
}