package com.zys.jym.lanhu.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.bean.UpdateApkData;
import com.zys.jym.lanhu.httpcallback.UpdateApkCallback;
import com.zys.jym.lanhu.receiver.NetWorkChangeReceiver;
import com.zys.jym.lanhu.service.UpdateService;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.DialogOkUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/12/14.
 */

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    String TAG = "TAG--AboutUsActivity";
    Toolbar index_toolbar;
    TextView tv_vn, tv_vn2, tv_d;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_aboutus);
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
        tv_vn = (TextView) findViewById(R.id.tv_vn);
        tv_vn2 = (TextView) findViewById(R.id.tv_vn2);
//        tv_d=(TextView) findViewById(R.id.tv_d);
        findViewById(R.id.rl_agreement).setOnClickListener(this);
        findViewById(R.id.rl_update).setOnClickListener(this);
    }


    private void initData() {
        getApkVersion(false);
        tv_vn.setText("V" + getAppVersionName());
        tv_vn2.setText("V" + getAppVersionName());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_update:
                //需请求接口
                getApkVersion(true);
                break;
            case R.id.rl_agreement:
                Intent in = new Intent(AboutUsActivity.this, AgreementActivity.class);
                startActivity(in);
                break;
        }
    }

    private void getApkVersion(final boolean click) {
        MyUtils.showDialog(AboutUsActivity.this, "获取版本信息...");
        OkHttpUtils
                .post()
                .url(LHHttpUrl.GETVERSION_URL)
                .addParams("type",1+"")
                .build()
                .execute(new UpdateApkCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                    }

                    @Override
                    public void onResponse(UpdateApkData mData) {
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                        if (mData.getErrcode() == 1) {
                            // 先判断是否有新版本 有弹提示框
                            if (!TextUtils.equals(mData.getData().getVersion(), getAppVersionName())) {
                                tv_vn2.setTextColor(getResources().getColor(R.color.main_color));
                                tv_vn2.setText("V" + mData.getData().getVersion());
                                getApplicationContext().setmNewApkData(mData.getData());
                                String msg="";
                                if(TextUtils.isEmpty(mData.getData().getMsg())){
                                    msg="有新版本啦";
                                }else {
                                    msg= mData.getData().getMsg();
                                }
                                DialogOkUtil.show_update_Dialog(AboutUsActivity.this, mData.getData().getVersion(),msg, new DialogOkUtil.On_Update_ClickListener() {
                                    @Override
                                    public void onCancle() {

                                    }

                                    @Override
                                    public void onDownload() {
                                        if (ContextCompat.checkSelfPermission(AboutUsActivity.this,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(AboutUsActivity.this,
                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    0x14);
                                            return;
                                        }
                                        downLoad();
                                    }
                                }).show();
                            }else {
                                if (click){
                                    MyUtils.showToast(AboutUsActivity.this, "暂时未发现新版本");
                                }

                            }
                        } else {
                            if (click){
                                MyUtils.showToast(AboutUsActivity.this, "暂时未发现新版本");
                            }

                        }
                    }
                });
    }

    private void downLoad() {
        if (NetWorkChangeReceiver.NET_WORK_WIFI_TYPE) {
            startService(new Intent(AboutUsActivity.this, UpdateService.class));
        } else if (!NetWorkChangeReceiver.NET_WORK_TYPE) {
            DialogOkUtil.show_Ok_Dialog(AboutUsActivity.this, "请打开网络连接", new DialogOkUtil.On_OK_ClickListener() {
                @Override
                public void onOk() {
                }
            }).show();
        } else {
            DialogOkUtil.show_OK_NO_Dialog(AboutUsActivity.this, "当前网络不是Wifi，是否用手机流量下载？", new DialogOkUtil.On_OK_N0_ClickListener() {
                @Override
                public void onOk() {
                    startService(new Intent(AboutUsActivity.this, UpdateService.class));
                }

                @Override
                public void onNo() {

                }
            }).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0x14:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    downLoad();
                } else {
                    MyUtils.showToast(AboutUsActivity.this, "权限获取失败，取消下载");
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(this);
    }


}
