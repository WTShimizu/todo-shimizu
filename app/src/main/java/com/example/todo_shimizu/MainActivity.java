package com.example.todo_shimizu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    public UserData userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userData = new UserData(getApplicationContext());

        AddDisplay fragment = new AddDisplay();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainPageLayout, fragment);
        transaction.commit();

    }

    public void replaceFragmentManager(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainPageLayout, fragment);
        transaction.commit();
    }

//    public void insertData(String com, String exp, boolean status, String day, String compday){
//        if (db == null) {
//            db = userData.getWritableDatabase();
//        }
//        String dayMold;
//        String compDayMold = compday.replace("/", "");
//        if (day != "") {
//            dayMold = day.replace("/", "");
//        } else {
//            dayMold = "0";
//        }
//
//        int statusFrag = 0;
//        String dbName;
//        if (exp == null) {
//            exp = "";
//        }
//        long result;
//        if (status) {
//            statusFrag = 0;
//            dbName = "userdb";
//            ContentValues values = new ContentValues();
//            values.put("title", com);
//            values.put("exp", exp);
//            values.put("status", statusFrag);
//            values.put("day", Integer.parseInt(dayMold));
//            values.put("compday", compDayMold);
//            values.put("listid", 0);
//            result  = db.insert(dbName, null, values);
//        } else {
//            statusFrag = 1;
//            dbName = "testdb";
//            ContentValues values = new ContentValues();
//            values.put("title", com);
//            values.put("exp", exp);
//            values.put("status", statusFrag);
//            values.put("day", Integer.parseInt(dayMold));
//            values.put("listid", 0);
//            result  = db.insert(dbName, null, values);
//        }
//
//        if (result == -1) {
//            Log.d("tag", "登録失敗");
//        } else {
//            Log.d("tag", "登録成功");
//        }
//    }
//
//    public Cursor readData(boolean dbFrag) {
//        if (db == null) {
//            db = userData.getWritableDatabase();
//        }
//        String readToDb;
//        String[] readToData;
//        if (dbFrag) {
//            readToDb = "userdb";
//            readToData = new String[] { "title", "day" , "exp", "status", "_id", "compday", "listid"};
//        } else {
//            readToDb = "testdb";
//            readToData = new String[] { "title", "day" , "exp", "status", "_id", "listid"};
//        }
//        String order_by = "day ASC";
//        Cursor cursor = db.query(
//                readToDb,
//                readToData,
//                null,
//                null,
//                null,
//                null,
//                order_by
//        );
//
//        cursor.moveToFirst();
//
//
//        return cursor;
//    }
//
//    public void delete() {
//        if (db == null) {
//            db = userData.getWritableDatabase();
//        }
//        db.delete("testdb", null, null);
//    }
//
//    public void selectDelete(String position, boolean dbFrag) {
//        String readToDb;
//        if (dbFrag) {
//            readToDb = "userdb";
//        } else {
//            readToDb = "testdb";
//        }
//        db.beginTransaction();
//        try {
//            db.delete(readToDb, "_id=" + position, null);
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            db.endTransaction();
//        }
//    }
//
////    public void updateuserdb(int listId) {
////        if (db == null) {
////            db = userData.getWritableDatabase();
////        }
////
////        ContentValues cv = new ContentValues();
////        cv.put("listid", listId);
////        db.update("userdb", cv, "listid=" + String.valueOf(listId), null);
////        cv = new ContentValues();
////        cv.put("listid", listId);
////        db.update("testdb", cv, "listid=" + String.valueOf(listId), null);
////    }
//
//    public void editData(String com, String exp, boolean status, String day, String compday, int id){
//        if (db == null) {
//            db = userData.getWritableDatabase();
//        }
//        ContentValues cv = new ContentValues();
//        int statusFrag = 0;
//        String dbName;
//        if (exp == null) {
//            exp = "";
//        }
//        String dayMold;
//        String compDayMold = compday.replace("/", "");
//        if (day != "") {
//            dayMold = day.replace("/", "");
//        } else {
//            dayMold = "0";
//        }
//        String readToDb;
//        if (status) {
//            statusFrag = 0;
//            readToDb = "userdb";
//            cv.put("title", com);
//            cv.put("exp", exp);
//            cv.put("status", statusFrag);
//            cv.put("day", Integer.parseInt(dayMold));
//            cv.put("compday", Integer.parseInt(compDayMold));
//            db.update(readToDb, cv, "_id = " + String.valueOf(id), null);
//        } else {
//            statusFrag = 1;
//            readToDb = "testdb";
//            cv.put("title", com);
//            cv.put("exp", exp);
//            cv.put("status", statusFrag);
//            cv.put("day", Integer.parseInt(dayMold));
//            db.update(readToDb, cv, "_id = " + String.valueOf(id), null);
//        }
//    }
}