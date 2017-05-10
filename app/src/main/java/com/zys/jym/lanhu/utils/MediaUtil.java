package com.zys.jym.lanhu.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zys.jym.lanhu.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import pub.devrel.easypermissions.EasyPermissions;

public class MediaUtil {
    static String TAG = "TAG--BitmapUtil";

    public final static int SELECT_PHOTO_CODE = 0x166;// 相册
    public final static int CAMERA_REQUEST_CODE = 0x168;// 相机
    public final static int SELECT_VIDEO_CODE = 0x170;// 视频库
    public final static int TACK_VIDEO_CODE = 0x172;// 录制视频
    public final static int SELECT_VOICE_CODE = 0x174;// 视频库
    public final static int TACK_VIOCE_CODE = 0x176;// 录制视频
    public static final int PHOTO_CROP = 0x178;// 图片裁剪
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;

    private static File file;
    private static File mCurrentPhotoFile;// 照相机拍照得到的图片
    private static final int INIT_WIDTH = 500, INIT_HEIGHT = 500;
    private static int cut_w, cut_h;// 裁剪图片宽度,高度
    private static final int INIT_WIDTH2 = 600, INIT_HEIGHT2 = 800;
    /**
     * 拍照的照片存储位置
     */
    private static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/lh/Camera/");


    public static void doPickPhotoAction2(final Activity context) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = context.getLayoutInflater().inflate(R.layout.view_dialog_photo, null);
        TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
        TextView tv_photo = (TextView) view.findViewById(R.id.tv_photo);
        TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        final Dialog finalDialog = dialog;
        final String status = Environment.getExternalStorageState();
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
                if (!status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡

                    Toast.makeText(context,
                            "没有找到SD卡或者正在使用请关闭usb连接模式",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                doTakePhoto(context);// 用户点击了从照相机获取
            }
        });
        tv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
                if (!status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡

                    Toast.makeText(context,
                            "没有找到SD卡或者正在使用请关闭usb连接模式",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                doPickPhotoFromGallery(context);// 从相册中去获取
            }
        });

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
            }
        });
    }

    /**
     * 开始启动照片选择框
     *
     * @param context
     */
    public static void doPickPhotoAction(final Activity context) {
        final Context dialogContext = new ContextThemeWrapper(context,
                android.R.style.Theme_Light);
        String[] choices;
        choices = new String[2];
        choices[0] = "拍照"; // 拍照
        choices[1] = "相册"; // 从相册中选择
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
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED ) {
                                    // app还没有使用摄像头的权限，调用该方法进行申请，同时给出了相应的说明文案，提高用户同意的可能性
                                    EasyPermissions.requestPermissions(context, "请允许申请打开摄像头权限，否则本应用无法进行拍照",
                                            0x11, Manifest.permission.CAMERA);
                                } else {
                                    // 已经有摄像头权限了，可以使用该权限完成app的相应的操作了
                                    doTakePhoto(context);// 用户点击了从照相机获取
                                }
