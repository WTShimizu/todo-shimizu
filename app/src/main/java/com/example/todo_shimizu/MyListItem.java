package com.example.todo_shimizu;

public class MyListItem {
    protected int id;
    protected String title;
    protected String day;
    protected String exp;
    protected String status;

    public MyListItem(int id, String title, String day, String exp, String status) {
        this.id = id;
        this.title = title;
        this.day = day;
        this.exp = exp;
        this.status = status;
    }

    public int getId() {
        return id;
    }
}
