package com.zys.jym.lanhu.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.bean.SMSData;
import com.zys.jym.lanhu.httpcallback.SMSCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;

import okhttp3.Call;


/**
 * Created by Administrator on 2016/12/15.
 */

public class FindPwdActivity extends BaseActivity implements View.OnClickListener {
    String TAG="TAG--FindPwdActivity";
    Toolbar index_toolbar;
    EditText et_phone;
    TextView tv_title;
    boolean isXG=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.ac_findpwd);
        ActivityUtil.add(this);
        initToolBar();
        initViews();
        initData();
    }




    private void initViews() {
        findViewById(R.id.btn_next).setOnClickListener(this);
        et_phone= (EditText) findViewById(R.id.et_phone);
        tv_title= (TextView) findViewById(R.id.tv_title);
    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        index_toolbar= (Toolbar) findViewById(R.id.index_toolbar);
        index_toolbar.setTitle("");
        index_toolbar.setTitleTextColor(Color.WHITE);
        index_toolbar.setNavigationIcon(R.mipmap.icon_back);
//        index_toolbar.inflateMenu(R.menu.menu_login);
        setSupportActionBar(index_toolbar);
        index_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void initData() {
       isXG  =getIntent().getBooleanExtra("isXG",false);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next:
                if (TextUtils.isEmpty(et_phone.getText().toString().trim())){
                    MyUtils.showToast(FindPwdActivity.this,"请输入手机号");
                    return;
                }
                if(!MyUtils.isPhoneNumber(et_phone.getText().toString().trim())){
                    MyUtils.showToast(FindPwdActivity.this,"手机号码格式不正确");
                    return;
                }
                if (isXG){
                    if (!TextUtils.equals(et_phone.getText().toString().trim(), getApplicationContext().getUser().getPhone())){
                        MyUtils.showToast(FindPwdActivity.this,"手机号不是此账户登录账号");
                        return;
                    }
                }
                postData();
                break;
        }
    }

    private void postData() {
        MyUtils.showDialog(FindPwdActivity.this,"发送中...");
        OkHttpUtils
                .post()
                .tag(this)
                .url(LHHttpUrl.SMSCODE_URL)
                .addParams("phone", et_phone.getText().toString())
                .addParams("type", "2")
                .build()
                .execute(new SMSCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                        MyUtils.showToast(FindPwdActivity.this, "连接服务器失败，请稍后再试");
                    }

                    @Override
                    public void onResponse(SMSData mData) {
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                        if (mData.errcode==1){
                            Intent in=new Intent(FindPwdActivity.this,FindPwd2Activity.class);
                            in.putExtra("phone",et_phone.getText().toString());
                            in.putExtra("token",mData.data.getSms_token());
                            startActivity(in);
                            finish();
                        }else {
                            MyUtils.showToast(FindPwdActivity.this,mData.errmsg);
                            MyUtils.Loge(TAG,"失败info="+mData.errmsg);
                        }
                    }
                });

    }







    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(this);
    }
}
