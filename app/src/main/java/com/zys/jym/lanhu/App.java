package com.zys.jym.lanhu;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.bean.NewApkData;
import com.zys.jym.lanhu.bean.PurseData;
import com.zys.jym.lanhu.bean.User;
import com.zys.jym.lanhu.receiver.NetWorkChangeReceiver;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.SPrefUtil;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/12/14.
 */

public class App extends Application {
    String TAG="TAG--App";
    private boolean isLogin = false;
    private User user = null;
    private PurseData purseData=null;
    private int payActivity=0;//0:充值狐币页面，1：兑换置顶页面，2：开通VIP页面
    private String updateUrl="";
    private NewApkData mNewApkData;
    private boolean b_location=true;

    public boolean isB_location() {
        return b_location;
    }

    public void setB_location(boolean b_location) {
        this.b_location = b_location;
    }

    public NewApkData getmNewApkData() {
        return mNewApkData;
    }

    public void setmNewApkData(NewApkData mNewApkData) {
        this.mNewApkData = mNewApkData;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean login) {
        isLogin = login;
    }

    public PurseData getPurseData() {
        return purseData;
    }

    public void setPurseData(PurseData purseData) {
        this.purseData = purseData;
    }

    public int getPayActivity() {
        return payActivity;
    }

    public void setPayActivity(int payActivity) {
        this.payActivity = payActivity;
    }

    {
        PlatformConfig.setWeixin("wx8176733770c70258", "3167f6e294086657f18080ff154c7c31");//3167f6e294086657f18080ff154c7c31
    }
    @Override
    public void onCreate() {
        super.onCreate();
        initOkHttp();
        initJpush();
        initUMShare();
        initNetReceiver();
        CrashReport.initCrashReport(getApplicationContext(), "9557e1699b", false);
    }



    private void initNetReceiver() {
        NetWorkChangeReceiver.registerReceiver(this);
    }

    private void initOkHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG--lanhu"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    private void initJpush() {
        if (SPrefUtil.getBoolean(this,"PUSH",true)){
            MyUtils.Loge(TAG,"initJpush");
            JPushInterface.setDebugMode(false);
            JPushInterface.init(this);
        }
    }

    private void initUMShare() {
        PlatformConfig.setQQZone("1105855575", "Ph3pQlYxpe1bqr3q");
        Config.isJumptoAppStore = true;
        UMShareAPI.get(this);
    }


}
