package com.example.notebook.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.notebook.Entity.User;
import com.example.notebook.Util.ToastUtil;

public class UserDao {
    private Context mContext;

    private static MyDatabaseHelper dbHelper;

    public UserDao(Context context) {
        this.mContext = context;
        dbHelper = new MyDatabaseHelper(context, DBstring.DB_NAME, null, DBstring.DB_VERSION);
    }

    public long register(String name, String pwd) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql;
        Cursor cursor = null;
        long ret = 0;

        sql = "select * from user where name=" + name;
        cursor = db.rawQuery(sql, null);//找不到这个cursor是不是就是空的了？？
        if (cursor.moveToFirst()) {
            ToastUtil.showMsg(mContext, "用户名已存在");
        } else {
            Log.d("TAG", "register successfully!");
            sql = "insert into user(name,pwd,image)"//这需要在加两个null吗？
                    + "values(?,?,null)";
            SQLiteStatement stat = db.compileStatement(sql);
            db.beginTransaction();
            stat.bindString(1, name);
            stat.bindString(2, pwd);

            ret = stat.executeInsert();
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
        return ret;
    }

    public boolean login(String name, String pwd) {
        boolean flag = false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select pwd from user where name=" + name;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            flag = true;
        }
        return flag;
    }

    //添加头像
    public void addImage() {

    }



}
