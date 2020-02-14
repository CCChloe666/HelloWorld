package com.example.notebook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.notebook.Entity.Group;
import com.example.notebook.Entity.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskDao {
    private Context mContext;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public TaskDao(Context context) {
        this.mContext = context;
        dbHelper = new MyDatabaseHelper(context, DBstring.DB_NAME, null, DBstring.DB_VERSION);
    }

    /**
     * 查询所有任务
     *
     * @return
     */
    public List<Task> queryAllTasks(String name) {
        this.db = dbHelper.getWritableDatabase();
        List<Task> taskList = new ArrayList<Task>();
        String sql = "select * from task where user_name=" + name;
        Task task = null;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            task = new Task();
            task.setId(cursor.getInt(cursor.getColumnIndex("id")));
            task.setName(cursor.getString(cursor.getColumnIndex("name")));
            task.setScore(cursor.getInt(cursor.getColumnIndex("score")));
            task.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
            taskList.add(task);
        }
        return taskList;
    }

    /**
     * 查询一天的任务
     *
     * @return
     */
    public Task queryOneDayTask(String createTime, String name) {
        Task task = null;
        String sql = "select * from task where createTime=" + createTime + "& user_name=" + name;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            task = new Task();
            task.setId(cursor.getInt(cursor.getColumnIndex("id")));
            task.setName(cursor.getString(cursor.getColumnIndex("name")));
            task.setScore(cursor.getInt(cursor.getColumnIndex("score")));
            task.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
        }
        return task;
    }

    public void updateTask(Task task){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",task.getName());
        values.put("score",task.getScore());
        values.put("createTime",task.getCreateTime());
        db.update("task", values, "id=?", new String[]{task.getId() + ""});
        db.close();
    }

    public int deleteTask(int taskId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int ret = 0;
        ret = db.delete("task", "id=?", new String[]{taskId + ""});
        if (db != null) {
            db.close();
        }
        return ret;
    }


}
