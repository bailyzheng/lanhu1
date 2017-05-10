package com.zys.jym.lanhu.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/4/20.
 */
public abstract class MainBaseFragment  extends Fragment {
    public Activity mMainActivity;

    // 创建Fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mMainActivity = getActivity();
    }

    // 处理Fragment布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return initViews();
    }

    // 依附的Activity创建完成
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 初始化布局
     * @return
     */
    public abstract View initViews();

    /**
     * 初始化数据
     * @return
     */
    public void initData() {

    }
}
