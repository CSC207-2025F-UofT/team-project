package com.studyarc.entity;

import java.util.Date;

public class Task {
    private String name;
    private String duedate;
    private String status;

    public Task(String name, String duedate) {
        this.name = name;
        this.duedate = duedate;
        this.status = "Not Started" ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
