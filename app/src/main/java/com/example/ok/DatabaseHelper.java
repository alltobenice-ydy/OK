package com.example.ok;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

//    private static final String DB_NAME = "mydata.db"; //数据库名称
//    private static final int version = 1; //数据库版本

    DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO 创建数据库后，对数据库的操作
        String sql = "create table test4(username varchar(20) not null , password varchar(60) not null );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 更改数据库版本的操作
    }

//    @Override
//    public void onOpen(SQLiteDatabase db) {
//
//        super.onOpen(db);
//        // TODO 每次成功打开数据库后首先被执行
//    }
}