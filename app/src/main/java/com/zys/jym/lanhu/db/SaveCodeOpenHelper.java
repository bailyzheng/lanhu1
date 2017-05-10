package com.zys.jym.lanhu.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/1/1.
 */

public class SaveCodeOpenHelper extends SQLiteOpenHelper {
    public SaveCodeOpenHelper(Context context) {
        super(context, "codes.db", null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("create table savecode(_id integer primary key autoincrement,codeid char(10))");
        } catch (Exception e) {

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
