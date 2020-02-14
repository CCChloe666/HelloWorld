package com.example.notebook.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {

    private int id;

    private String title;

    private String content;

    private String createTime;

    private int groupId;//分类ID

    private String groupName;//分类名称

    private int isWasted=0;//1添加到垃圾箱,0不添加

    private int isAdded;//是否添加到复习计划,1添加,0不添加

    private int isStared;//是否添加到我的收藏,1添加,0不添加

    public Note(){

    }

    public Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        createTime = in.readString();
        groupId = in.readInt();
        groupName = in.readString();
        isWasted = in.readInt();
        isAdded = in.readInt();
        isStared = in.readInt();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getIsStared() {
        return isStared;
    }

    public void setIsStared(int isStared) {
        this.isStared = isStared;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(createTime);
        dest.writeInt(groupId);
        dest.writeString(groupName);
        dest.writeInt(isWasted);
        dest.writeInt(isAdded);
        dest.writeInt(isStared);
    }
}
