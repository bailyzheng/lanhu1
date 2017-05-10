package com.zys.jym.lanhu.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.utils.ActivityUtil;

/**
 * Created by Administrator on 2016/12/17.
 */

public class AgreementActivity extends BaseActivity {
    Toolbar index_toolbar;
    WebView wv_agreement;
    AgreementActivity aga;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_agreement);
        aga=this;
        ActivityUtil.add(aga);
        initViews();
        initToolBar();
        initData();
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

    private void initViews() {
        wv_agreement= (WebView) findViewById(R.id.wv_agreement);
    }

    private void initData() {

        // 加载需要显示的网页
        wv_agreement.loadUrl("file:///android_asset/agreement.html");
        wv_agreement.setWebViewClient(new AgaWebViewClient());
        WebSettings settings = wv_agreement.getSettings();
        settings.setJavaScriptEnabled(true);// 表示支持js
//        settings.setBuiltInZoomControls(true);// 显示放大缩小按钮
        settings.setUseWideViewPort(true);// 支持双击缩放
    }
    @Override
    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK && wv_agreement.canGoBack()) {
                // goBack()表示返回WebView的上一页面
                wv_agreement.goBack();
                return true;
            } else {
                if (wv_agreement != null) {
                    wv_agreement.destroy();
                }
                aga.finish();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return false;
    }
    private class AgaWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(aga);
    }
}
