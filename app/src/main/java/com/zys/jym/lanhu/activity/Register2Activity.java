package com.zys.jym.lanhu.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.bean.RegisterData;
import com.zys.jym.lanhu.httpcallback.RegisterCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.GetVerificationUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/12/14.
 */

public class Register2Activity extends BaseActivity implements View.OnClickListener {
    String TAG ="TAG--Register2Activity";
    Toolbar index_toolbar;
    String phone,token;
    TextView tv_phone,tv_get_authCode;
    EditText et_authCode,et_pwd,et_yqmcode;
    CheckBox cb_agree;
    boolean logout;
    String yqmcode="";
    private static int countdown = 60;//获取验证码倒计时
    Handler mhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x200:
                    tv_get_authCode.setText("重新发送(" + countdown + ")");
                    break;
                case 0x201:
                    countdown = 60;
                    tv_get_authCode.setText("再次获取");
                    tv_get_authCode.setEnabled(true);
                    break;
            }
        }
    };
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
        setContentView(R.layout.ac_register2);
        ActivityUtil.add(this);
        initToolBar();
        initViews();
        initData();
    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        index_toolbar= (Toolbar) findViewById(R.id.index_toolbar);
        index_toolbar.setTitle("");
        index_toolbar.setTitleTextColor(Color.WHITE);
        index_toolbar.setNavigationIcon(R.mipmap.icon_back);
        setSupportActionBar(index_toolbar);
        index_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void initViews() {
        tv_get_authCode= (TextView) findViewById(R.id.tv_get_authCode);
        findViewById(R.id.btn_register).setOnClickListener(this);
        tv_get_authCode.setOnClickListener(this);
        tv_phone= (TextView) findViewById(R.id.tv_phone);
        et_authCode= (EditText) findViewById(R.id.et_authCode);
        et_pwd= (EditText) findViewById(R.id.et_pwd);
        cb_agree= (CheckBox) findViewById(R.id.cb_agree);
        findViewById(R.id.tv_bc_agreement).setOnClickListener(this);
        et_yqmcode= (EditText) findViewById(R.id.et_yqmcode);
    }
    private void initData() {
        phone=getIntent().getStringExtra("phone");
        token=getIntent().getStringExtra("token");
        logout =getIntent().getBooleanExtra("logout",false);
        if (!TextUtils.isEmpty(phone)){
            tv_phone.setText(phone);
            tv_get_authCode.setEnabled(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (; countdown > 0; countdown--) {
                        mhandler.sendEmptyMessage(0x200);
                        if (countdown <= 0) {
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mhandler.sendEmptyMessage(0x201);

                }
            }).start();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:
                if (TextUtils.isEmpty(et_authCode.getText().toString().trim())){
                    MyUtils.showToast(Register2Activity.this,"请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(et_pwd.getText().toString().trim())){
                    MyUtils.showToast(Register2Activity.this,"请输入密码");
                    return;
                }
                if(!MyUtils.isPassword(et_pwd.getText().toString().trim())){
                    MyUtils.showToast(Register2Activity.this,"密码格式不正确");
                    return;
                }
                if (!cb_agree.isChecked()){
                    MyUtils.showToast(Register2Activity.this,"请同意服务条款后再使用");
                    return;
                }
                if (!TextUtils.isEmpty(et_yqmcode.getText().toString().trim())){
                    yqmcode= et_yqmcode.getText().toString().trim();
                }else {
                    yqmcode="";
                }
                register();
                break;
            case R.id.tv_get_authCode:
                GetVerificationUtil.getverification(Register2Activity.this,tv_get_authCode,phone,1);
                break;
            case R.id.tv_bc_agreement:
                Intent in3 =new Intent(Register2Activity.this,AgreementActivity.class);
                startActivity(in3);
                break;
        }
    }
    
    public  void register(){
        MyUtils.showDialog(Register2Activity.this,"注册中...");
        OkHttpUtils
                .post()
                .tag(this)
                .url(LHHttpUrl.REG_URL)
                .addParams("phone", phone)
                .addParams("smscode",et_authCode.getText().toString())
                .addParams("password",et_pwd.getText().toString())
                .addParams("sms_token",token)
                .addParams("regcode",yqmcode)//邀请码
                .build()
                .execute(new RegisterCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                        MyUtils.showToast(Register2Activity.this, "连接服务器失败，请稍后再试");
                    }

                    @Override
                    public void onResponse(RegisterData mData) {
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                        if(mData.errcode==40001){
                            ActivityUtil.exitAll();
                            ActivityUtil.toLogin(Register2Activity.this);
                            return;
                        }
                        if (mData.errcode==1){
                            MyUtils.showToast(Register2Activity.this,"注册成功!");
                            getApplicationContext().setUser(mData.data);
                            getApplicationContext().setIsLogin(true);
                            if (logout){
                                Intent in =new Intent(Register2Activity.this,MainActivity.class);
                                startActivity(in);
                            }
                            LoginActivity.la.finish();
                            finish();
                        }else {
                            MyUtils.showToast(Register2Activity.this,mData.errmsg);
                            MyUtils.Loge(TAG,"注册失败info="+mData.errmsg);
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
