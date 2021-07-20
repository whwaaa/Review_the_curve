package com.xiaojumao.bean;

public class EmailSendConetent {
    private Integer uid;
    private String title;
    private String content;
    private String schedule;

    @Override
    public String toString() {
        return "EmailSendConetent{" +
                "uid=" + uid +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", schedule='" + schedule + '\'' +
                '}';
    }

    public EmailSendConetent() {
    }

    public EmailSendConetent(Integer uid, String title, String content, String schedule) {
        this.uid = uid;
        this.title = title;
        this.content = content;
        this.schedule = schedule;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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
}
