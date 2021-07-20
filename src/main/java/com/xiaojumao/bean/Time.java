package com.xiaojumao.bean;

public class Time {
    private Long time;
    private Integer tid;
    private String status;

    @Override
    public String toString() {
        return "Time{" +
                "time=" + time +
                ", tid=" + tid +
                ", status='" + status + '\'' +
                '}';
    }

    public Time() {
    }

    public Time(Long time, String status) {
        this.time = time;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }
}
