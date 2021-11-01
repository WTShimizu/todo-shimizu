package com.example.todo_shimizu;

public class DataList {
    int mId;
    String mTitle;
    String mExp;
    String mStatus;
    String mDay;

    void setId(int id) {
        mId = id;
    }
    int getId() {
        return mId;
    }

    void setTitle(String title) {
        mTitle = title;
    }
    String getTitle() {
        return mTitle;
    }

    void setExp(String exp) {
        mExp = exp;
    }
    String getExp() {
        return mExp;
    }

    void setStatus(String status) {
        mStatus = status;
    }
    String getStatus() {
        return mStatus;
    }

    void setDay(String day) {
        mDay = day;
    }
    String getDay() {
        return mDay;
    }
}
