package com.zys.jym.lanhu.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zys.jym.lanhu.db.SaveCodeOpenHelper;

/**
 * Created by Administrator on 2017/1/1.
 */

public class SaveCodeUtil {
    static String TAG = "TAG--SaveCodeUtil";
    private static SaveCodeOpenHelper oh;
    private static SQLiteDatabase db;
    private static int count;

    public static void insertCode(Context mContext, String codeid) {
        // 存入本地数据库
        oh = new SaveCodeOpenHelper(mContext);
        db = oh.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("codeid", codeid);
            db.insert("savecode", null, values);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG,e.toString());
        }
        MyUtils.Loge(TAG,"codeid="+codeid);
        db.close();
    }

    /**
     * 根据codeId查询当前数据有没有此二维码
     */
    public static int selectCountFormCodeId( Context mContext,String codeId) {
        oh = new SaveCodeOpenHelper(mContext);
        db = oh.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(
                    "select * from savecode where codeid = ?",
                    new String[] { codeId });
            count = cursor.getCount();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG,e.toString());
        }
        db.close();
        MyUtils.Loge(TAG,"count="+count);
        return count;
    }


}
