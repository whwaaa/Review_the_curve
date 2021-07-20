package com.xiaojumao.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaojumao.bean.Message;
import com.xiaojumao.bean.Task;
import com.xiaojumao.bean.Time;
import com.xiaojumao.bean.User;
import com.xiaojumao.mvc.ResponseBody;
import com.xiaojumao.server.TaskServer;
import com.xiaojumao.server.UserServer;
import com.xiaojumao.utils.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class TaskController {

    private static TaskServer server = new TaskServer();

    @ResponseBody("/task/getAllTask.do")
    public String getAllTask(HttpServletRequest req, HttpServletResponse resp){
        String jsoncallback = req.getParameter("jsoncallback");
        // 1. 获取参数
        String uidStr = req.getParameter("uid");
        Integer sessionUid = SessionUtil.getUid(req, resp);
        Integer uid = null;
        if (uidStr == null) {
            if(sessionUid == null){
                return JSONUtil.toJSON(new Message(-1, "要先登录<( ￣^￣)", null),jsoncallback);
            }else{
                // 服务器已经登录
                uid = sessionUid;
            }
        }else{
            uid = Integer.parseInt(uidStr);
            // 存入session
            SessionUtil.setUid(req, uid);
        }
        
        // 2. 调用servle层方法
        List<Task> alltask = server.getAllTaskByUid(uid);
        for (Task task : alltask) {
            String time = task.getTime();
            Gson gson = new Gson();
            List<Time> list = gson.fromJson(time, new TypeToken<List<Time>>() {}.getType());
            task.setTimeList(list);
            task.setTime(null);
        }

        // 3. 根据数据返回响应结果
        return JSONUtil.toJSON(new Message(0, "", alltask), jsoncallback);
    }

    @ResponseBody("/task/addTask.do")
    public String addTask(HttpServletRequest req, HttpServletResponse resp){
        // 获取数据
        String jsoncallback = req.getParameter("jsoncallback");
        String time = req.getParameter("time");
        String s = new Date(Long.parseLong(time)).toLocaleString();
        System.out.println(s);
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String mailRe = req.getParameter("mailRe");
        String uidStr = req.getParameter("uid");
        // TODO: 还未开发连续学习模式,series暂时固定1
        Integer series = 1;//Integer.parseInt(req.getParameter("series"));

        Integer uid = SessionUtil.getUid(req, resp);
        if(uid==null && uidStr!=null){
            uid = Integer.parseInt(uidStr);
        }
        // 封装调取service
        Integer res = null;
        for(int i=0; i<series; i++){
            if (uid != null) {
                Task task = ManageTask.createTask(uid, Long.parseLong(time)+i*24*60*60*1000, title, content);
                res = server.addTask(task);
            }else{
                return JSONUtil.toJSON(new Message(-2, "要先登录<( ￣^￣)", null),jsoncallback);
            }
        }
        // 返回结果
        if(res > 0){
            // 发送邮件
            if(mailRe.equals("true")){
                User userByUid = new UserServer().getUserByUid(uid);
                String email = userByUid.getEmail();
                String token = userByUid.getToken();
                EmailSend.createTaskSendEmail(email, title, token);
            }

            return JSONUtil.toJSON(new Message(0, "添加成功", null),jsoncallback);
        }else{
            return JSONUtil.toJSON(new Message(-1, "添加失败", null),jsoncallback);
        }

    }

    @ResponseBody("/task/delete.do")
    public String delete(HttpServletRequest req, HttpServletResponse resp){
        String jsoncallback = req.getParameter("jsoncallback");
        int tid = Integer.parseInt(req.getParameter("tid"));
        Integer integer = server.deleteTask(tid);
        if(integer > 0){
            return JSONUtil.toJSON(new Message(0, "删除成功", null),jsoncallback);
        }
        return JSONUtil.toJSON(new Message(-1, "删除失败", null),jsoncallback);
    }

    @ResponseBody("/task/getTaskByTid.do")
    public String getTaskByTidController(HttpServletRequest req, HttpServletResponse resp){
        String jsoncallback = req.getParameter("jsoncallback");
        // 获取tid
        String tidStr = req.getParameter("tid");
        int tid = Integer.parseInt(tidStr);
        // 通过tid查询task
        Task task = server.getTaskByTid(tid);
        if (task != null) {
            String time = task.getTime();
            Gson gson = new Gson();
            List<Time> list = gson.fromJson(time, new TypeToken<List<Time>>() {}.getType());
            task.setTimeList(list);
            task.setTime(null);
            return JSONUtil.toJSON(new Message(0, "", task),jsoncallback);
        }

        return JSONUtil.toJSON(new Message(-1, "未查询到信息", null),jsoncallback);
    }


    @ResponseBody("/task/update.do")
    public String getTaskByTid(HttpServletRequest req, HttpServletResponse resp){
        String jsoncallback = req.getParameter("jsoncallback");
        // 获取数据
        String time = req.getParameter("time");
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String isActive = req.getParameter("isActive");
        String tidStr = req.getParameter("tid");
        String uidStr = req.getParameter("uid");
        Integer tid;
        // 修改数据库
        if (tidStr != null) {
            tid = Integer.parseInt(tidStr);
            Integer uid = SessionUtil.getUid(req, resp);
            if(uid==null && uidStr!=null){
                uid = Integer.parseInt(uidStr);
            }
            Task task = ManageTask.createTask(uid, Long.parseLong(time), title, content);
            Integer integer = server.updaeTask(tid, task);
            if(integer > 0){
                return JSONUtil.toJSON(new Message(0, "修改成功", null),jsoncallback);
            }
        }
        return JSONUtil.toJSON(new Message(-1, "修改失败", null),jsoncallback);
    }
}




