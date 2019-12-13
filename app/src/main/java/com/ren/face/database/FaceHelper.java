package com.ren.face.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.ren.face.constant.Constant.db_name;

public class FaceHelper extends SQLiteOpenHelper {
    public FaceHelper(@Nullable Context context) {
        super(context, db_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String account_tb="create table t_account (id integer  primary key AUTOINCREMENT ,account text,name text,pwd text,role integer)";
        String time_tb="create table t_time(account text ,time text)";
        db.execSQL(account_tb);
        db.execSQL(time_tb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
