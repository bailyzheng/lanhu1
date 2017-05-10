package com.zys.jym.lanhu.activity.pager;


import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.zys.jym.lanhu.R;


public class MainBasePager {
	public static Activity mMainActivity;
	
	public FrameLayout fl_base_content;// 内容
	public View mRootView;// 根布局

	public MainBasePager(Activity activity) {
		mMainActivity = activity;
		initViews();
		initOneData();
	}



	/**
	 * 初始化布局
	 */
	public void initViews() {
		mRootView = View.inflate(mMainActivity, R.layout.main_base_pager, null);
		
		fl_base_content = (FrameLayout) mRootView
				.findViewById(R.id.fl_base_content);
	}
	public void initOneData() {

	}
	/**
	 * 初始化数据
	 */
	public void initData() {

	}
}
