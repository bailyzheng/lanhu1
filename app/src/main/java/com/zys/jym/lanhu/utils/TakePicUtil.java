package com.zys.jym.lanhu.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.ContextThemeWrapper;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/12/18.
 */

public class TakePicUtil {
    private static final int CROP_PHOTO = 2;
    private static final int REQUEST_CODE_PICK_IMAGE=3;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private static File mCurrentPhotoFile;// 照相机拍照得到的图片

    public static final int PHOTO_CROP = 0x178;// 图片裁剪

    private static final int INIT_WIDTH = 500, INIT_HEIGHT = 500;
    private static int cut_w, cut_h;// 裁剪图片宽度,高度

    private static File output;
    private static Uri imageUri;

    /**
     * 拍照的照片存储位置
     */
    private static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/lh/Camera/");



    public static void doPickPhotoAction(final Activity context) {
        final Context dialogContext = new ContextThemeWrapper(context,
                android.R.style.Theme_Light);
        String[] choices;
        choices = new String[2];
        choices[0] = "拍照"; // 拍照
        choices[1] = "从相册中选择"; // 从相册中选择
        final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
                android.R.layout.simple_list_item_1, choices);

        final AlertDialog.Builder builder = new AlertDialog.Builder(
                dialogContext);
        builder.setTitle("选择路径");
        builder.setSingleChoiceItems(adapter, -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String status = Environment.getExternalStorageState();
                        if (!status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡

                            Toast.makeText(dialogContext,
                                    "没有找到SD卡或者正在使用请关闭usb连接模式",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        switch (which) {
                            case 0:
                                // 用户点击了从照相机获取
                                if (ContextCompat.checkSelfPermission(context,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        != PackageManager.PERMISSION_GRANTED)
                                {
                                    ActivityCompat.requestPermissions(context,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);

                                }else {
                                    takePhoto(context);
                                }
                                break;
                            case 1:
                                // 从相册中去获取
                                if (ContextCompat.checkSelfPermission(context,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        != PackageManager.PERMISSION_GRANTED)
                                {
                                    ActivityCompat.requestPermissions(context,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);

                                }else {
                                    choosePhoto(context);
                                }


                                break;
                        }
                    }
                });
        builder.setNegativeButton(
                "取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        builder.create().show();
    }


    /**
     * 拍照
     */
    static void takePhoto(Activity context){
        /**
         * 最后一个参数是文件夹的名称，可以随便起
         */
        File file=new File(Environment.getExternalStorageDirectory(),"LHPZ");
        if(!file.exists()){
            file.mkdir();
        }
        /**
         * 这里将时间作为不同照片的名称
         */
        output=new File(file,System.currentTimeMillis()+"lh.jpg");

        /**
         * 如果该文件夹已经存在，则删除它，否则创建一个
         */
        try {
            if (output.exists()) {
                output.delete();
            }
            output.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 隐式打开拍照的Activity，并且传入CROP_PHOTO常量作为拍照结束后回调的标志
         */
        imageUri = Uri.fromFile(output);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        context.startActivityForResult(intent, CROP_PHOTO);

    }

    /**
     * 从相册选取图片
     */
   static void choosePhoto(Activity context){
        /**
         * 打开选择图片的界面
         */
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        context.startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

    }


    /**
     * 所有图片裁剪回调 如不需要裁剪及不用调用该方法
     *
     * @param context
     * @param
     */
    public static void doCropPhoto(Activity context, Intent data) {
        try {

            Intent intent = new Intent("com.android.camera.action.CROP");
            Uri uri = null;
            if (data.getData() != null) {
                uri = data.getData();
            } else {
                uri = Uri.fromFile(getCameraPath(data)); // 拍照返回数据
            }
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", true);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", cut_w != 0 ? cut_w : INIT_WIDTH);
            intent.putExtra("outputY", cut_h != 0 ? cut_h : INIT_HEIGHT);
            context.startActivityForResult(intent, PHOTO_CROP);
        } catch (Exception e) {
            Toast.makeText(context, "没有找到图片", Toast.LENGTH_LONG).show();

        }
    }

    /**
     * 拍照图片路径
     *
     * @return
     */
    private static File getCameraPath(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap myBitmap = (Bitmap) extras.get("data");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(MediaUtil.getmCurrentPhotoFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        return getmCurrentPhotoFile();
    }

    /**
     * 得到本地图片路径
     *
     * @return
     */
    public static File getmCurrentPhotoFile() {
        if (mCurrentPhotoFile == null) {
            if (!PHOTO_DIR.exists())
                PHOTO_DIR.mkdirs();// 创建照片的存储目录
            mCurrentPhotoFile = new File(PHOTO_DIR, "temp.jpg");
            if (!mCurrentPhotoFile.exists())
                try {
                    mCurrentPhotoFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
        return mCurrentPhotoFile;
    }
}
