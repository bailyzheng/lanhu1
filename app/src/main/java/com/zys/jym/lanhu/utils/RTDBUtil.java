package com.zys.jym.lanhu.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zys.jym.lanhu.db.ReleaseTimeOpenHelper;

/**
 * Created by Administrator on 2017/1/1.
 */

public class RTDBUtil {
    static String TAG = "TAG--RTDBUtil";
    private static ReleaseTimeOpenHelper oh;
    private static SQLiteDatabase db;
    private static int count;

    public static void insertCode(Context mContext,String uid, String time,String lasttime) {
        // 存入本地数据库
        oh = new ReleaseTimeOpenHelper(mContext);
        db = oh.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("uid", uid);
            values.put("time", time);
            values.put("lasttime", lasttime);
            db.insert("urtime", null, values);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG,e.toString());
        }
//        MyUtils.Loge(TAG,"time="+time);
        db.close();
    }
    /**
     * 根据uid查询当前数据有没有此数据
     */
    public static int selectCountFormUid( Context mContext,String uid) {
        oh = new ReleaseTimeOpenHelper(mContext);
        db = oh.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(
                    "select * from urtime where uid = ?",
                    new String[] { uid });
            count = cursor.getCount();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG,e.toString());
        }
        db.close();
        MyUtils.Loge(TAG,"count="+count);
        return count;
    }

    /**
     * 根据uid查询time
     */
    public static String[] selectTimeformUid(Context mContext,String uid) {

        String nTime="";
        String lTime="";
        oh = new ReleaseTimeOpenHelper(mContext);
        db = oh.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(
                    "select * from urtime where uid = ?",
                    new String[] { uid });
            if (cursor.moveToFirst()) {
                nTime = cursor.getString(cursor.getColumnIndex("time"));
                lTime= cursor.getString(cursor.getColumnIndex("lasttime"));
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG,e.toString());
        }
        db.close();
        String[] nltime={nTime,lTime};
        return nltime;
    }
    public static void updateUserTime(Context mContext, String uid, String time,String lasttime) {
        oh = new ReleaseTimeOpenHelper(mContext);
        db = oh.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("uid", uid);
            values.put("time", time);
            values.put("lasttime", lasttime);
            db.update("urtime", values, "uid=?",
                    new String[] { uid });
        } catch (Exception e) {
            // TODO: handle exception
        }
        db.close();
    }

}
