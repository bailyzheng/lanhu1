package com.zys.jym.lanhu.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.App;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.pager.GroupPager;
import com.zys.jym.lanhu.activity.pager.MinePager;
import com.zys.jym.lanhu.activity.pager.ReleasePager;
import com.zys.jym.lanhu.activity.pager.TopPager;
import com.zys.jym.lanhu.bean.GetPurseData;
import com.zys.jym.lanhu.bean.UpdateApkData;
import com.zys.jym.lanhu.fragment.impl.MainFragment;
import com.zys.jym.lanhu.httpcallback.GetPurseCallback;
import com.zys.jym.lanhu.httpcallback.UpdateApkCallback;
import com.zys.jym.lanhu.receiver.NetWorkChangeReceiver;
import com.zys.jym.lanhu.service.UpdateService;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.DialogOkUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MediaUtil;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.SPrefUtil;

import okhttp3.Call;

import static com.zys.jym.lanhu.fragment.BaseFragment.mContext;

public class MainActivity extends BaseActivity {
    static String TAG="TAG--MainActivity";
    private static App app;
    private static final String FRAGMENT_CONTENT_MAIN = "fragment_content_main";
    private long exitTime = 0;
    public static MainActivity ma;
    public static Intent mdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }
        setContentView(R.layout.activity_main);
        ma = this;
        MyUtils.Loge("AAAAAA"+TAG, ma.toString());
        ActivityUtil.add(ma);
        app = getApplicationContext();
        initFragment();
        getPurseData(false);
        getApkVersion();
    }



    /**
     * 初始化fragment,将frament布局填充给布局文件
     */
    public void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_main, new MainFragment(), FRAGMENT_CONTENT_MAIN);
        ft.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                MyUtils.showToast(ma, "再按一次退出程序");
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                ActivityUtil.exitAll();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getApkVersion() {
        OkHttpUtils
                .post()
                .url(LHHttpUrl.GETVERSION_URL)
                .addParams("type",1+"")
                .build()
                .execute(new UpdateApkCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                    }

                    @Override
                    public void onResponse(UpdateApkData mData) {
                        MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                        if(mData.getErrcode()==40001){
                            ActivityUtil.exitAll();
                            ActivityUtil.toLogin(MainActivity.this);
                            return;
                        }
                        if (mData.getErrcode()==1){
                        // 先判断是否有新版本 有弹提示框
                            if (!TextUtils.equals(mData.getData().getVersion(),getAppVersionName())){
                                app.setmNewApkData(mData.getData());
                                String msg="";
                                if(TextUtils.isEmpty(mData.getData().getMsg())){
                                    msg="有新版本啦";
                                }else {
                                    msg= MyUtils.Replace_hh_Str(mData.getData().getMsg());
                                }
                                DialogOkUtil.show_update_Dialog(MainActivity.this,mData.getData().getVersion(), msg, new DialogOkUtil.On_Update_ClickListener() {
                                    @Override
                                    public void onCancle() {

                                    }

                                    @Override
                                    public void onDownload() {
                                        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(MainActivity.this,
                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    0x14);
                                            return;
                                        }
                                        downLoad();
                                    }
                                }).show();
                            }

                        }
                    }
                });
    }
    private void downLoad() {
        if (NetWorkChangeReceiver.NET_WORK_WIFI_TYPE){
            startService(new Intent(ma,UpdateService.class));
        }else if (!NetWorkChangeReceiver.NET_WORK_TYPE){
            DialogOkUtil.show_Ok_Dialog(ma, "请打开网络连接", new DialogOkUtil.On_OK_ClickListener() {
                @Override
                public void onOk() {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);startActivity(intent);
                }
            }).show();
        }else {
            DialogOkUtil.show_OK_NO_Dialog(ma, "当前网络不是Wifi，是否用手机流量下载？", new DialogOkUtil.On_OK_N0_ClickListener() {
                @Override
                public void onOk() {
                    startService(new Intent(ma, UpdateService.class));
                }

                @Override
                public void onNo() {

                }
            }).show();
        }

    }
    /**
     * 获取账户余额信息
     */
    public static void getPurseData(final boolean dhzd) {
        if (!app.getIsLogin()){
            return;
        }
//        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .url(LHHttpUrl.FLUSH_URL)
//                    .addParams("login_token", app.getUser().getLogin_token())
                    .addParams("login_token", SPrefUtil.getString(ma,"TOKEN",""))
                    .build()
                    .execute(new GetPurseCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                        }

                        @Override
                        public void onResponse(GetPurseData mData) {
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
//                            if(mData.getErrcode()==40001){
//                                ActivityUtil.exitAll();
//                                ActivityUtil.toLogin(MainActivity.this);
//                                return;
//                            }
                            if (mData.getErrcode() == 1) {
                                MyUtils.Loge(TAG, "user=" + mData.getUserDate() + "，账户信息=" + mData.getUserInfo());
                                app.setPurseData(mData.getUserDate());
                                app.getUser().setViptime(mData.getUserInfo().getViptime());
                                app.getPurseData().setViprest(mData.getUserDate().getViprest());
                                MinePager.setPData(mData.getUserDate());
                                if (dhzd) {
                                    ZDActivity.setZDNum();
                                }
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
                    MediaUtil.doCropPhotore(this,data);
                    ReleasePager.setImg(data);
                    mdata=data;

                    break;
                case MediaUtil.PHOTO_CROP:// 剪裁
                    ReleasePager.setImg(data);
                    mdata=data;
                    break;
            }
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
            case 0x13:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    MediaUtil.doPickPhotoFromGallery(this);
                } else {
                    // Permission Denied
                }
                break;
            case 0x14:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    downLoad();
                } else {
                    MyUtils.showToast(ma,"权限获取失败，取消下载");
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    protected void onDestroy() {
        GroupPager.mAdapter=null;
        GroupPager.mTopDataList.clear();
        try{
            TopPager.mAdapter=null;
            TopPager.mTopDataList.clear();
        }catch (Exception e){

        }
        super.onDestroy();
        ActivityUtil.delect(ma);
    }
}
