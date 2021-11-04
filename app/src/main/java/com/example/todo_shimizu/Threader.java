package com.example.todo_shimizu;

import android.util.Log;

import java.util.List;

public class Threader {

    // TODO : FRAG
    static class AddThreadHttp extends Thread {
        AddDisplay mAdd;
        public AddThreadHttp(AddDisplay addDisplay) {
            mAdd = addDisplay;
        }

        public void run() {
            List<DataList> dataLists = new HttpRequest().getRequest(true);
            if (dataLists != null) {
                mAdd.createList(dataLists);
            }
        }
    }

    static class NewThreadHttp extends Thread {
        DataList dataLists;
        public NewThreadHttp(DataList dataList) {
            dataLists = dataList;
        }

        public void run() {
            Log.d("tag", new HttpRequest().postRequest(true, dataLists));
        }
    }

    static class DeleteThreadHttp extends Thread {
        int mId;
        public DeleteThreadHttp(int id) {
            mId = id;
        }

        public void run() {
            Log.d("tag", new HttpRequest().deleteRequest(true, mId));
        }
    }

    static class EditViewThreadHttp extends Thread {
        int mId;
        EditDisplay mEdit;
        public EditViewThreadHttp(int id, EditDisplay editDisplay) {
            mId = id;
            mEdit = editDisplay;
        }

        public void run() {
            DataList dataLists = new HttpRequest().getIdRequest(true, mId);
            if (dataLists != null) {
                mEdit.createEdit(dataLists);
            }
        }
    }

    static class EditThreadHttp extends Thread {
        DataList dataLists;
        public EditThreadHttp(DataList dataList) {
            dataLists = dataList;
        }

        public void run() {
            Log.d("tag", new HttpRequest().editRequest(true, dataLists));
        }
    }
}
