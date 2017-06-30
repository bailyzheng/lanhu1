package com.zys.jym.lanhu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import com.zys.jym.lanhu.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author ※简单※
 * @Project: tailorpus
 * @Title: PictureMenu.java
 * @Package com.cld.tailorpus.picture
 * @Description: 图片菜单
 * 1、拍照
 * 2、从相册选择
 * @date 2015年11月10日
 */
public class PictureMenu extends DialogFragment implements OnClickListener {
    private Activity mActivity;
    /**
     * 是否为单张图片
     */
    private boolean isSingle = false;
    /**
     * 拍照的图片文件
     */
    public File mTakePhotoFile;

    /**
     * 裁剪的图片文件
     */
    public File mCropPhotoFile;
    private String mPhotoPath;
    private File mPhotoFile;

    /**
     * 图片相关操作
     */
    public static final int REQUEST_CODE_CAMERA = 1001;// 拍照
    public static final int REQUEST_CODE_GALLERY = 1002;// 从相册选择
    public static final int REQUEST_CODE_CROP = 1003;// 裁剪

    public PictureMenu() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        setStyle(PictureMenu.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.picture_menu, container, false);
        rootView.findViewById(R.id.btn_camera).setOnClickListener(this);// 拍照
        rootView.findViewById(R.id.btn_gallery).setOnClickListener(this);// 从相册选择
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(this);// 取消
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BF777777")));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:// 拍照
                try {
                    takePicture();
                } catch (Exception e) {
                    MyUtils.showToast(getActivity(), "相机打开失败");
                }

                break;
            case R.id.btn_gallery:// 相册选择
                selectPhotoFormGallery();
                break;
            case R.id.btn_cancel:// 取消
                if (isVisible() && isCancelable()) {
                    dismiss();
                }
                break;
            default:
                break;
        }
    }

    /**
     * @return void
     * @throws
     * @Title: takePicture
     * @Description: 拍照
     */
    private void takePicture() {
        /**
         * 创建照片
         */
        File takePhotoFile = CameraHelper.getOutputMediaFile(getActivity(), CameraHelper.MEDIA_TYPE_IMAGE);

        mTakePhotoFile = takePhotoFile;
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoFile != null) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(takePhotoFile));
        }
        getActivity().startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);

    }

    /**
     * @return void
     * @throws
     * @Title: selectPhotoFormGallery
     * @Description: 从相册选择
     */
    private void selectPhotoFormGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        getActivity().startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);

    }


    /**
     * @param context
     * @param originalUri
     * @return void
     * @throws
     * @Title: cropPictures
     * @Description: 裁剪图片
     */
    public void cropPictures(Context context, Uri originalUri, String uploadType) {
        File cropPhotoFile = CameraHelper.getOutputMediaFile(context, CameraHelper.MEDIA_TYPE_IMAGE);
        mCropPhotoFile = cropPhotoFile;
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(originalUri, "image/*");
        cropIntent.putExtra("crop", "true");// 可裁减

		    /* 裁剪框比例 */
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
            /* 输出图片的大小 */
        cropIntent.putExtra("outputX", 360);
        cropIntent.putExtra("outputY", 360);


        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("return-data", false);// 设为false，通过图片URI获取
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropPhotoFile));
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 不启用人脸识别
        cropIntent.putExtra("noFaceDetection", false);
        getActivity().startActivityForResult(cropIntent, REQUEST_CODE_CROP);
    }


    /**
     * @return void
     * @throws
     * @Title: dismissDialog
     * @Description: 对话框消失
     */
    public void dismissDialog() {
        if (isVisible() && isCancelable()) {
            dismiss();
        }
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

}
