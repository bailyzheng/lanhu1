package com.zys.jym.lanhu.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.bean.UpHeadImgData;
import com.zys.jym.lanhu.fragment.impl.MineFragment;
import com.zys.jym.lanhu.httpcallback.UpHeadImgCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MediaUtil;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.view.CircleImageView;

import java.io.File;

import okhttp3.Call;

/**
 * 修改资料
 * Created by Administrator on 2016/12/14.
 */

public class ModifyDataActivity extends BaseActivity implements View.OnClickListener {
    String TAG="TAG--ModifyDataActivity";
    Toolbar index_toolbar;
    CircleImageView iv_head;
    boolean setting;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_modifydata);
        ActivityUtil.add(this);
        initToolBar();
        initViews();
        initData();
    }

    private void initToolBar() {
        index_toolbar= (Toolbar) findViewById(R.id.index_toolbar);
        index_toolbar.setTitle("");
        index_toolbar.setTitleTextColor(Color.WHITE);
        index_toolbar.setNavigationIcon(R.mipmap.backimg);
        setSupportActionBar(index_toolbar);
        index_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initViews() {
        findViewById(R.id.rl_modifydata).setOnClickListener(this);
        findViewById(R.id.rl_nickname).setOnClickListener(this);
        findViewById(R.id.rl_describe).setOnClickListener(this);
        findViewById(R.id.rl_pwd).setOnClickListener(this);
        iv_head= (CircleImageView) findViewById(R.id.iv_head);
    }

    private void initData() {
        setting=getIntent().getBooleanExtra("setting",false);
        if (!TextUtils.isEmpty(getApplicationContext().getUser().getHeadurl())){
            Picasso.with(ModifyDataActivity.this)
                    .load(LHHttpUrl.IMG_URL+getApplicationContext().getUser().getHeadurl())
                    .into(iv_head);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_nickname:
                Intent in =new Intent(ModifyDataActivity.this,ModifyNickNameActivity.class);
                startActivity(in);
                break;
            case R.id.rl_describe:
                Intent in1 =new Intent(ModifyDataActivity.this,ModifyDescribeActivity.class);
                startActivity(in1);
                break;
            case R.id.rl_pwd:
                Intent in3 =new Intent(ModifyDataActivity.this,FindPwdActivity.class);
                in3.putExtra("isXG",true);
                startActivity(in3);
                break;
            case R.id.rl_modifydata:
                MediaUtil.doPickPhotoAction(ModifyDataActivity.this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            switch (requestCode){
                case MediaUtil.SELECT_PHOTO_CODE:// 相册
                case MediaUtil.CAMERA_REQUEST_CODE:// 相机
                    MyUtils.Loge(TAG,"data111="+data.toString());
                    MediaUtil.doCropPhoto(ModifyDataActivity.this,data);
                    break;
                case MediaUtil.PHOTO_CROP:// 剪裁
//                    iv_head.setImageURI(data.getData());
//                    SettingActivity.setHeadImg(data);
//                    MyUtils.Loge(TAG,"data2="+data.toString());
                    try {
                        MyUtils.Loge(TAG,"FilePath="+MediaUtil.getRealFilePath(
                                ModifyDataActivity.this, data.getData()));
                        File iconFile = new File(MediaUtil.getRealFilePath(
                                ModifyDataActivity.this, data.getData()));
                        postIcon(iconFile,data);
                    } catch (Exception e) {
                        MyUtils.Loge(TAG, "上传头像异常：" + e);
                    }
                    break;
            }
        }
    }

    private void postIcon(File iconFile, final Intent data) {
        MyUtils.showDialog(ModifyDataActivity.this, "正在上传头像...");
        if(getApplicationContext()!=null&&getApplicationContext().getUser()!=null&&!TextUtils.isEmpty(getApplicationContext().getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .tag(this)
                    .url(LHHttpUrl.MODIFYHEADURL_URL)
                    .addFile("pic", "photo.png", iconFile)
                    .addParams("login_token", getApplicationContext().getUser().getLogin_token())
                    .build()
                    .execute(new UpHeadImgCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                            MyUtils.showToast(ModifyDataActivity.this, "服务器出现问题，请稍后再试");
                        }

                        @Override
                        public void onResponse(UpHeadImgData mData) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                            MyUtils.showToast(ModifyDataActivity.this, mData.errmsg);
                            if (mData.errcode == 1) {
                                getApplicationContext().getUser().setHeadurl(mData.getData().getHeadurl());
                                iv_head.setImageURI(data.getData());
                                if (setting) {
                                    SettingActivity.setHeadImg(data);
                                    MineFragment.setHeadImg(data);
                                } else {
                                    MineFragment.setHeadImg(data);
                                }

                            }
                        }
                    });
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 0x11:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    MediaUtil.doTakePhoto(this);
                } else {
                    // Permission Denied
                }
                break;
        }
        if (requestCode == 0x13) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                MediaUtil.doPickPhotoFromGallery(this);
            } else {
                // Permission Denied
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(this);
    }
}
