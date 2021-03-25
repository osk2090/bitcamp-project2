package com.eomcs.pms.handler;

import com.eomcs.util.Prompt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberValidator {

//  public String inputMember(String promptTitle) {
//
//    while (true) {
//      String name = Prompt.inputString(promptTitle);
//      if (name.length() == 0) {
//        return null;
//      }
//
//      try {
//        return this.stmt.executeQuery("member/selectByName", name).next().split(",")[1];
//      } catch (Exception e) {
//        System.out.println("등록된 회원이 아닙니다.");
//      }
//    }
//  }
//
//  public String inputMembers(String promptTitle) {
//    String members = "";
//    while (true) {
//      String name = inputMember(promptTitle);
//      if (name == null) {
//        return members;
//      } else {
//        if (!members.isEmpty()) {
//          members += "/";
//        }
//        members += name;
//      }
//    }
//  }


  public String inputMember(String promptTitle) throws Exception {
    while (true) {
      String name = Prompt.inputString(promptTitle);
      if (name.length() == 0) {
        return null;
      }

      try (Connection con = DriverManager.getConnection(
              "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
           PreparedStatement stmt = con.prepareStatement(
                   "select name "
                           + " from pms_member"
                           + " where name like concat('%',?,'%')"
                           + " order by no desc ")) {

        stmt.setString(1, name);

        try (ResultSet rs = stmt.executeQuery()) {
          if (!rs.next()) {
            System.out.println("등록된 회원이 아닙니다.");
            return null;
          }
          return name;
        }
      }
    }
  }

  public String inputMembers(String promptTitle) throws Exception {
    String members = "";
    while (true) {
      String name = inputMember(promptTitle);
      if (name == null) {
        return members;
      } else {
        if (!members.isEmpty()) {
          members += "/";
        }
        members += name;
      }
    }
  }
}






