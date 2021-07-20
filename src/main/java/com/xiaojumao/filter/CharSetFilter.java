package com.xiaojumao.filter; /**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-01 0:30
 * @Modified By:
 */

import javax.servlet.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebFilter("*.do")
public class CharSetFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json;charset=utf-8;");
        chain.doFilter(request, response);
    }
}
