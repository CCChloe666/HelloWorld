package com.example.notebook.Entity;


import android.os.Parcel;
import android.os.Parcelable;

public class DayMatter implements Parcelable {

    private int id;

    private String title;

    private String aimTime;

    private String createTime;

    public DayMatter(){

    }

    protected DayMatter(Parcel in) {
        id = in.readInt();
        title = in.readString();
        aimTime = in.readString();
        createTime = in.readString();
    }

    public static final Creator<DayMatter> CREATOR = new Creator<DayMatter>() {
        @Override
        public DayMatter createFromParcel(Parcel in) {
            return new DayMatter(in);
        }

        @Override
        public DayMatter[] newArray(int size) {
            return new DayMatter[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(aimTime);
        dest.writeString(createTime);
    }
}
