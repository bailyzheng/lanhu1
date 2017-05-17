package com.zys.jym.lanhu.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.bean.UpHeadImgData;
import com.zys.jym.lanhu.httpcallback.UpHeadImgCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MediaUtil;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.SPrefUtil;

import java.io.File;

import okhttp3.Call;

/**
 * 投诉
 * Created by Administrator on 2016/12/16.
 */

public class ComplaintActivity extends BaseActivity implements View.OnClickListener {
    String TAG = "TAG--ComplaintActivity";
    Toolbar index_toolbar;
    TextView tv_qz, tv_sq, tv_zzyy, tv_csxyy;
    CheckBox cb_qz, cb_sq, cb_zzyy, cb_csxyy;
    EditText et_content;
    ImageView iv_cpic;
    int c = 1;
    boolean sPic = false;
    File cFile = null;
    String cid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_complaint);
        ActivityUtil.add(this);
        initToolBar();
        initViews();
        initData();
    }

    private void initToolBar() {
        index_toolbar = (Toolbar) findViewById(R.id.index_toolbar);
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
        findViewById(R.id.rl_qz).setOnClickListener(this);
        findViewById(R.id.rl_sq).setOnClickListener(this);
        findViewById(R.id.rl_zzyy).setOnClickListener(this);
        findViewById(R.id.rl_csxyy).setOnClickListener(this);
        tv_qz = (TextView) findViewById(R.id.tv_qz);
        tv_sq = (TextView) findViewById(R.id.tv_sq);
        tv_zzyy = (TextView) findViewById(R.id.tv_zzyy);
        tv_csxyy = (TextView) findViewById(R.id.tv_csxyy);
        cb_qz = (CheckBox) findViewById(R.id.cb_qz);
        cb_sq = (CheckBox) findViewById(R.id.cb_sq);
        cb_zzyy = (CheckBox) findViewById(R.id.cb_zzyy);
        cb_csxyy = (CheckBox) findViewById(R.id.cb_csxyy);
        et_content = (EditText) findViewById(R.id.et_content);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        iv_cpic = (ImageView) findViewById(R.id.iv_cpic);
        iv_cpic.setOnClickListener(this);
    }


    private void initData() {
        //得到传过来的群聊id
        cid = getIntent().getStringExtra("cid");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_qz:
                if (cb_qz.isChecked()) {
                    c = 0;
                    tv_qz.setTextColor(getResources().getColor(R.color.black));
                    cb_qz.setChecked(false);
                } else {
                    c = 1;
                    tv_qz.setTextColor(getResources().getColor(R.color.main_color));
                    tv_sq.setTextColor(getResources().getColor(R.color.black));
                    tv_zzyy.setTextColor(getResources().getColor(R.color.black));
                    tv_csxyy.setTextColor(getResources().getColor(R.color.black));
                    cb_qz.setChecked(true);
                    cb_sq.setChecked(false);
                    cb_zzyy.setChecked(false);
                    cb_csxyy.setChecked(false);
                }

                break;
            case R.id.rl_sq:
                if (cb_sq.isChecked()) {
                    c = 0;
                    tv_sq.setTextColor(getResources().getColor(R.color.black));
                    cb_sq.setChecked(false);
                } else {
                    c = 2;
                    tv_sq.setTextColor(getResources().getColor(R.color.main_color));
                    tv_qz.setTextColor(getResources().getColor(R.color.black));
                    tv_zzyy.setTextColor(getResources().getColor(R.color.black));
                    tv_csxyy.setTextColor(getResources().getColor(R.color.black));
                    cb_sq.setChecked(true);
                    cb_qz.setChecked(false);
                    cb_zzyy.setChecked(false);
                    cb_csxyy.setChecked(false);
                }

                break;
            case R.id.rl_zzyy:
                if (cb_zzyy.isChecked()) {
                    c = 0;
                    tv_zzyy.setTextColor(getResources().getColor(R.color.black));
                    cb_zzyy.setChecked(false);
                } else {
                    c = 3;
                    tv_zzyy.setTextColor(getResources().getColor(R.color.main_color));
                    tv_sq.setTextColor(getResources().getColor(R.color.black));
                    tv_qz.setTextColor(getResources().getColor(R.color.black));
                    tv_csxyy.setTextColor(getResources().getColor(R.color.black));
                    cb_zzyy.setChecked(true);
                    cb_sq.setChecked(false);
                    cb_qz.setChecked(false);
                    cb_csxyy.setChecked(false);
                }

                break;
            case R.id.rl_csxyy:
                if (cb_csxyy.isChecked()) {
                    c = 0;
                    tv_csxyy.setTextColor(getResources().getColor(R.color.black));
                    cb_csxyy.setChecked(false);
                } else {
                    c = 4;
                    tv_csxyy.setTextColor(getResources().getColor(R.color.main_color));
                    tv_sq.setTextColor(getResources().getColor(R.color.black));
                    tv_zzyy.setTextColor(getResources().getColor(R.color.black));
                    tv_qz.setTextColor(getResources().getColor(R.color.black));
                    cb_csxyy.setChecked(true);
                    cb_sq.setChecked(false);
                    cb_zzyy.setChecked(false);
                    cb_qz.setChecked(false);
                }
                break;

            case R.id.iv_cpic:
                MediaUtil.doPickPhotoAction(ComplaintActivity.this);
                break;
            case R.id.btn_submit:
                if (c == 0) {
                    MyUtils.showToast(ComplaintActivity.this, "请选择投诉类型");
                    return;
                }
                if (TextUtils.isEmpty(et_content.getText().toString().trim())) {
                    MyUtils.showToast(ComplaintActivity.this, "请输入投诉内容");
                    return;
                }
                if (sPic) {
                    postFData();
                } else {
                    postNData();
                }
                break;

        }
    }


    private void postFData() {
        MyUtils.showDialog(ComplaintActivity.this, "正在提交...");
        MyUtils.Loge(TAG, "cataId=" + c + ",content=" + et_content.getText().toString().trim() + ",topicId=" + cid);

//        if (getApplicationContext() != null && getApplicationContext().getUser() != null && !TextUtils.isEmpty(getApplicationContext().getUser().getLogin_token())) {
        OkHttpUtils
                .post()
                .tag(this)
                .url(LHHttpUrl.ADDCOMPLAIN_URL)
                .addFile("pic", "cpic.png", cFile)
                .addParams("cataId", c + "")
//                    .addParams("login_token", getApplicationContext().getUser().getLogin_token())
                .addParams("login_token", SPrefUtil.getString(ComplaintActivity.this, "TOKEN", ""))
                .addParams("content", et_content.getText().toString().trim())
                .addParams("topicId", cid)//id 传值过来
                .build()
                .execute(new UpHeadImgCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                        MyUtils.showToast(ComplaintActivity.this, "服务器出现问题，请稍后再试");
                    }

                    @Override
                    public void onResponse(UpHeadImgData mData) {
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                        MyUtils.showToast(ComplaintActivity.this, mData.errmsg);
                        if (mData.errcode == 40001) {
                            ActivityUtil.exitAll();
                            ActivityUtil.toLogin(ComplaintActivity.this);
                            return;
                        }
                        if (mData.errcode == 1) {
                            finish();
                        }
                    }
                });
