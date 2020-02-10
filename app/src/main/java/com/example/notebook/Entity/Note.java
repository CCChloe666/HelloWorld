package com.example.notebook.Entity;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Note{

    private int id;

    private String title;

    private String content;

    private String createTime;

    private int groupId;//分类ID

    private String groupName;//分类名称

    private int type;//笔记类型，1纯文本，2Html，3Markdown

    private String bgColor;//背景颜色，存储颜色代码

    private int isWasted;

    private int isAdded;//是否添加到复习计划！！！！！

    public int getIsAdded() {
        return isAdded;
    }

    public void setIsAdded(int isAdded) {
        this.isAdded = isAdded;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public int getIsWasted() {
        return isWasted;
    }

    public void setIsWasted(int isWasted) {
        this.isWasted = isWasted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
