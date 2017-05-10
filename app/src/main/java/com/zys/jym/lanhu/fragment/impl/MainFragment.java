package com.zys.jym.lanhu.fragment.impl;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zys.jym.lanhu.App;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.LoginActivity;
import com.zys.jym.lanhu.activity.pager.GroupPager;
import com.zys.jym.lanhu.activity.pager.MainBasePager;
import com.zys.jym.lanhu.activity.pager.MinePager;
import com.zys.jym.lanhu.activity.pager.ReleasePager;
import com.zys.jym.lanhu.activity.pager.TopPager;
import com.zys.jym.lanhu.fragment.MainBaseFragment;
import com.zys.jym.lanhu.view.NoScrollViewPager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/20.
 */
public class MainFragment extends MainBaseFragment {
    private static NoScrollViewPager vp_content;
    private static RadioGroup rg_main;
    private RadioButton rb_top,rb_release,rb_group,rb_mine;
    private ArrayList<MainBasePager> mPagerList;
    App app;

    @Override
    public View initViews() {
        View view = View.inflate(mMainActivity, R.layout.fragment_main, null);
        vp_content = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        rg_main= (RadioGroup) view.findViewById(R.id.rg_main);
        rb_group = (RadioButton) view.findViewById(R.id.rb_group);
        rb_release=(RadioButton) view.findViewById(R.id.rb_release);
        rb_top = (RadioButton) view.findViewById(R.id.rb_top);
        rb_mine=(RadioButton) view.findViewById(R.id.rb_mine);
        return view;
    }

    @Override
    public void initData() {
        app= (App) getActivity().getApplicationContext();
        rg_main.check(R.id.rb_group);// 设置默认选择
        mPagerList = new ArrayList<MainBasePager>();
        mPagerList.add(new GroupPager(mMainActivity));
        mPagerList.add(new ReleasePager(mMainActivity));
        mPagerList.add(new TopPager(mMainActivity));
        mPagerList.add(new MinePager(mMainActivity));
        vp_content.setAdapter(new ContentViewPagerAdapter());
        vp_content.setCurrentItem(0, false);
        vp_content.setOffscreenPageLimit(0);
//        mPagerList.get(0).initData();// 初始化首页数据
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_group:
                        vp_content.setCurrentItem(0, false);
                        break;
                    case R.id.rb_release:
                        if (app.getIsLogin()) {
                            vp_content.setCurrentItem(1, false);
                        }else {
                            rb_group.setChecked(true);
                            rb_release.setChecked(false);
                            toLogin();
                        }

                        break;
                    case R.id.rb_top:
                        if (app.getIsLogin()) {
                            vp_content.setCurrentItem(2, false);
                        }else {
                            rb_group.setChecked(true);
                            rb_top.setChecked(false);
                            toLogin();
                        }
                        break;
                    case R.id.rb_mine:
                        if (app.getIsLogin()) {
                            vp_content.setCurrentItem(3, false);
                        }else {
                            rb_group.setChecked(true);
                            rb_mine.setChecked(false);
                            toLogin();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        vp_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mPagerList.get(position).initData();
                if (position == 0) {
                    rg_main.check(R.id.rb_group);
                }
                if (position == 1) {
                    rg_main.check(R.id.rb_release);
                }
                if (position == 2) {
                    rg_main.check(R.id.rb_top);
                }
                if (position == 3) {
                    rg_main.check(R.id.rb_mine);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void toLogin() {
        Intent in =new Intent(mMainActivity, LoginActivity.class);
        startActivity(in);
    }


    /**
     * vp_content的Adapter
     *
     * @author ZYS
     */
    class ContentViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            MainBasePager pager = mPagerList.get(position);
            container.addView(pager.mRootView);
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    public static void check_Top(){
        rg_main.check(R.id.rb_top);
    }
    public static void check_Release(){
        rg_main.check(R.id.rb_release);
    }
    public static void check_Group(){
        rg_main.check(R.id.rb_group);
    }
}
