package com.xiaojumao.dao;

import com.xiaojumao.bean.Task;

import java.util.List;

public interface TaskDao {
    /**
     * 添加学习任务
     * @param task:学习任务
     * @return 1:添加成功 0:添加失败
     */
    public Integer addTask(Task task);

    /**
     * 修改学习任务
     * @param id:要修改的学习任务
     * @param newTask:修改为新的任务
     * @return 1:修改成功 0:修改失败
     */
    public Integer updaeTask(int id, Task newTask);

    /**
     * 删除学习任务
     * @param id:要删除的任务id
     * @return 1:修改成功 0:修改失败
     */
    public Integer deleteTask(int id);

    /**
     * 查询当前用户下所有学习任务
     * @param uid
     * @return
     */
    public List<Task> getAllTaskByUid(int uid);

    /**
     * 根据tid查询Task
     * @param tid:要查询的Task
     * @return
     */
    public Task getTaskByTid(int tid);

    /**
     * 查询所有用户所有学习任务
     * @return
     */
    public List<Task> getAllTask();
}
