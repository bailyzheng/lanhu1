package com.zys.jym.lanhu.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

//import com.tencent.smtt.sdk.WebChromeClient;
//import com.tencent.smtt.sdk.WebSettings;
//import com.tencent.smtt.sdk.WebView;
//import com.tencent.smtt.sdk.WebViewClient;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.utils.MyUtils;

public class UseInstructions2Activity extends BaseActivity {

    private TextView tv_use_instructions_show;
    private Toolbar toolbar;
    private String urlShow;
    private WebView wv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_instructions2);
//        getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initView();
        Intent intent = getIntent();
        urlShow = intent.getStringExtra("url");
        MyUtils.Loge("aaa", "urlShow::" + urlShow);
        if(urlShow.length()>0&&urlShow.contains("https")){
            urlShow=urlShow.replaceFirst("https","http");
        }
        if(!TextUtils.isEmpty(urlShow)) {
            initData();
        }
//        loadUrl();
    }
//
//    private void loadUrl() {
//        Intent intent = getIntent();
//        urlShow = intent.getStringExtra("url");
//        MyUtils.Loge("aaa", "urlShow::" + urlShow);
//        WebSettings webSettings = wv_show.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        wv_show.loadUrl(urlShow);
//        wv_show.setWebViewClient(new WebViewClient() {
//            /**
//             *  防止加载网页时调起系统浏览器
//             * @param view
//             * @param url
//             * @return
//             */
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onReceivedError(WebView var1, int var2, String var3, String var4) {
//                MyUtils.Loge("打印日志","网页加载失败");
//            }
//        });
//        //进度条
//        wv_show.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    MyUtils.Loge("打印日志","加载完成");
//                }
//            }
//        });
//    }

    private void initData() {

        MyUtils.Loge("aaa", "改后----urlShow::" + urlShow);
        WebSettings webSettings = wv_show.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);

        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);//WebView中包含一个ZoomButtonsController，当使用web.getSettings().setBuiltInZoomControls(true);启用后，用户一旦触摸屏幕，就会出现缩放控制图标。
//        webSettings.setPluginState(WebSettings.PluginState.ON);
//        webSettings.setPluginsEnabled(true);//可以使用插件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//设置加载进来的页面自适应手机屏幕
        webSettings.setAllowFileAccess(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);//启用Dom内存（不加就显示不出来）
        wv_show.setWebChromeClient(new WebChromeClient());
//        加载需要显示的网页
        wv_show.loadUrl(urlShow);

//        wv_show.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                MyUtils.Loge("aaa", "webview:url:" + url);
////                view.loadUrl(url);
//                if (url.startsWith("http:") || url.startsWith("https:")) {
//                    return false;
//                }
//                try {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                } catch (Exception e) {
//                }
//                return true;
//            }
//        });
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.index_toolbar);
        toolbar.setTitle("新手必看");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.mipmap.backimg);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        wv_show = (WebView) findViewById(R.id.wv_my_show);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wv_show.setVisibility(View.GONE);//ZoomButtonsController有一个register和unregister的过程
//        wv_show.destroy();
    }
}
