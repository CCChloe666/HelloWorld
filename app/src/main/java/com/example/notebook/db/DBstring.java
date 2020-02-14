package com.example.notebook.db;

public class DBstring {
    public static final String DB_NAME = "db_notebook";

    public static final int DB_VERSION = 6;

    public static final String CREATE_WASTED_TABLE = "create table wasted_notes ("
            + "id integer primary key autoincrement,"
            + "title text,"
            + "content text,"
            + "date text,"
            + "address text,"
            + "timestamp float, "
            + "lastmodify float,"
            + "iswasted integer,"
            + "user_name text"
            + ")";


    public static final String CREATE_USER = "create table user ("
            + "id integer primary key autoincrement,"
            + "name text,"
            + "pwd text,"
            + "image text"
            + ")";

    public static final String CREATE_NOTE = "create table note ("
            + "id integer primary key autoincrement,"
            + "title text,"
            + "content text,"
            + "createTime text,"
            + "groupId integer,"
            + "groupName text,"
            + "isAdded integer,"
            + "isWasted integer,"
            + "isStared integer,"
            + "user_name text"
            + ")";

    public static final String CREATE_GROUP = "create table group_note ("
            + "id integer primary key autoincrement,"
            + "name text,"
            + "createTime text,"
            + "user_name text"
            + ")";

    public static final String CREATE_TASK = "create table task ("
            + "id integer primary key autoincrement,"
            + "name text,"
            + "score integer,"
            + "createTime text,"
            + "user_name text"
            + ")";

    public static final String CREATE_CARD = "create table card ("
            + "id integer primary key autoincrement,"
            + "title text,"
            + "front_content text,"
            + "back_content text,"
            + "user_name text"
            + ")";

    public static final String CREATE_DAYMATTER = "create table daymatter ("
            + "id integer primary key autoincrement,"
            + "title text,"
            + "aimTime text,"
            + "createTime text,"
            + "user_name text"
            + ")";

}
