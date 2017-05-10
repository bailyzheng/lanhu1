package com.zys.jym.lanhu.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.App;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.bean.LoginData;
import com.zys.jym.lanhu.httpcallback.LoginCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.SPrefUtil;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/12/14.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    String TAG="TAG--LoginActivity";
    private Button btn_login;
    private TextView tv_register,tv_findpwd;
    private EditText et_phone,et_pwd;
    private App app;
    public static LoginActivity la;
    AlertDialog dialog;
    boolean logout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
        setContentView(R.layout.ac_login);
        la=this;
        ActivityUtil.add(la);
        initViews();
        initData();
    }

    private void initViews() {
        btn_login= (Button) findViewById(R.id.btn_login);
        tv_register= (TextView) findViewById(R.id.tv_register);
        tv_findpwd= (TextView) findViewById(R.id.tv_findpwd);
        et_phone= (EditText) findViewById(R.id.et_phone);
        et_pwd=(EditText) findViewById(R.id.et_pwd);
        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_findpwd.setOnClickListener(this);
    }

    private void initData() {
        app= getApplicationContext();
        logout= getIntent().getBooleanExtra("logout",false);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                if (TextUtils.isEmpty(et_phone.getText().toString().trim())){
                    MyUtils.showToast(LoginActivity.this,"请输入手机号");
                    return;
                }
                if(!MyUtils.isPhoneNumber(et_phone.getText().toString().trim())){
                    MyUtils.showToast(LoginActivity.this,"手机号码格式不正确");
                    return;
                }
                if (TextUtils.isEmpty(et_pwd.getText().toString().trim())){
                    MyUtils.showToast(LoginActivity.this,"请输入密码");
                    return;
                }
                if(!MyUtils.isPassword(et_pwd.getText().toString().trim())){
                    MyUtils.showToast(LoginActivity.this,"密码格式不正确");
                    return;
                }
                login();

            break;
            case R.id.tv_register:
                Intent in1 =new Intent(LoginActivity.this,RegisterActivity.class);
                in1.putExtra("logout",logout);
                startActivity(in1);
                break;
            case R.id.tv_findpwd:
                Intent in2 =new Intent(LoginActivity.this,FindPwdActivity.class);
                startActivity(in2);
                break;
        }
    }




    private void login() {
        MyUtils.showDialog(LoginActivity.this,"登录中...");
        OkHttpUtils
                .post()
                .tag(this)
                .url(LHHttpUrl.LOGIN_URL)
                .addParams("phone", et_phone.getText().toString())
                .addParams("password",et_pwd.getText().toString())
                .build()
                .execute(new LoginCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                        MyUtils.showToast(LoginActivity.this, "连接服务器失败，请稍后再试");
                    }

                    @Override
                    public void onResponse(LoginData mData) {
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                        if (mData.errcode==1){
//                            MyUtils.showToast(LoginActivity.this,"登陆成功");
                            SPrefUtil.setString(LoginActivity.this, "PHONE", et_phone.getText().toString().trim());
                            SPrefUtil.setString(LoginActivity.this,"PWD", et_pwd.getText().toString().trim());
                            app.setIsLogin(true);
                            app.setUser(mData.data);
                            if (logout){
                                Intent in=new Intent(LoginActivity.this,Main2Activity.class);
                                startActivity(in);
                            }
                            finish();
                        }else {
                            MyUtils.showToast(LoginActivity.this,mData.errmsg);
                            MyUtils.Loge(TAG,"登陆失败info="+mData.errmsg);
                        }
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(la);
    }
}
