package com.eomcs.pms.table;

import com.eomcs.pms.domain.Task;
import com.eomcs.util.JsonFileHandler;
import com.eomcs.util.Request;
import com.eomcs.util.Response;

import java.io.File;
import java.sql.Date;
import java.util.List;

// 1) 간단한 동작 테스트를 위해 임의의 값을 리턴한다.
// 2) JSON 포맷의 파일을 로딩한다.
public class TaskTable implements DataTable {

  File jsonFile = new File("tasks.json");
  List<Task> list;

  public TaskTable() {
    list = JsonFileHandler.loadObjects(jsonFile, Task.class);
  }

  @Override
  public void service(Request request, Response response) throws Exception {
    Task task = null;
    String[] fields = null;

    switch (request.getCommand()) {
      case "task/insert":

        // data에서 CSV 형식으로 표현된 문자열을 꺼내 각 필드 값으로 분리한다.
        fields = request.getData().get(0).split(",");

        // CSV 데이터를 Board 객체에 저장한다.
        task = new Task();

        // 새 게시글의 번호
        if (list.size() > 0) {
          task.setNo(list.get(list.size() - 1).getNo() + 1);
        } else {
          task.setNo(1);
        }

        task.setContent(fields[0]);
        task.setDeadline(Date.valueOf(fields[1]));
        task.setStatus(Integer.parseInt(fields[2]));
//        task.setOwner(fields[3]);

        // 새 게시글을 담은 Board 객체를 목록에 저장한다.
        list.add(task);

        // 게시글을 목록에 추가하는 즉시 List 컬렉션의 전체 데이터를 파일에 저장한다.
        // - 매번 전체 데이터를 파일에 저장하는 것은 비효율적이다.
        // - 효율성에 대한 부분은 무시한다.
        // - 현재 중요한 것은 서버 애플리케이션에서 데이터 파일을 관리한다는 점이다.
        // - 어차피 이 애플리케이션은 진정한 성능을 제공하는 DBMS으로 교체할 것이다.
        //
        JsonFileHandler.saveObjects(jsonFile, list);
        break;
      case "task/selectall":
        for (Task t : list) {
          response.appendData(String.format("%d,%s,%s,%s,%d",
                  t.getNo(),
                  t.getContent(),
                  t.getDeadline(),
                  t.getOwner(),
                  t.getStatus()
          ));
        }
        break;
      case "task/select":
        int no = Integer.parseInt(request.getData().get(0));

        task = getTask(no);
        if (task != null) {
          response.appendData(String.format("%d,%s,%s,%s,%d",
                  task.getNo(),
                  task.getContent(),
                  task.getDeadline(),
                  task.getOwner(),
                  task.getStatus()));
        } else {
          throw new Exception("해당 번호의 작업이 없습니다.");
        }
        break;
      case "task/selectByKeyword"://
        break;
      case "task/update":
        fields = request.getData().get(0).split(",");

        task = getTask(Integer.parseInt(fields[0]));
        if (task  == null) {
          throw new Exception("해당 번호의 작업이 없습니다.");
        }

        // 해당 게시물의 제목과 내용을 변경한다.
        // - List 에 보관된 객체를 꺼낸 것이기 때문에
        //   그냥 그 객체의 값을 변경하면 된다.
        task.setContent(fields[1]);
        task.setDeadline(Date.valueOf(fields[2]));
        task.setStatus(Integer.parseInt(fields[3]));
//        task.setOwner(fields[4]);

        JsonFileHandler.saveObjects(jsonFile, list);
        break;
      case "task/delete":
        no = Integer.parseInt(request.getData().get(0));
        task = getTask(no);
        if (task == null) {
          throw new Exception("해당 번호의 작업이 없습니다.");
        }

        list.remove(task);

        JsonFileHandler.saveObjects(jsonFile, list);
        break;
      default:
        throw new Exception("해당 명령을 처리할 수 없습니다.");
    }
  }

  private Task getTask(int taskNo) {
    for (Task t : list) {
      if (t.getNo() == taskNo) {
        return t;
      }
    }
    return null;
  }
}
