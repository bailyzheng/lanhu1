package com.zys.jym.lanhu.activity.pager.release;

import android.app.Activity;
import android.view.View;

import com.zys.jym.lanhu.R;


/**
 * Created by Administrator on 2016/3/29.
 */
public class R_GroupCardPager extends ReleaseBasePager implements View.OnClickListener {
    static String TAG = "TAG--R_GroupCardPager";

    public R_GroupCardPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initViews() {
        super.initViews();
        View view = View.inflate(mReleaseActivity, R.layout.fragment_rgroupcard, null);
        fl_base_content.addView(view);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View view) {

    }


}
