package com.xiaojumao.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaojumao.bean.EmailSendConetent;
import com.xiaojumao.bean.Task;
import com.xiaojumao.bean.Time;
import com.xiaojumao.bean.User;
import com.xiaojumao.server.TaskServer;
import com.xiaojumao.server.UserServer;

import java.util.*;

public class ManageTask {
    public static List<Time> timeLists = new ArrayList<>();
    public static Map<Long, EmailSendConetent> timeMap = new HashMap<>();

    public static Task createTask(int uid, Long startTime, String title, String content){
        // 7个学习/复习时间隔
        Long[] reviewCycle = {0l, 12*60*60*1000l, 24*60*60*1000l, 2*24*60*60*1000l,
                4*24*60*60*1000l, 7*24*60*60*1000l, 15*24*60*60*1000l};
        String[] timeStatus = {"开始学习ヾ(•ω•`)o", "12小时(强化记忆)o((>ω< ))o", "第1天(复习)(╯°□°）", "第2天(复习)<( ‵□′)>",
                "第4天(复习)╰（‵□′）╯", "第7天(强化记忆)┗|｀O′|┛", "第15天(学习完成)ヽ(゜▽゜　)－"};
        // 计算7个学习/复习时间点
        List<Time> timeList = new ArrayList<>();
        for(int i=0; i<7; i++) {
            Time time = new Time(startTime + reviewCycle[i], timeStatus[i]);
            timeList.add(time);
        }

        // 数据封装到Task
        // 时间转成JSON字符串存储
        String time = JSONUtil.toJSON(timeList);
        Task task = new Task(title, content, time, uid, timeList);

        // 封装数据存入任务列表
        timeLists.addAll(timeList);
        Collections.sort(timeLists, (o1, o2) -> {
            if(o1.getTime() >= o2.getTime())
                return 1;
            else
                return -1;
        });

        return task;
    }

    /**
     * 查询出需要发送的学习任务,并调用发送邮件
     * @param offTime:早上发送的邮件填入0点整时间戳,下午发送的邮件填入12点整时间戳
     * @param descTime:填入"早上"或"下午"
     */
    public static void getTodayTask_To_SendEmail(Long offTime, String descTime){
        // 最终查询出需要发送邮件的uid所对应的EmailSendConetent集合
        Map<Integer, List<EmailSendConetent>> sendEmailtaskEnd = new HashMap<>();

        TaskServer server = new TaskServer();
        // 先查询所有task
        List<Task> allTask = server.getAllTask();
        for (Task task : allTask) {
            // 解析time
            String gsonTime = task.getTime();
            Gson gson = new Gson();
            List<Time> timeList = gson.fromJson(gsonTime, new TypeToken<List<Time>>() {}.getType());
            // 遍历time存入map
            for (Time time : timeList) {
                EmailSendConetent e = new EmailSendConetent(task.getUid(), task.getTitle(), task.getContent(), time.getStatus());
                timeMap.put(time.getTime(), e);
            }
        }
        Set<Long> times = timeMap.keySet();
        Set<Long> sortSet = new TreeSet<>((o1, o2) -> o1.compareTo(o2));
        sortSet.addAll(times);

        // 查询出需要发送的所有邮件任务
        Iterator<Long> iterator = sortSet.iterator();
        while(iterator.hasNext()){
            Long time = iterator.next();
            // TODO: 暂时将时间改为查询整天
            if(time >= offTime+12*60*60*1000)
                // 从小到大排序,当时间大于今日中午,上午需要发送的邮件查询完毕,跳出
                break;
            if(time >= offTime){
                // 查询到需要发送的任务对应的时间,并将时间对应任务存入最终的Map中(sendEmailtaskEnd)
                EmailSendConetent e2 = timeMap.get(time);
                // 根据uid查询EmailSendContent集合是否存在,存在继续添加对象,不存在创建集合再添加对象
                List<EmailSendConetent> e2list = sendEmailtaskEnd.get(e2.getUid());
                if(e2list != null){
                    e2list.add(e2);
                }else{
                    e2list = new ArrayList<>();
                    e2list.add(e2);
                    sendEmailtaskEnd.put(e2.getUid(), e2list);
                }
            }
        }

        // 得到最终sendEmailtaskEnd的map
        Set<Integer> endKey = sendEmailtaskEnd.keySet();
        Iterator<Integer> endIt = endKey.iterator();
        while(endIt.hasNext()){
            Integer uid = endIt.next();
            System.out.println("uid: " + uid);
            // 通过uid查询email
            User userByUid = new UserServer().getUserByUid(uid);
            String email = userByUid.getEmail();
            String token = userByUid.getToken();
            List<EmailSendConetent> uidCons = sendEmailtaskEnd.get(uid);
            // 发送邮件
            EmailSend.sendStudyTask(email, uidCons, descTime, token);
        }
    }
}
