package com.xiaojumao.filter;

import com.xiaojumao.utils.SessionUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter({"/task/update.do","/create.html","/update.html"})
public class AccessFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String uidStr = req.getParameter("uid");
        Integer uid = SessionUtil.getUid(req, resp);
        if(uid==null && uidStr!=null){
            uid = Integer.parseInt(uidStr);
        }
        if(uid == null){
            // 未登录
            resp.sendError(404, "非正常访问,请先回登录界面");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
