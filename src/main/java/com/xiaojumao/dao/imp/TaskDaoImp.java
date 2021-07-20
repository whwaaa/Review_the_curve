package com.xiaojumao.dao.imp;

import com.xiaojumao.bean.Task;
import com.xiaojumao.dao.TaskDao;
import com.xiaojumao.utils.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDaoImp extends DBUtils implements TaskDao {

    // 添加学习任务
    private static final String ADD_TASK_SQL = "INSERT INTO TASK(TITLE,CONTENT,TIME,UID) VALUES(?,?,?,?)";
    // 修改学习任务
    private static final String UPDATE_TASK_SQL = "UPDATE TASK SET TITLE=?,CONTENT=?,TIME=?,UID=? WHERE TID=?";
    // 删除学习任务
    private static final String DELETE_TASK_SQL = "DELETE FROM TASK WHERE TID=?";
    // 查询当前用户下所有学习任务
    private static final String GET_ALL_TASK_BY_UID = "SELECT * FROM TASK WHERE UID=?";
    // 根据tid查询Task
    private static final String GETL_TASK_BY_TID = "SELECT * FROM TASK WHERE TID=?";
    // 查询所有用户所有学习任务
    private static final String GET_ALL_TASK_SQL = "SELECT * FROM TASK";

    /**
     * 添加学习任务
     * @param task:学习任务
     * @return 1:添加成功 0:添加失败
     */
    @Override
    public Integer addTask(Task task) {
        List param = new ArrayList();
        param.add(task.getTitle());
        param.add(task.getContent());
        param.add(task.getTime());
        param.add(task.getUid());
        try {
            return update(ADD_TASK_SQL, param);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeAll();
        }
        return null;
    }

    /**
     * 修改学习任务
     * @param id:要修改的学习任务
     * @param newTask:修改为新的任务
     * @return 1:修改成功 0:修改失败
     */
    @Override
    public Integer updaeTask(int id, Task newTask) {
        List param = new ArrayList();
        param.add(newTask.getTitle());
        param.add(newTask.getContent());
        param.add(newTask.getTime());
        param.add(newTask.getUid());
        param.add(id);
        try {
            return update(UPDATE_TASK_SQL, param);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeAll();
        }
        return null;
    }

    /**
     * 删除学习任务
     * @param id:要删除的任务id
     * @return 1:修改成功 0:修改失败
     */
    @Override
    public Integer deleteTask(int id) {
        List param = new ArrayList();
        param.add(id);
        try {
            return update(DELETE_TASK_SQL, param);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeAll();
        }
        return null;
    }

    /**
     * 查询当前用户下所有学习任务
     * @param uid
     * @return
     */
    @Override
    public List<Task> getAllTaskByUid(int uid) {
        List<Task> taskList = new ArrayList<>();
        List param = new ArrayList();
        param.add(uid);
        try {
            ResultSet resultSet = query(GET_ALL_TASK_BY_UID, param);
            if(resultSet != null){
                while(resultSet.next()){
                    int tid = resultSet.getInt("tid");
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    String time = resultSet.getString("time");
                    Task task = new Task(tid, title, content, time, uid, null);
                    taskList.add(task);
                }
                return taskList;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeAll();
        }

        return null;
    }


    /**
     * 根据tid查询Task
     * @param tid:要查询的Task
     * @return
     */
    @Override
    public Task getTaskByTid(int tid) {
        List param = new ArrayList();
        param.add(tid);
        try {
            ResultSet resultSet = query(GETL_TASK_BY_TID, param);
            if(resultSet != null){
                while(resultSet.next()){
                    int uid = resultSet.getInt("uid");
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    String time = resultSet.getString("time");
                    return new Task(tid, title, content, time, uid, null);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeAll();
        }

        return null;
    }


    /**
     * 查询所有用户所有学习任务
     * @return
     */
    @Override
    public List<Task> getAllTask() {
        List<Task> taskList = new ArrayList<>();
        try {
            ResultSet resultSet = query(GET_ALL_TASK_SQL, null);
            if(resultSet != null){
                while(resultSet.next()){
                    int tid = resultSet.getInt("tid");
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    String time = resultSet.getString("time");
                    int uid = resultSet.getInt("uid");
                    Task task = new Task(tid, title, content, time, uid, null);
                    taskList.add(task);
                }
                return taskList;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeAll();
        }

        return null;
    }
}
