package com.xiaojumao.server;

import com.xiaojumao.bean.Task;
import com.xiaojumao.dao.imp.TaskDaoImp;
import com.xiaojumao.utils.Base64Util;

import java.util.List;

public class TaskServer {
    private static TaskDaoImp dao = new TaskDaoImp();

    public Integer addTask(Task task) {
        // 将content内容转成base64
        String base64Str = Base64Util.stringToBase64(task.getContent());
        task.setContent(base64Str);
        return dao.addTask(task);
    }

    public Integer updaeTask(int id, Task newTask) {
        // 将content内容转成base64
        String base64Str = Base64Util.stringToBase64(newTask.getContent());
        newTask.setContent(base64Str);
        return dao.updaeTask(id, newTask);
    }

    public Integer deleteTask(int id) {
        return dao.deleteTask(id);
    }

    public List<Task> getAllTaskByUid(int uid) {
        List<Task> allTaskByUid = dao.getAllTaskByUid(uid);
        if(allTaskByUid != null){
            for (Task task : allTaskByUid) {
                // 将content中的base64解码
                String str = Base64Util.base64ToString(task.getContent());
                task.setContent(str);
            }
        }
        return allTaskByUid;
    }

    public Task getTaskByTid(int tid) {
        // 将content中的base64解码
        Task task = dao.getTaskByTid(tid);
        String str = Base64Util.base64ToString(task.getContent());
        task.setContent(str);
        return task;
    }

    /**
     * 查询所有用户所有学习任务
     * @return
     */
    public List<Task> getAllTask(){
        List<Task> allTask = dao.getAllTask();
        if(allTask != null){
            for (Task task : allTask) {
                // 将content中的base64解码
                String str = Base64Util.base64ToString(task.getContent());
                task.setContent(str);
            }
        }
        return allTask;
    }
}
