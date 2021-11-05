package com.example.todo_shimizu;

import android.util.Log;

import java.util.List;

public class Threader {

    // TODO : FRAG
    static class AddThreadHttp extends Thread {
        AddDisplay mAdd;
        boolean mFrag;
        public AddThreadHttp(AddDisplay addDisplay, boolean b) {
            mAdd = addDisplay;
            mFrag = b;
        }


        public void run() {
            List<DataList> dataLists = new HttpRequest().getRequest(mFrag);
            if (dataLists != null) {
                mAdd.createList(dataLists);
            }
        }
    }

    static class DeleteListThreadHttp extends Thread {
        DeleteDisplay mDele;
        boolean mFrag;
        public DeleteListThreadHttp(DeleteDisplay deleteDisplay, boolean b) {
            mDele = deleteDisplay;
            mFrag = b;
        }


        public void run() {
            List<DataList> dataLists = new HttpRequest().getRequest(mFrag);
            if (dataLists != null) {
                mDele.createDelList(dataLists);
            }
        }
    }

    static class NewThreadHttp extends Thread {
        DataList dataLists;
        boolean mFrag;
        public NewThreadHttp(DataList dataList, boolean b) {
            dataLists = dataList;
            mFrag = b;
        }

        public void run() {
            Log.d("tag", new HttpRequest().postRequest(mFrag, dataLists));
        }
    }

    static class DeleteThreadHttp extends Thread {
        int mId;
        boolean mFrag;
        public DeleteThreadHttp(int id, boolean b) {
            mId = id;
            mFrag = b;
        }

        public void run() {
            Log.d("tag", new HttpRequest().deleteRequest(mFrag, mId));
        }
    }

    static class EditViewThreadHttp extends Thread {
        int mId;
        EditDisplay mEdit;
        boolean mFrag;
        public EditViewThreadHttp(int id, EditDisplay editDisplay, boolean b) {
            mId = id;
            mEdit = editDisplay;
            mFrag = b;
        }

        public void run() {
            DataList dataLists = new HttpRequest().getIdRequest(mFrag, mId);
            if (dataLists != null) {
                mEdit.createEdit(dataLists);
            }
        }
    }

    static class EditThreadHttp extends Thread {
        DataList dataLists;
        boolean mFrag;
        public EditThreadHttp(DataList dataList, boolean b) {
            dataLists = dataList;
            mFrag = b;
        }

        public void run() {
            Log.d("tag", new HttpRequest().editRequest(mFrag, dataLists));

        }
    }
}
