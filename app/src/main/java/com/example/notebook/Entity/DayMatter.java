package com.example.notebook.Entity;


public class DayMatter{

    private int id;

    private String title;

    private String aimTime;

    private String createTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAimTime() {
        return aimTime;
    }

    public void setAimTime(String aimTime) {
        this.aimTime = aimTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
