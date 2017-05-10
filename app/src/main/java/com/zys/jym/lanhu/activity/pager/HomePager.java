package com.zys.jym.lanhu.activity.pager;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zys.jym.lanhu.R;


/**
 * Created by Administrator on 2016/3/29.
 */
public class HomePager extends MainBasePager implements View.OnClickListener {
    static String TAG = "TAG--HomePager";
    private Toolbar index_toolbar;

    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initViews() {
        super.initViews();
        View view = View.inflate(mMainActivity, R.layout.pager_home, null);
        index_toolbar= (Toolbar) view.findViewById(R.id.index_toolbar);
        fl_base_content.addView(view);
        initToolBar();
    }



    private void initToolBar() {
        index_toolbar.setTitle("");
        index_toolbar.setTitleTextColor(Color.WHITE);
        index_toolbar.setNavigationIcon(R.mipmap.icon_search);
//        mMainActivity.setSupportActionBar(index_toolbar);
        index_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View view) {

    }


}
