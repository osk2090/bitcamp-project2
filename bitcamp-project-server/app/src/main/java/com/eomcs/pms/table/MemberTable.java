package com.eomcs.pms.table;

import com.eomcs.pms.domain.Member;
import com.eomcs.util.JsonFileHandler;
import com.eomcs.util.Request;
import com.eomcs.util.Response;

import java.io.File;
import java.sql.Date;
import java.util.List;

// 1) 간단한 동작 테스트를 위해 임의의 값을 리턴한다.
// 2) JSON 포맷의 파일을 로딩한다.
public class MemberTable implements DataTable {

  File jsonFile = new File("members.json");

  List<Member> list;

  public MemberTable() {
    list = JsonFileHandler.loadObjects(jsonFile, Member.class);
  }

  @Override
  public void service(Request request, Response response) throws Exception {
    Member member = null;
    String[] fields = null;

    switch (request.getCommand()) {
      case "member/insert":
        fields = request.getData().get(0).split(",");
        member = new Member();

        //새 회원번호
        if (list.size() > 0) {
          member.setNo(list.get(list.size() - 1).getNo() + 1);
        } else {
          member.setNo(1);
        }

        member.setName(fields[0]);
        member.setEmail(fields[1]);
        member.setPassword(fields[2]);
        member.setPhoto(fields[3]);
        member.setTel(fields[4]);
        member.setRegisteredDate(new Date(System.currentTimeMillis()));

        list.add(member);

        JsonFileHandler.saveObjects(jsonFile, list);
        break;
      case "member/selectall":
        for (Member m : list) {
          response.appendData(String.format("%d,%s,%s,%s,%s",
                  m.getNo(),
                  m.getName(),
                  m.getEmail(),
                  m.getTel(),
                  m.getRegisteredDate()));
        }
        break;
      case "member/select":
        int no = Integer.parseInt(request.getData().get(0));

        member = getMember(no);
        if (member != null) {
          response.appendData(String.format("%d,%s,%s,%s,%s,%s",
                  member.getNo(),
                  member.getName(),
                  member.getEmail(),
                  member.getPhoto(),
                  member.getTel(),
                  member.getRegisteredDate()));
        } else {
          throw new Exception("해당 번호의 멤버가 없습니다.");
        }
        break;
      case "member/selectByName":
        String name = request.getData().get(0);
        member = getMemberByName(name);
        if (member != null) {
          response.appendData(String.format("%d,%s,%s,%s,%s,%s",
                  member.getNo(),
                  member.getName(),
                  member.getEmail(),
                  member.getPhoto(),
                  member.getTel(),
                  member.getRegisteredDate()));
        } else {
          throw new Exception("해당 번호의 회원이 없습니다.");
        }
        break;
      case "member/update":
        fields = request.getData().get(0).split(",");

        member = getMember(Integer.parseInt(fields[0]));
        if (member == null) {
          throw new Exception("해당 번호의 멤버가 없습니다.");
        }

        // 해당 게시물의 제목과 내용을 변경한다.
        // - List 에 보관된 객체를 꺼낸 것이기 때문에 
        //   그냥 그 객체의 값을 변경하면 된다.
        member.setName(fields[1]);
        member.setEmail(fields[2]);
        member.setPhoto(fields[3]);
        member.setTel(fields[4]);

        JsonFileHandler.saveObjects(jsonFile, list);
        break;
      case "member/delete":
        no = Integer.parseInt(request.getData().get(0));
        member = getMember(no);
        if (member == null) {
          throw new Exception("해당 번호의 멤버가 없습니다.");
        }

        list.remove(member);

        JsonFileHandler.saveObjects(jsonFile, list);
        break;
      default:
        throw new Exception("해당 명령을 처리할 수 없습니다.");
    }
  }

  private Member getMember(int memberNo) {
    for (Member m : list) {
      if (m.getNo() == memberNo) {
        return m;
      }
    }
    return null;
  }

  private Member getMemberByName(String name) {
    for (Member m : list) {
      if (m.getName().equals(name)) {
        return m;
      }
    }
    return null;
  }
}
