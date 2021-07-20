package com.xiaojumao.bean;

import java.util.List;

public class Task {
    private Integer tid;
    private String title;
    private String content;
    private String time;
    private Integer uid;
    private List<Time> timeList;

    @Override
    public String toString() {
        return "Task{" +
                "tid=" + tid +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", uid=" + uid +
                ", timeList=" + timeList +
                '}';
    }

    public Task() {
    }

    public Task(String title, String content, String time, Integer uid, List<Time> timeList) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.uid = uid;
        this.timeList = timeList;
    }

    public Task(Integer tid, String title, String content, String time, Integer uid, List<Time> timeList) {
        this.tid = tid;
        this.title = title;
        this.content = content;
        this.time = time;
        this.uid = uid;
        this.timeList = timeList;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public List<Time> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<Time> timeList) {
        this.timeList = timeList;
    }
}
