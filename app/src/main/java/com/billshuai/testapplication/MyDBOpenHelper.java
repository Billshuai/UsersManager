package com.billshuai.testapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Houge on 2017/4/11.
 */

public class MyDBOpenHelper extends SQLiteOpenHelper {

    //数据库名称
    private static final String DATABASE_NAME = "billshuai.db";

    //数据库版本号
    private static final int DATABASE_VERSION = 1;

    public MyDBOpenHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    //第一次创建数据库时,数据库SQL语句 添加一个表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name varchar(30), password varchar(30))");
    }

    //数据库版本更改时
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
