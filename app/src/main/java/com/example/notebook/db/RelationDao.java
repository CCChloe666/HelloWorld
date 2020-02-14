package com.example.notebook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class RelationDao {

    private Context mContext;
    private static MyDatabaseHelper dbHelper;

    public RelationDao(Context context) {
        this.mContext = context;
        dbHelper = new MyDatabaseHelper(context, DBstring.DB_NAME, null, DBstring.DB_VERSION);
    }

    public long insertUserId(int id){
        long ret=-1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        return ret;
    }

    public long insertNoteId(){
        long ret=-1;

        return ret;
    }

    public long insertGroupId(){
        long ret=-1;

        return ret;
    }

    public long insertTaskId(){
        long ret=-1;

        return ret;
    }

    public long insertCardId(){
        long ret=-1;

        return ret;
    }

    public long insertDayMatterId(){
        long ret=-1;

        return ret;
    }


}
