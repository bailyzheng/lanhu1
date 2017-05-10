package com.zys.jym.lanhu;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.umeng.socialize.utils.ContextUtil.getContext;

/**
 * Created by Administrator on 2016/12/14.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //获取状态栏高度
    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = getContext().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 获取手机屏幕的宽度和高度
     *
     * @return
     */
    public Map<String, Integer> getWidthHeight(Context context) {
        Map<String, Integer> whMap = new HashMap<String, Integer>();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;// 屏幕宽度（像素）
        int height = metrics.heightPixels;// 屏幕高度（像素）
//		float density = metrics.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
//      int densityDpi = metrics.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        whMap.put("width", width);
        whMap.put("height", height);
//        whMap.put("width", px2dip(context,width));
//        whMap.put("height", px2dip(context,height));
        //System.out.println("屏幕的宽度=================="+width+",height="+height);
        //System.out.println("屏幕的宽度=================="+px2dip(context,width)+",height dp="+px2dip(context,height));
        return whMap;
    }

    private String getRunningActivityName() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }

    /**
     * 重写getApplicationContext方法，获取到我们所需要的Application对象
     */
    @Override
    public App getApplicationContext() {
        return (App)super.getApplicationContext();
    }

    //获取当前app版本code
    public int getAppVersion() {
        int localVersion=1;
        String localVersionName="1.0";
        try {
            PackageInfo packageInfo = getApplicationContext()
                    .getPackageManager().getPackageInfo(getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localVersion;
    }
    //获取当前app版本号
    public String getAppVersionName() {
        String localVersionName="1.0";
        try {
            PackageInfo packageInfo = getApplicationContext()
                    .getPackageManager().getPackageInfo(getPackageName(), 0);
            localVersionName = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localVersionName;
    }
}
