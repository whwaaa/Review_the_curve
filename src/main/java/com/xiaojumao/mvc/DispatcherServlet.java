package com.xiaojumao.mvc;

import com.xiaojumao.utils.ManageTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-06-30 10:41
 * @Modified By:
 */
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        String path = config.getInitParameter("contentConfigLocation");
        InputStream is = DispatcherServlet.class.getClassLoader().getResourceAsStream(path);
        HandlerMapping.load(is);

        // 发送今日学习任务邮件
        Runnable runnable = new Runnable() {
            public void run() {
                while (true) {
                    System.out.println("发送今日学习任务邮件_已就绪");
                    // 获取今日0点时间
                    Calendar c0 = Calendar.getInstance();
                    c0.set(Calendar.HOUR_OF_DAY,0);
                    c0.set(Calendar.MINUTE,0);
                    c0.set(Calendar.SECOND,0);
                    c0.set(Calendar.MILLISECOND, 0);
                    Long time_00_00 = c0.getTime().getTime();
                    // 获取今日12点时间
                    Calendar c1 = Calendar.getInstance();
                    c1.set(Calendar.HOUR_OF_DAY,12);
                    c1.set(Calendar.MINUTE,0);
                    c1.set(Calendar.SECOND,0);
                    c1.set(Calendar.MILLISECOND, 0);
                    Long time_12_00 = c1.getTime().getTime();
                    // 获取今日8点时间
                    Calendar c2 = Calendar.getInstance();
                    c2.set(Calendar.HOUR_OF_DAY,8);
                    c2.set(Calendar.MINUTE,0);
                    c2.set(Calendar.SECOND,0);
                    c2.set(Calendar.MILLISECOND, 0);
                    Long time_8_00 = c2.getTime().getTime();
                    // 获取今日14点时间
                    Calendar c3 = Calendar.getInstance();
                    c3.set(Calendar.HOUR_OF_DAY,12);
                    c3.set(Calendar.MINUTE,0);
                    c3.set(Calendar.SECOND,0);
                    c3.set(Calendar.MILLISECOND,0);
                    Long time_14_00 = c3.getTime().getTime();

                    try {
                        long nowTime = Calendar.getInstance().getTime().getTime();
                        // 在上午8点之前启动,定时在8点发送邮件,否则跳过
                        if(nowTime < time_8_00) {
                            Thread.sleep(time_8_00 - nowTime);
                            // 上午发送的提醒邮件
                            ManageTask.getTodayTask_To_SendEmail(time_00_00,"上午");
                        }else{
                            System.out.println("跳过上午的邮件发送");
                        }
                    } catch (InterruptedException e) {
                        System.out.println("time_8_00 发送失败");
                        return;
                    }


                    try {
                        long nowTime = Calendar.getInstance().getTime().getTime();
                        // 在 下午14点之前启动,定时在14点发送邮件,否则跳过
                        if(nowTime < time_14_00) {
                            Thread.sleep(time_14_00 - nowTime);
                            // 下午发送的提醒邮件
                            ManageTask.getTodayTask_To_SendEmail(time_12_00,"下午");
                        }else{
                            System.out.println("跳过下午的邮件发送");
                        }
                    } catch (InterruptedException e) {
                        System.out.println("time_14_00 发送失败");
                        return;
                    }

                    return;
                }
            }
        };
        Thread t = new Thread(runnable);
        t.start();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        // 1.获取用户请求的uri  /xxx.do
        String uri = req.getRequestURI();
        HandlerMapping.MVCMapping mvcMapping = HandlerMapping.get(uri);
        if (mvcMapping == null) {
            resp.sendError(404, "自定义MVC;映射地址不存在" + uri);
            return;
        }

        Object obj = mvcMapping.getObj();
        Method method = mvcMapping.getMethod();
        Object result = null;
        try {
            result = method.invoke(obj, req, resp);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        switch(mvcMapping.getType()){
            case TEXT:
                resp.getWriter().write((String)result);
                break;
            case VIEW:
                resp.sendRedirect((String)result);
                break;
        }
    }
}
