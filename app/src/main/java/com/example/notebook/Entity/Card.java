package com.example.notebook.Entity;


public class Card {

    private int id;

    private String title;

    private String front_content;

    private String back_front;

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

    public String getBack_front() {
        return back_front;
    }

    public void setBack_front(String back_front) {
        this.back_front = back_front;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
