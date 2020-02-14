package com.example.notebook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.notebook.Entity.Card;

import java.util.List;

public class CardDao {
    private Context mContext;
    private static MyDatabaseHelper dbHelper;

    public CardDao(Context context) {
        this.mContext = context;
        dbHelper = new MyDatabaseHelper(context, DBstring.DB_NAME, null, DBstring.DB_VERSION);
    }


    public long insertCard(Card card, String name) {
        long ret = -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "insert into card(title,front_content,back_content,user_name)"
                + "values(?,?,?,?)";
        SQLiteStatement stat = db.compileStatement(sql);
        db.beginTransaction();
        stat.bindString(1, card.getTitle());
        stat.bindString(2, card.getFront_content());
        stat.bindString(3, card.getFront_content());
        stat.bindString(4, name);

        ret = stat.executeInsert();
        db.setTransactionSuccessful();

        db.endTransaction();
        db.close();
        return ret;
    }

    public Card getACard(int cardId) {
        Card card=null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from card where id="+cardId;
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            card = new Card();
            card.setId(cursor.getInt(cursor.getColumnIndex("id")));
            card.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            card.setFront_content(cursor.getString(cursor.getColumnIndex("front_content")));
            card.setBack_content(cursor.getString(cursor.getColumnIndex("back_content")));
        }
        return card;
    }

    public List<Card> gueryAllCard(String name){
        Card card= null;
        List<Card> cardList = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from card where user_name="+name;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            card = new Card();
            card.setId(cursor.getInt(cursor.getColumnIndex("id")));
            card.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            card.setFront_content(cursor.getString(cursor.getColumnIndex("front_content")));
            card.setBack_content(cursor.getString(cursor.getColumnIndex("back_content")));
            cardList.add(card);
        }
        return cardList;
    }


    public void updateCard(Card card){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",card.getTitle());
        values.put("front_content",card.getFront_content());
        values.put("back_content",card.getBack_content());
        db.update("task", values, "id=?", new String[]{card.getId() + ""});
        db.close();
    }

    public int deleteCard(int cardId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int ret = 0;
        ret = db.delete("task", "id=?", new String[]{cardId + ""});
        if (db != null) {
            db.close();
        }
        return ret;
    }

}
