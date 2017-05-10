package com.zys.jym.lanhu.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;

/**
 * Created by Administrator on 2016/12/25.
 */

public class FTFActivity extends BaseActivity {
    Toolbar index_toolbar;
    ImageView iv_mycode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_ftf);
        initToolBar();
        initViews();
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
        iv_mycode= (ImageView) findViewById(R.id.iv_mycode);
    }

    private void initData() {

    }

}
