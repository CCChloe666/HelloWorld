package com.example.notebook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.notebook.Entity.Group;
import com.example.notebook.Util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class GroupDao {
    private Context mContext;

    private MyDatabaseHelper dbHelper;

    private SQLiteDatabase db;

    public GroupDao(Context context) {
        this.mContext = context;
        dbHelper = new MyDatabaseHelper(context, DBstring.DB_NAME, null, DBstring.DB_VERSION);
    }

    /**
     * 查询所有分类
     *
     * @return
     */
    public List<Group> queryGroupAll(String name) {
        this.db = dbHelper.getWritableDatabase();
        List<Group> groupList = new ArrayList<Group>();
        String sql = "select * from group_note where user_name=" + name;
        Group group = null;

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            group = new Group();
            group.setId(cursor.getInt(cursor.getColumnIndex("id")));
            group.setName(cursor.getString(cursor.getColumnIndex("name")));
            group.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
            groupList.add(group);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }
        return groupList;
    }

    /**
     * 根据分类名查询分类
     *
     * @param groupName
     * @return
     */
    public Group queryGroupByName(String groupName) {
        this.db = dbHelper.getWritableDatabase();
        String sql = "select * from group_note where name=" + groupName;
        Group group = null;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            group = new Group();
            group.setId(cursor.getInt(cursor.getColumnIndex("id")));
            group.setName(groupName);
            group.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }
        return group;
    }

    /**
     * 根据分类ID查询分类
     *
     * @return
     */
    public Group queryGroupById(int groupId) {
        this.db = dbHelper.getWritableDatabase();
        Group group = null;
        String sql = "select * from group_note where id=" + groupId;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            group = new Group();
            group.setId(groupId);
            group.setName(cursor.getString(cursor.getColumnIndex("name")));
            group.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }
        return group;
    }

    /**
     * 添加一个分类
     */
    public void insertGroup(String groupName, String createTime,String name) {
        this.db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("group_note", null, "name=?", new String[]{groupName}, null, null, null);
        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put("name", groupName);
            values.put("createTime", createTime);
            values.put("user_name",name);
            db.insert("group_note", null, values);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }
    }

    /**
     * 更新一个分类
     */
    public void updateGroup(Group group) {
        this.db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", group.getName());
        values.put("createTime", group.getCreateTime());
        db.update("group_note", values, "id=?", new String[]{group.getId() + ""});
        if (db != null) {
            db.close();
        }
    }

    /**
     * 删除一个分类
     */
    public int deleteGroup(int groupId) {
        this.db = dbHelper.getWritableDatabase();
        int ret = db.delete("group_note", "id=?", new String[]{groupId + ""});
        if (db != null) {
            db.close();
        }
        return ret;
    }


}
