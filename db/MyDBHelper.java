package com.example.miogk.myhupu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/6/25.
 */

public class MyDBHelper extends SQLiteOpenHelper {
    public static final int PAGE_SIZE = 20;
    public static final int DB_VERSION = 7;
    public static final String DB_NAME = "myHupu.db";
    public static final String USER_TABLE = "user";
    public static final String POST_TABLE = "post";
    public static final String USER_TABLE_TABLE = "user_table";
    public static final String MESSAGE = "message";
    //    public static final String REPLY_TABLE = "reply";
    public static final String SQL_USER = "(userid integer primary key autoincrement, username text, password text, lighten int)";
    public static final String SQL_POST = "(postid integer primary key autoincrement, username text, title text, content text, time text, lastreply text)";
    public static final String SQL_USER_TABLE = "(user_table_id integer primary key autoincrement, username text, table_name text, table_row_id number, time text);";
    public static final String SQL_MESSAGE = "(messageid integer primary key autoincrement, postid text, username text, rUsername text, rContent text, table_row_id text)";

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + USER_TABLE + SQL_USER;
        String sql_2 = "create table if not exists " + POST_TABLE + SQL_POST;
        String sql_3 = "create table if not exists " + USER_TABLE_TABLE + SQL_USER_TABLE;
        String sql_4 = "create table if not exists " + MESSAGE + SQL_MESSAGE;
        db.execSQL(sql);
        db.execSQL(sql_2);
        db.execSQL(sql_3);
        db.execSQL(sql_4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists " + USER_TABLE;
        String sql_2 = "drop table if exists " + POST_TABLE;
        String sql_3 = "drop table if exists " + USER_TABLE_TABLE;
        String sql_4 = "drop table if exists " + MESSAGE;
        db.execSQL(sql);
        db.execSQL(sql_2);
        db.execSQL(sql_3);
        db.execSQL(sql_4);
        onCreate(db);
    }
}