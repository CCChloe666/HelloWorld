package com.example.notebook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TestHelper extends SQLiteOpenHelper {

    public static final String CREATE_NOTE = "create table note ("
            +"id integer primary key autoincrement"
            +"title"
            +"content"
            +"";

    public TestHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
