package com.zys.jym.lanhu.utils;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.zys.jym.lanhu.activity.GroupCodeActivity;
import com.zys.jym.lanhu.activity.Main2Activity;
import com.zys.jym.lanhu.fragment.impl.HomeFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.zys.jym.lanhu.fragment.BaseFragment.mContext;


/**
 * Created by Administrator on 2017/1/4.
 */

public class LXRUtil {
    static String TAG = "TAG--LXRUtil";
    static int addNum = 0;
    static int delNum = 0;

    /**
     * 添加联系人到通讯录
     * @param mContext
     * @param name
     * @param phone
     * @param one
     * @param size
     * @param type
     */

    public static void addContacts(Activity mContext,String name, String phone, boolean one, int size, int type) {
        try {
            ContentValues values = new ContentValues();
            // 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
            Uri rawContactUri = mContext.getContentResolver().insert(
                    ContactsContract.RawContacts.CONTENT_URI, values);
            long rawContactId = ContentUris.parseId(rawContactUri);

            // 往data表插入姓名数据
            values.clear();
            values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);// 内容类型
            values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, "蓝狐微商-" + name);
            mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                    values);
            values.clear();
            values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.CommonDataKinds.Note.NOTE, "蓝狐微商");
            mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                    values);
            // 往data表插入电话数据
            values.clear();
            values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                    values);

            MyUtils.Loge(TAG, "添加成功");
            if (one) {
//                MyUtils.showToast(mContext, "添加成功，请在联系人中查看");
            } else {
                addNum++;
                if (addNum == size) {
                    if (type == 1) {
//                        HomeFragment.setServerData();
                        HomeFragment.handler1.sendEmptyMessage(1);
                    }
                    if (type == 2) {
                        Main2Activity.handler1.sendEmptyMessage(1);
                    }
                    if (type == 3) {

                        GroupCodeActivity.handler1.sendEmptyMessage(1);
                    }
                    addNum = 0;
                }
            }
        }catch (Exception e){
//            MyUtils.showToast(mContext,"打开手机设置，找到已安装的应用，蓝狐微商，允许读取联系人信息，允许写入联系人权限，即可使用");
            if (type == 1) {
                HomeFragment.handler1.sendEmptyMessage(3);
            }
            if (type == 2) {
                Main2Activity.handler1.sendEmptyMessage(2);
            }
            if (type == 3) {

                GroupCodeActivity.handler1.sendEmptyMessage(2);
            }
        }


    }

    /**
     * 清除手机通讯录里包含“蓝狐微商”的联系人
     * @param mContext
     */
    public static void deleteContacts(Activity mContext) {
        try {
            List<String> nameList = new ArrayList<>();
            Cursor cursor = mContext.managedQuery(ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
            while (cursor.moveToNext()) {
//            System.out.println(
//                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID)));
//            System.out.println(
//                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                if (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)) != null) {
                    if (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)).contains("蓝狐微商")) {
                        nameList.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    }
                }
            }
            MyUtils.Loge(TAG, "NAMELIST---" + nameList.size());
            for (int i = 0; i < nameList.size(); i++) {
                String name = nameList.get(i);
                //根据姓名求id
                Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
                ContentResolver resolver = mContext.getContentResolver();
                Cursor cursor1 = resolver.query(uri, new String[]{ContactsContract.Contacts.Data._ID}, "display_name=?", new String[]{name}, null);
                if (cursor1.moveToFirst()) {
                    int id = cursor1.getInt(0);
                    //根据id删除data中的相应数据
                    resolver.delete(uri, "display_name=?", new String[]{name});
                    uri = Uri.parse("content://com.android.contacts/data");
                    resolver.delete(uri, "raw_contact_id=?", new String[]{id + ""});
                    Log.e(TAG, "删除成功");
                    delNum++;
                }

//            if (one) {
//            MyUtils.showToast(mContext, "删除成功");
//            MySharedPrefrencesUtil.setParam(mContext, "phone_data","");
//            } else {
//                if (delNum == size) {
//
//                    delNum = 0;
//                }
//            }
                cursor1.close();
            }
            HomeFragment.handler1.sendEmptyMessage(2);
        }catch (Exception e){
            HomeFragment.handler1.sendEmptyMessage(3);
        }

    }
}
