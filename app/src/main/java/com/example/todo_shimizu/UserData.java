package com.example.todo_shimizu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserData extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "TestDB.db";
    private static final String TABLE_NAME = "testdb";
    private static final String TABLE_NAME2 = "userdb";
    private static final String _ID = "_id";
    private static final String LIST_ID = "listid";
    private static final String COLUMN_NAME_TITLE = "title";
    private static final String COLUMN_NAME_SUBTITLE = "day";
    private static final String COLUMN_NAME_SUBTITLE2 = "compday";
    private static final String COLUMN_NAME_EXP = "exp";
    private static final String COLUMN_NAME_STATUS = "status";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    LIST_ID + " INTEGER," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_EXP + " TEXT," +
                    COLUMN_NAME_STATUS + " INTEGER," +
                    COLUMN_NAME_SUBTITLE + " INTEGER)";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final String SQL_CREATE_ENTRIES2 =
            "CREATE TABLE " + TABLE_NAME2 + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    LIST_ID + " INTEGER," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_EXP + " TEXT," +
                    COLUMN_NAME_STATUS + " INTEGER," +
                    COLUMN_NAME_SUBTITLE2 + " INTEGER," +
                    COLUMN_NAME_SUBTITLE + " INTEGER)";
    private static final String SQL_DELETE_ENTRIES2 = "DROP TABLE IF EXISTS " + TABLE_NAME;

    UserData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                SQL_CREATE_ENTRIES
        );
        db.execSQL(
                SQL_CREATE_ENTRIES2
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(
                SQL_DELETE_ENTRIES
        );
        db.execSQL(
                SQL_DELETE_ENTRIES2
        );
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}