//                                // 用户点击了从照相机获取
//                                if (ContextCompat.checkSelfPermission(context,
//                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                                        != PackageManager.PERMISSION_GRANTED)
//                                {
//                                    ActivityCompat.requestPermissions(context,
//                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);
//
//                                }else {
//                                    doTakePhoto(context);// 用户点击了从照相机获取
//                                }

                                break;
                            case 1:

                                // 从相册中去获取
                                if (ContextCompat.checkSelfPermission(context,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    EasyPermissions.requestPermissions(context, "请允许申请读取图片权限，否则本应用无法进行图片上传",
                                            0x13, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                } else {
                                    doPickPhotoFromGallery(context);
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
     * 拍照获取图片
     */
    public static void doTakePhoto(Activity context) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            context.startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "路径未找到", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 请求Gallery相册程序
     *
     * @param context
     * @param
     */
    public static void doPickPhotoFromGallery(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
            ((Activity) context).startActivityForResult(intent,
                    SELECT_PHOTO_CODE);
        } catch (Exception e) {
            Toast.makeText(context, "路径未找到", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 拍照后保存
     *
     * @param bm
     * @return
     */
    public static Uri saveBitmap(Context context, Bitmap bm) {
        File tmpDir = new File(Environment.getExternalStorageDirectory()
                + "/Bid");
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }
        File img = new File(tmpDir.getAbsolutePath() + "Bidapp.png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(context, "成功了", Toast.LENGTH_SHORT).show();
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "失败了", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "失败了", Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    /**
     * @param inStream
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;

    }

    /**
     * @param bytes
     * @param opts
     * @return Bitmap
     */
    public static Bitmap Bytes2Bitmap(byte[] bytes, Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    /**
     * @param bitmap 对象
     * @param w      要缩放的宽度
     * @param h      要缩放的高度
     * @return newBmp 新 Bitmap对象
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newBmp;
    }

    /**
     * 把Bitmap转Byte
     *
     * @Author HEH
     * @EditTime 2010-07-19 上午11:45:56
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 把字节数组保存为一个文件
     *
     * @Author HEH
     * @EditTime 2010-07-19 上午11:45:56
     */
    public static File Bytes2File(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 将字符串转换成Bitmap类型
     *
     * @param string
     * @return Bitmap
     */
    public static Bitmap stringtoBitmap(String string) {

        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 将Bitmap转换成字符串
     *
     * @param bitmap
     * @return String
     */
    public static String bitmaptoString(Bitmap bitmap) {

        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    /**
     * 图片压缩处理，size参数为压缩比，比如size为2，则压缩为1/4,size是动态的
     **/
    public static Bitmap compressBitmap(String path, byte[] data,
                                        Context context, Uri uri, int size, boolean width,
                                        int imageViewWidth, int imageViewHeight) {
        Options options = null;
        if (size > 0) {
            Options info = new Options();
            /** 如果设置true的时候，decode时候Bitmap返回的为数据将空 */
            info.inJustDecodeBounds = false;
            decodeBitmap(path, data, context, uri, info);
            // 拿到图片宽高 把图片宽高读取放在Options里
            int imageWidth = info.outWidth;
            int imageHeight = info.outHeight;

            int scaleWidth = imageWidth / imageViewWidth;
            int scaleHeight = imageHeight / imageViewHeight;
            if (scaleWidth >= scaleHeight && scaleWidth >= 1) {
                size = scaleWidth;
            } else if (scaleWidth < scaleHeight && scaleHeight >= 1) {
                size = scaleHeight;
            }

            options = new Options();
            /** 动态设置压缩比例 */
            options.inSampleSize = size;// 压缩比例
            MyUtils.Loge(TAG, "压缩比例=" + size);
        }
        Bitmap bm = null;
        try {
            bm = decodeBitmap(path, data, context, uri, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    /**
     * 把byte数据解析成图片
     */
    private static Bitmap decodeBitmap(String path, byte[] data,
                                       Context context, Uri uri, Options options) {
        Bitmap result = null;
        if (path != null) {
            result = BitmapFactory.decodeFile(path, options);
        } else if (data != null) {
            result = BitmapFactory.decodeByteArray(data, 0, data.length,
                    options);
        } else if (uri != null) {
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = null;
            try {
                inputStream = cr.openInputStream(uri);
                result = BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Bitmap url2Bitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTextFromStream(InputStream is) {

        byte[] b = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while ((len = is.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            String text = new String(bos.toByteArray());
            bos.close();
            return text;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * uri转Filepath
     *
     * @param context
     * @param uri
     * @return
     */
    public static String uri2Filepath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static String getFilePath(Activity context, Uri uri) {
        String filename = null;
        if (uri.getScheme().toString().compareTo("content") == 0) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Audio.Media.DATA}, null, null, null);
            if (cursor.moveToFirst()) {
                filename = cursor.getString(0);
            }
        } else if (uri.getScheme().toString().compareTo("file") == 0)         //file:///开头的uri
        {
            filename = uri.toString();
            filename = uri.toString().replace("file://", "");
            //替换file://
            if (!filename.startsWith("/mnt")) {
                //加上"/mnt"头
                filename += "/mnt";
            }
        }
        if (filename == null) {
            return uri2Filepath(context, uri);
        }
        return filename;
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param imageUri
     */
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }

        return getFilePath(context, imageUri);
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 保存图片file文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static File Bitmap2File(Context context, Bitmap bm, String fileName)
            throws IOException {
        File dirFile = null;
        if (getSDPath(context) != null) {
            try {
                String path = getSDPath(context) + "/Bid/";
                dirFile = new File(path);
                if (!dirFile.exists()) {
                    dirFile.mkdir();
                }
                File myCaptureFile = new File(path + fileName);
                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(myCaptureFile));
                bm.compress(CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();

            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return dirFile;

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
     * 二维码剪裁
     *
     * @param context
     * @param
     */
    public static void doCropPhotore(Activity context, Intent data) {
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
            intent.putExtra("aspectX", 3);
            intent.putExtra("aspectY", 4);
            intent.putExtra("outputX", cut_w != 0 ? cut_w : INIT_WIDTH2);
            intent.putExtra("outputY", cut_h != 0 ? cut_h : INIT_HEIGHT2);
            context.startActivityForResult(intent, PHOTO_CROP);
        } catch (Exception e) {
            Toast.makeText(context, "没有找到图片", Toast.LENGTH_LONG).show();
//            MyUtils.Loge(TAG,"没有找到图片");

        }
    }

    /**
     * 获取不裁剪时图片返回的路径
     *
     * @param activity
     * @param data
     * @return path
     */
    @SuppressWarnings("deprecation")
    public static String getNoCropPath(Activity activity, Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            return getCameraPath(data).toString();// 获取拍照图片路径
        }
        String path = "";
        Uri imageuri = data.getData();
        if (imageuri != null) {
            String[] proj = {MediaColumns.DATA};
            Cursor cursor = activity.managedQuery(imageuri, proj, null, null,
                    null);
            int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            if (cursor.moveToFirst()) {
                path = cursor.getString(column_index);
            }
        }
        return path;
    }

    /**
     * 裁剪后的图片路径
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getCropPath(Activity activity, Intent data) {

        String path = "";
        Uri imageuri = data.getData();
        if (imageuri == null) {
            String str = data.getAction();
            imageuri = Uri.parse(str);
        }
        String[] proj = {MediaColumns.DATA};
        Cursor cursor = activity.managedQuery(imageuri, proj, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            if (cursor.moveToFirst()) {
                path = cursor.getString(column_index);
            }
        }
        if (path == "") {
            path = getCameraPath(data).toString();
        }
        return path;
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
        myBitmap.compress(CompressFormat.JPEG, 100, fos);
        return MediaUtil.getmCurrentPhotoFile();
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

    /**
     * 设置裁剪图片尺寸
     *
     * @param w
     * @param h
     */
    public static void InitCutSize(int w, int h) {
        cut_w = w;
        cut_h = h;
    }

    /**
     * 得到SD卡路径
     *
     * @return
     */
    public static String getSDPath(Context context) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        } else {
            MyUtils.showToast(context, "sd路径不存在");
        }
        return sdDir.toString();
    }

//    /**
//     * 视频缩略图
//     *
//     * @param filePath
//     * @return
//     */
//    public static Bitmap getVideoThumbnail(String filePath) {
//        Bitmap bitmap = null;
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        try {
//            retriever.setDataSource(filePath);
//            bitmap = retriever.getFrameAtTime();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                retriever.release();
//            } catch (RuntimeException e) {
//                e.printStackTrace();
//            }
//        }
//        return bitmap;
//    }

    //返回压缩后的图片path
    public static String compressImage(String filePath, String targetPath, int quality) {
        Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片
        int degree = readPictureDegree(filePath);//获取相片拍摄角度
        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bm = rotateBitmap(bm, degree);
        }
        File outputFile = new File(targetPath);
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                //outputFile.createNewFile();
            } else {
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } catch (Exception e) {
        }
        return outputFile.getPath();
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 600, 800);
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }


    /**
     * 获取照片角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转照片
     *
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    public static File saveBitmapFile(Bitmap bitmap){
        File file=new File(Environment.getExternalStorageDirectory() + "/lh/YaSuoCode/"+ TimeUtil.getTimeStamp()+"upCode.jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
