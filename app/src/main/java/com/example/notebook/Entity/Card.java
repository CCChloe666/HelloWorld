package com.example.notebook.Entity;


import android.os.Parcel;
import android.os.Parcelable;


public class Card implements Parcelable {

    private int id;

    private String title;

    private String front_content;

    private String back_content;

    public Card(){

    }

    protected Card(Parcel in) {
        id = in.readInt();
        title = in.readString();
        front_content = in.readString();
        back_content = in.readString();
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFront_content() {
        return front_content;
    }

    public void setFront_content(String front_content) {
        this.front_content = front_content;
    }

    public String getBack_content() {
        return back_content;
    }

    public void setBack_content(String back_content) {
        this.back_content = back_content;
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
        dest.writeString(front_content);
        dest.writeString(back_content);
    }
}
