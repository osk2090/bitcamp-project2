package com.eomcs.pms.filter;

import com.eomcs.stereotype.Component;
import com.eomcs.util.CommandRequest;
import com.eomcs.util.CommandResponse;
import com.eomcs.util.Filter;
import com.eomcs.util.FilterChain;

import java.util.Locale;

@Component
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(CommandRequest request, CommandResponse response, FilterChain nextChain) throws Exception {
        String commandPath = request.getCommandPath().toLowerCase(Locale.ROOT);
        if (commandPath.endsWith("add") ||//예) /board/add
                commandPath.endsWith("update") ||//예) /board/update
                commandPath.endsWith("delete")) {//예) /board/delete

            if (request.getSession().getAttribute("loginUser") == null) {
                response.getWriter().println("로그인 해야 합니다!");
                return;
            }
        }
        //로그인이 필요한 서비스에 대해서 정상적으로 로그인 되어 있다면 다음 필터를 실행한다
        nextChain.doFilter(request, response);

    }
}