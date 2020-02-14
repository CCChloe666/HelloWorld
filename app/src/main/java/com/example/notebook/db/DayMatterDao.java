package com.example.notebook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.notebook.Entity.Card;
import com.example.notebook.Entity.DayMatter;

import java.util.ArrayList;
import java.util.List;

public class DayMatterDao {
    private Context mContext;
    private static MyDatabaseHelper dbHelper;

    public DayMatterDao(Context context) {
        this.mContext = context;
        dbHelper = new MyDatabaseHelper(context, DBstring.DB_NAME, null, DBstring.DB_VERSION);
    }

    public List<DayMatter> queryAllDay(String name) {
        DayMatter dayMatter = null;
        List<DayMatter> dayMattersList = new ArrayList<DayMatter>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from daymatter where user_name=" + name;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            dayMatter = new DayMatter();
            dayMatter.setId(cursor.getInt(cursor.getColumnIndex("id")));
            dayMatter.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            dayMatter.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
            dayMatter.setAimTime(cursor.getString(cursor.getColumnIndex("aimTime")));
            dayMattersList.add(dayMatter);
        }
        return dayMattersList;
    }

    public DayMatter getADay(int DayId) {
        DayMatter dayMatter = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from daymatter where id="+DayId;
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            dayMatter = new DayMatter();
            dayMatter.setId(cursor.getInt(cursor.getColumnIndex("id")));
            dayMatter.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            dayMatter.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
            dayMatter.setAimTime(cursor.getString(cursor.getColumnIndex("aimTime")));
        }
        return dayMatter;
    }

    public void updateDayMatter(DayMatter dayMatter){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",dayMatter.getTitle());
        values.put("createTime",dayMatter.getCreateTime());
        values.put("aimTime",dayMatter.getAimTime());
        db.update("daymatter", values, "id=?", new String[]{dayMatter.getId() + ""});
        db.close();
    }

    public int deleteADayMatter(int dayId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int ret = 0;
        ret = db.delete("daymatter", "id=?", new String[]{dayId + ""});
        if (db != null) {
            db.close();
        }
        return ret;
    }

}
