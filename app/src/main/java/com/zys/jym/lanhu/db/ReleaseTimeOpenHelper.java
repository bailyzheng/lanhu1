package com.zys.jym.lanhu.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/1/1.
 */

public class ReleaseTimeOpenHelper extends SQLiteOpenHelper {
    public ReleaseTimeOpenHelper(Context context) {
        super(context, "releasetime.db", null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table urtime(_id integer primary key autoincrement,uid char(20),time char(50),lasttime char(50))");
        } catch (Exception e) {

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
