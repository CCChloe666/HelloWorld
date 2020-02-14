package com.example.notebook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.notebook.Util.ToastUtil;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;

    public MyDatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBstring.CREATE_USER);
        db.execSQL(DBstring.CREATE_NOTE);
        db.execSQL(DBstring.CREATE_GROUP);
        db.execSQL(DBstring.CREATE_TASK);
        db.execSQL(DBstring.CREATE_CARD);
        db.execSQL(DBstring.CREATE_DAYMATTER);
        db.execSQL(DBstring.CREATE_WASTED_TABLE);
        ToastUtil.showMsg(mContext,"database created successfully!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        db.execSQL("drop table if exists note");
        db.execSQL("drop table if exists group_note");
        db.execSQL("drop table if exists task");
        db.execSQL("drop table if exists daymatter");
        db.execSQL("drop table if exists card");
        db.execSQL("drop table if exists wasted_notes");
        onCreate(db);
        ToastUtil.showMsg(mContext,"数据库changed.");
    }
}
