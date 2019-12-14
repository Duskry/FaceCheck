package com.ren.face.bean;

import java.util.Date;

public class StudentCheck {
    Integer checkId;
    String account;
    String name;
    Date time;

    public StudentCheck() {
    }

    public Integer getCheckId() {
        return checkId;
    }

    public void setCheckId(Integer checkId) {
        this.checkId = checkId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "StudentCheck{" +
                "checkId=" + checkId +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", time=" + time +
                '}';
    }
}
