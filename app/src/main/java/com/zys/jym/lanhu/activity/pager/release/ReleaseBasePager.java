package com.zys.jym.lanhu.activity.pager.release;


import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.zys.jym.lanhu.R;


public class ReleaseBasePager {
	public static Activity mReleaseActivity;

	public FrameLayout fl_base_content;// 内容
	public View mRootView;// 根布局

	public ReleaseBasePager(Activity activity) {
		mReleaseActivity = activity;
		initViews();
		initOneData();
	}
	/**
	 * 初始化布局
	 */
	public void initViews() {
		mRootView = View.inflate(mReleaseActivity, R.layout.release_base_pager, null);
		
		fl_base_content = (FrameLayout) mRootView
				.findViewById(R.id.fl_base_content_r);
	}
	public void initOneData() {

	}
	/**
	 * 初始化数据
	 */
	public void initData() {

	}
}
