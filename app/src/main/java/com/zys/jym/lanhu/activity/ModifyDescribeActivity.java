package com.zys.jym.lanhu.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.bean.LoginData;
import com.zys.jym.lanhu.httpcallback.LoginCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;

import okhttp3.Call;

/**
 * 修改描述
 * Created by Administrator on 2016/12/14.
 */

public class ModifyDescribeActivity extends BaseActivity implements View.OnClickListener {
    String TAG="TAG--ModifyDescribeActivity";
    Toolbar index_toolbar;
    EditText et_describe;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_modifydescribe);
        ActivityUtil.add(this);
        initViews();
        initToolBar();
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
        et_describe= (EditText) findViewById(R.id.et_describe);
        findViewById(R.id.btn_upda).setOnClickListener(this);
    }

    private void initData() {
        if (!TextUtils.isEmpty(getApplicationContext().getUser().getDescribe())){
            et_describe.setText(getApplicationContext().getUser().getDescribe());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_upda:
                if (TextUtils.isEmpty(et_describe.getText().toString().trim())){
                    MyUtils.showToast(ModifyDescribeActivity.this,"请输入个人描述");
                    return;
                }
                postData();
                break;
        }
    }

    private void postData() {
        MyUtils.showDialog(ModifyDescribeActivity.this, "正在修改...");
        if(getApplicationContext()!=null&&getApplicationContext().getUser()!=null&&!TextUtils.isEmpty(getApplicationContext().getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .tag(this)
                    .url(LHHttpUrl.MODIFYINFO_URL)
                    .addParams("login_token", getApplicationContext().getUser().getLogin_token())
                    .addParams("type", "2")
                    .addParams("info", et_describe.getText().toString().trim())
                    .build()
                    .execute(new LoginCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                            MyUtils.showToast(ModifyDescribeActivity.this, "连接服务器失败，请稍后再试");
                        }

                        @Override
                        public void onResponse(LoginData mData) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                            MyUtils.showToast(ModifyDescribeActivity.this, mData.errmsg);
                            if (mData.errcode == 1) {
//                            finish();
                                getApplicationContext().getUser().setDescribe(et_describe.getText().toString().trim());
                            }
                        }
                    });
        }

    }
        @Override
        protected void onDestroy () {
            super.onDestroy();
            ActivityUtil.delect(this);
        }
    }
