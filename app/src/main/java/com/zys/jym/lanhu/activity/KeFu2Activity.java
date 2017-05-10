package com.zys.jym.lanhu.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.utils.MyUtils;

public class KeFu2Activity extends BaseActivity implements View.OnClickListener {
    private Toolbar index_toolbar;
    private TextView tv_kefu_zy;
    private TextView tv_kefu_hyf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ke_fu2);
        initToolBar();
        initView();
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
    private void initView() {
        tv_kefu_zy=(TextView)findViewById(R.id.tv_kefu_zy);
        tv_kefu_hyf=(TextView)findViewById(R.id.tv_kefu_hyf);
        tv_kefu_zy.setOnClickListener(this);
        tv_kefu_hyf.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_kefu_zy:
                startToQQ("2235725202");
                break;
            case R.id.tv_kefu_hyf:
                startToQQ("1197070411");
                break;
        }
    }
    public void startToQQ(String qqNum){
        if(checkApkExist(this,"com.tencent.mobileqq")){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin="+qqNum+"&version=1")));
        }else{
            MyUtils.showToast(this,"本机未安装QQ应用");}
    }
    public boolean checkApkExist(Context context,  String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e){
            return false;
        }
    }
}
