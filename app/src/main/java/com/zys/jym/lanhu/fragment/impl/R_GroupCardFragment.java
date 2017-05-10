package com.zys.jym.lanhu.fragment.impl;

import android.view.View;

import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.fragment.BaseFragment;
import com.zys.jym.lanhu.utils.MyUtils;


/**
 * Created by Administrator on 2017/1/22.
 */

public class R_GroupCardFragment extends BaseFragment {
        String TAG="TAG--R_GroupCardFragment";
    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_rgroupcard, null);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MyUtils.Loge(TAG,"R_GroupCardFragment--onResume");
    }

    @Override
    public void initData() {
    }

}
