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
import com.zys.jym.lanhu.fragment.impl.MineFragment;
import com.zys.jym.lanhu.httpcallback.LoginCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/12/14.
 */

public class ModifyNickNameActivity extends BaseActivity implements View.OnClickListener {
    String TAG="TAG--ModifyNickNameActivity";
    Toolbar index_toolbar;
    EditText et_nickname;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_modifynickname);
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
        et_nickname= (EditText) findViewById(R.id.et_nickname);
        findViewById(R.id.btn_post).setOnClickListener(this);
    }

    private void initData() {
        if (!TextUtils.isEmpty(getApplicationContext().getUser().getNickname())){
            et_nickname.setText(getApplicationContext().getUser().getNickname());
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_post:
                if (TextUtils.isEmpty(et_nickname.getText().toString().trim())){
                    MyUtils.showToast(ModifyNickNameActivity.this,"请输入昵称");
                    return;
                }
                if(!MyUtils.isNickName(et_nickname.getText().toString().trim())){
                    MyUtils.showToast(ModifyNickNameActivity.this,"昵称格式不正确");
                    return;
                }
                postData();
                break;
        }
    }

    private void postData() {
        MyUtils.showDialog(ModifyNickNameActivity.this,"正在修改...");
        if(getApplicationContext()!=null&&getApplicationContext().getUser()!=null&&!TextUtils.isEmpty(getApplicationContext().getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .tag(this)
                    .url(LHHttpUrl.MODIFYINFO_URL)
                    .addParams("login_token", getApplicationContext().getUser().getLogin_token())
                    .addParams("type", "1")
                    .addParams("info", et_nickname.getText().toString().trim())
                    .build()
                    .execute(new LoginCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                            MyUtils.showToast(ModifyNickNameActivity.this, "连接服务器失败，请稍后再试");
                        }

                        @Override
                        public void onResponse(LoginData mData) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                            MyUtils.showToast(ModifyNickNameActivity.this, mData.errmsg);
                            if (mData.errcode == 1) {
                                getApplicationContext().getUser().setNickname(et_nickname.getText().toString().trim());
                                MineFragment.handler.sendEmptyMessage(0x1);
                            }
                        }
                    });
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(this);
    }


}
