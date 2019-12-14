package com.ren.face.bean;


import java.io.Serializable;

public class Student implements Serializable {
    private Integer id;
    private String account;
    private String name;
    private Integer role;

    public Student(String account, String name, Integer role) {
        this.account = account;
        this.name = name;
        this.role = role;
    }

    public Student() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
