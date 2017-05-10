package com.zys.jym.lanhu.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.zys.jym.lanhu.App;
import com.zys.jym.lanhu.activity.Main2Activity;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/1/22.
 */

public abstract class BaseFragment extends Fragment {
    String TAG="TAG--BaseFragment";
    protected View mRootView=null;
    public static Context mContext;
    public static Activity mActivity;
    public static App app;
    protected boolean isVisible;
    private boolean isPrepared;
    private boolean isFirst = true;


    public View getmRootView() {
        return mRootView;
    }

    public void setmRootView(View mRootView) {
        this.mRootView = mRootView;
    }

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        Log.d("TAG", "fragment->setUserVisibleHint");
//        MyUtils.Loge(TAG,"BaseFragment--setUserVisibleHint"+",isPrepared="+isPrepared+",isVisible="+isVisible+",isFirst="+isFirst);
        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mActivity=getActivity();
        app= (App) mContext.getApplicationContext();
        setHasOptionsMenu(true);
//        Log.d("TAG", "fragment->onCreate");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if (mRootView == null) {
            mRootView = initView();
        }
//        Log.d("TAG", "fragment->onCreateView");
//        MyUtils.Loge(TAG,"BaseFragment--onCreateView"+",isPrepared="+isPrepared+",isVisible="+isVisible+",isFirst="+isFirst);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Log.d("TAG", "fragment->onActivityCreated");
        isPrepared = true;
        lazyLoad();
    }

    protected void lazyLoad() {
//        MyUtils.Loge(TAG,"BaseFragment--lazyLoad"+",isPrepared="+isPrepared+",isVisible="+isVisible+",isFirst="+isFirst);
        if (!isPrepared || !isVisible || !isFirst) {
            return;
        }
        Log.d("TAG", getClass().getName() + "->initData()");
        initData();
        isFirst = false;

    }

    //do something
    protected void onInvisible() {
//        MyUtils.Loge(TAG,"BaseFragment--onInvisible"+"isPrepared="+isPrepared+"isVisible="+isVisible+"isFirst="+isFirst);

    }

    public abstract View initView();

    public abstract void initData();
    //获取状态栏高度
    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = getContext().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }
}