//        }
    }

    private void postNData() {
        MyUtils.showDialog(ComplaintActivity.this, "正在提交...");
//        MyUtils.Loge(TAG,"cataId="+c+",content="+et_content.getText().toString().trim()+",topicId="+cid+",login_token="+getApplicationContext().getUser().getLogin_token());
//        if(getApplicationContext()!=null&&getApplicationContext().getUser()!=null&&!TextUtils.isEmpty(getApplicationContext().getUser().getLogin_token())) {
        OkHttpUtils
                .post()
                .tag(this)
                .url(LHHttpUrl.ADDCOMPLAIN_URL)
                .addParams("cataId", c + "")
//                    .addParams("login_token", getApplicationContext().getUser().getLogin_token())
                .addParams("login_token", SPrefUtil.getString(ComplaintActivity.this, "TOKEN", ""))
                .addParams("content", et_content.getText().toString().trim())
                .addParams("topicId", cid)//id 传值过来
                .build()
                .execute(new UpHeadImgCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                        MyUtils.showToast(ComplaintActivity.this, "服务器出现问题，请稍后再试");
                    }

                    @Override
                    public void onResponse(UpHeadImgData mData) {
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                        MyUtils.showToast(ComplaintActivity.this, mData.errmsg);
                        if (mData.errcode == 40001) {
                            ActivityUtil.exitAll();
                            ActivityUtil.toLogin(ComplaintActivity.this);
                            return;
                        }
                        if (mData.errcode == 1) {
                            finish();
                        }
                    }
                });
//        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case MediaUtil.SELECT_PHOTO_CODE:// 相册
                case MediaUtil.CAMERA_REQUEST_CODE:// 相机
                    try {
                        MyUtils.Loge(TAG, "FilePath=" + MediaUtil.getRealFilePath(
                                ComplaintActivity.this, data.getData()));
                        cFile = new File(MediaUtil.getRealFilePath(
                                ComplaintActivity.this, data.getData()));
                        sPic = true;
                        iv_cpic.setImageURI(data.getData());
                    } catch (Exception e) {
                        MyUtils.Loge(TAG, "图片选择失败：" + e);
                        MyUtils.showToast(ComplaintActivity.this, "图片选择失败");
                        sPic = false;
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(this);
    }


}
