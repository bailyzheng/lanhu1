package com.zys.jym.lanhu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.App;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.bean.LoginData;
import com.zys.jym.lanhu.bean.User;
import com.zys.jym.lanhu.httpcallback.LoginCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MySharedPrefrencesUtil;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.SPrefUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class SplashActivity extends BaseActivity {
    String TAG = "TAG--SplashActivity";
//    private ImageView iv_splash;
//    private ImageView iv_leaf;
    private App app;
    boolean haveNewApk=false;//是否有新版本
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MyUtils.Loge("bbb","111111111");
                    login();
                    break;
                case 2:
                    MyUtils.Loge("bbb","2222222222");
                    OkHttpUtils.getInstance().cancelTag(this);
                    app.setIsLogin(false);
                    jumpActivity();
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 去除标题  必须在setContentView()方法之前调用
        setContentView(R.layout.ac_splash);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivityUtil.add(this);
        app = getApplicationContext();
        getApkV();//检查更新
        initStartTime();
        initEndTime();
    }

    private void getApkV() {
        //处理haveNewApk
    }

    //
    private void initStartTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void initEndTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(6000);
                    if (!SPrefUtil.getBoolean(SplashActivity.this, "finish", false)) {
                        handler.sendEmptyMessage(2);
                        MyUtils.Loge(TAG, " 发送取消请求--");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
//
    private void login() {
        SPrefUtil.setBoolean(SplashActivity.this, "finish", false);
        final String phone = SPrefUtil.getString(SplashActivity.this, "PHONE", "");
        final String pwd = SPrefUtil.getString(SplashActivity.this, "PWD", "");
        if (!TextUtils.isEmpty(phone)) {
            RequestQueue volleyQueue=Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest=new StringRequest(StringRequest.Method.POST, LHHttpUrl.LOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Gson gson=new Gson();
                    LoginData mData = gson.fromJson(response, LoginData.class);
                    if (mData.errcode == 1) {
                        app.setIsLogin(true);
                        app.setUser(mData.data);
//                        if(app.getPurseData()!=null&&Long.parseLong(app.getPurseData().getViprest()) > 0){
//                            MySharedPrefrencesUtil.setParam(SplashActivity.this, "isAllowAdd", "open");
//                        }
                        SPrefUtil.setString(SplashActivity.this,"TOKEN",mData.getData().getLogin_token());
                        jumpActivity();
                    } else {
                        app.setIsLogin(false);
                        jumpActivity();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    app.setIsLogin(false);
                    MyUtils.showToast(SplashActivity.this,"网络有问题，请检查网络");
//                    jumpActivity();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String ,String>map=new HashMap<>();
                    map.put("phone",phone);
                    map.put("password",pwd);
                    return map;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(10*1000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            volleyQueue.add(stringRequest);
//            OkHttpUtils
//                    .post()
//                    .tag(this)
//                    .url(LHHttpUrl.LOGIN_URL)
//                    .addParams("phone", phone)
//                    .addParams("password", pwd)
//                    .build()
//                    .execute(new LoginCallback() {
//                        @Override
//                        public void onError(Call call, Exception e) {
//                            app.setIsLogin(false);
//                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
//                            jumpActivity();
//                        }
//
//                        @Override
//                        public void onResponse(LoginData mData) {
//                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
//                            if (mData.errcode == 1) {
//                                app.setIsLogin(true);
//                                app.setUser(mData.data);
//                                jumpActivity();
//                            } else {
//                                app.setIsLogin(false);
//                                jumpActivity();
//                            }
//                        }
//                    });
        } else {
            jumpActivity();
        }
    }

    private void jumpActivity() {
        if (SPrefUtil.getBoolean(SplashActivity.this, "finish", false)) {
            return;
        }
        if (haveNewApk) {
            return;
        }
//        if(!app.getIsLogin()){
//            return;
//        }
        SPrefUtil.setBoolean(SplashActivity.this, "finish", true);
        Intent intent =new Intent(SplashActivity.this, Main2Activity.class);
        startActivity(intent);
//        if (app.getIsLogin()) {
//            intent = new Intent(SplashActivity.this, MainActivity.class);
//            startActivity(intent);
//        } else {
//            intent = new Intent(SplashActivity.this, LoginActivity.class);
//            startActivity(intent);
//        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
        ActivityUtil.delect(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
