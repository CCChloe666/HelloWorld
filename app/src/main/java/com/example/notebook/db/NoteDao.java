package com.example.notebook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.notebook.Entity.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteDao {
    private Context mContext;

    private static NoteDao noteDao;
    private static MyDatabaseHelper dbHelper;

    public NoteDao(Context context) {
        this.mContext = context;
        dbHelper = new MyDatabaseHelper(context, DBstring.DB_NAME, null, DBstring.DB_VERSION);
    }

    //获得一个笔记note
    public Note queryOne(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from note where id=" + id;
        Note note = null;
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor!=null){
            note = new Note();
            note.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            note.setContent(cursor.getString(cursor.getColumnIndex("content")));
            note.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
            note.setGroupId(cursor.getInt(cursor.getColumnIndex("groupId")));
            note.setType(cursor.getInt(cursor.getColumnIndex("type")));
            note.setBgColor(cursor.getString(cursor.getColumnIndex("bgColor")));
            note.setIsWasted(cursor.getInt(cursor.getColumnIndex("isWasted")));
        }
        return note;
    }


    /**
     * 查询所有笔记
     */
    public List<Note> queryAllNotes(int groupId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        List<Note> noteList = new ArrayList<>();
        Note note;
        String sql;
        Cursor cursor = null;
        if (groupId > 0) {
            sql = "select * from note where groupId =" + groupId
                    + "order by createTime desc";
        } else {
            sql = "select * from note";
        }
        cursor = db.rawQuery(sql, null);
//        cursor = db.query("note", null, null, null, null, null, "n_id desc");
        while (cursor.moveToNext()) {
            note = new Note();
            note.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            note.setContent(cursor.getString(cursor.getColumnIndex("content")));
            note.setGroupId(cursor.getInt(cursor.getColumnIndex("groupId")));
            note.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
            note.setType(cursor.getInt(cursor.getColumnIndex("type")));
            note.setBgColor(cursor.getString(cursor.getColumnIndex("bgColor")));
            note.setIsWasted(cursor.getInt(cursor.getColumnIndex("isWasted")));
            noteList.add(note);
        }
        if (cursor != null) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }

        return noteList;

    }

    /**
     * 插入笔记
     */
    public long insertNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "insert into note(title,content,createTime,"
                + "groupId,type,bgColor,isWasted)"
                + "values(?,?,?,?,?,?,?)";
        long ret = 0;
        SQLiteStatement stat = db.compileStatement(sql);
        db.beginTransaction();
        stat.bindString(1, note.getTitle());
        stat.bindString(2, note.getContent());
        stat.bindString(3, note.getCreateTime());
        stat.bindLong(4, note.getGroupId());
        stat.bindLong(5, note.getType());
        stat.bindString(6, note.getBgColor());
        stat.bindLong(7, note.getIsWasted());
        ret = stat.executeInsert();
        db.setTransactionSuccessful();

        db.endTransaction();
        db.close();

        return ret;

    }

    /**
     * 更新笔记
     *
     * @param note
     */
    public void updateNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("groupId", note.getGroupId());
        values.put("createTime", note.getCreateTime());
        values.put("type", note.getType());
        values.put("bgColor", note.getBgColor());
        values.put("isWasted", note.getIsWasted());
        db.update("note", values, "id=?", new String[]{note.getId() + ""});
        db.close();
    }

    /**
     * 删除笔记
     */
    public int deleteNote(int noteId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int ret = 0;
        ret = db.delete("note", "id=?", new String[]{noteId + ""});

        if (db != null) {
            db.close();
        }
        return ret;
    }


    /**
     * 批量删除笔记
     *
     * @param mNotes
     */
    public int deleteNote(List<Note> mNotes) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int ret = 0;

        if (mNotes != null && mNotes.size() > 0) {
            db.beginTransaction();
            for (Note note : mNotes) {
                ret += db.delete("note", "id=?", new String[]{note.getId() + ""});
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            if (db != null) {
                db.close();
            }

        }

        return ret;
    }


}
