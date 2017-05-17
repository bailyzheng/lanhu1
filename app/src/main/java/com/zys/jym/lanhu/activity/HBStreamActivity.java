package com.zys.jym.lanhu.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.adapter.StreamLvAdapter;
import com.zys.jym.lanhu.bean.HB_ZDStream;
import com.zys.jym.lanhu.bean.StreamData;
import com.zys.jym.lanhu.httpcallback.StreamCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.SPrefUtil;
import com.zys.jym.lanhu.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 狐币流水
 * Created by Administrator on 2016/12/15.
 */

public class HBStreamActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    String TAG = "TAG--HBStreamActivity";
    Toolbar index_toolbar;
    SwipeRefreshLayout srf_ly;
    ListView lv_hbstream;
    List<HB_ZDStream> mHBStreamlist = new ArrayList<HB_ZDStream>();
    ;
    StreamLvAdapter mAdapter;
    boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    int page = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_stream);
        ActivityUtil.add(this);
        initToolBar();
        initViews();
        initData();
    }

    private void initToolBar() {
        index_toolbar = (Toolbar) findViewById(R.id.index_toolbar);
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
        srf_ly = (SwipeRefreshLayout) findViewById(R.id.srf_ly);
        //改变加载显示的颜色
        srf_ly.setColorSchemeColors(getResources().getColor(R.color.main_color));
        srf_ly.setOnRefreshListener(this);
        lv_hbstream = (ListView) findViewById(R.id.lv_stream);
        initListViewScroll();
    }

    private void initData() {
        getData(page, true);
    }

    private void getData(final int mPage, boolean isV) {
        if (isV) {
            MyUtils.showDialog(HBStreamActivity.this, "加载中...");
        }
//        if(getApplicationContext()!=null&&getApplicationContext().getUser()!=null&&!TextUtils.isEmpty(getApplicationContext().getUser().getLogin_token())) {
        OkHttpUtils
                .post()
                .tag(this)
                .url(LHHttpUrl.GETPURSELOG_URL)
//                .addParams("login_token", getApplicationContext().getUser().getLogin_token())
                .addParams("login_token", SPrefUtil.getString(HBStreamActivity.this,"TOKEN",""))
                .addParams("p", mPage + "")
                .build()
                .execute(new StreamCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        srf_ly.setRefreshing(false);
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                        MyUtils.showToast(HBStreamActivity.this, "连接服务器失败，请稍后再试");
                    }

                    @Override
                    public void onResponse(StreamData mData) {
                        MyUtils.dismssDialog();
                        srf_ly.setRefreshing(false);
                        MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                        if (mData.getErrcode() == 40001) {
                            ActivityUtil.exitAll();
                            ActivityUtil.toLogin(HBStreamActivity.this);
                            return;
                        }
                        if (mData.getErrcode() == 1) {
                            if (mData.getData().getLogList().size() != 0) {
                                lv_hbstream.setVisibility(View.VISIBLE);
                                if (mPage == 1) {
                                    mHBStreamlist.clear();
                                    mHBStreamlist.addAll(mData.getData().getLogList());
                                } else {
                                    mHBStreamlist.addAll(mData.getData().getLogList());
                                }
                                if (mAdapter != null) {
                                    mAdapter.notifyDataSetChanged();
                                } else {
                                    mAdapter = new StreamLvAdapter(HBStreamActivity.this, mHBStreamlist, 1);
                                    lv_hbstream.setAdapter(mAdapter);
                                }

                            } else {
                                MyUtils.Loge(TAG, "没有更多数据");
                                if (page == 1) {
                                    lv_hbstream.setVisibility(View.INVISIBLE);
                                    //MyUtils.showToast(HBStreamActivity.this,mData.getErrmsg());
                                }
                                page--;
                                if (page < 1) {
                                    page = 1;
                                }
                            }
                        } else {
                            MyUtils.Loge(TAG, "page=" + page);
                            if (mPage > 1) {
                                page--;
                                if (page < 1) {
                                    page = 1;
                                }
                                MyUtils.Loge(TAG, "page1=" + page);
                            } else {
                                lv_hbstream.setVisibility(View.INVISIBLE);
                            }
                            //MyUtils.showToast(HBStreamActivity.this, mData.getErrmsg());
                        }

                    }
                });
//        }
    }


    //上滑出现回到顶部按钮
    private void initListViewScroll() {
        lv_hbstream.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        if (lv_hbstream.getLastVisiblePosition() == (lv_hbstream.getCount() - 1)) {
                            page++;
                            getData(page, false);
                        }
//                        // 判断滚动到顶部
//                        if (lv_hbstream.getFirstVisiblePosition() == 0) {
//                            btn_totop.setVisibility(View.GONE);
//                        }
                        // 判断是否滚动到顶部  否显示  是隐藏
//                        if (lv_hbstream.getFirstVisiblePosition() != 0) {
//                            btn_totop.setVisibility(View.VISIBLE);
//                        }else {
//                            btn_totop.setVisibility(View.GONE);
//                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                        scrollFlag = true;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                        scrollFlag = false;
                        break;
                }
            }

            /**
             * firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
             * visibleItemCount：当前能看见的列表项个数（小半个也算） totalItemCount：列表项共数
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // 当开始滑动且ListView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮
                if (scrollFlag
                        && ScreenUtil.getScreenViewBottomHeight(lv_hbstream) >= ScreenUtil
                        .getScreenHeight(HBStreamActivity.this)) {
                    if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
//                        btn_totop.setVisibility(View.VISIBLE);
                    } else if (firstVisibleItem < lastVisibleItemPosition) {// 下滑
//                        btn_totop.setVisibility(View.GONE);
                    } else {
                        return;
                    }
                    lastVisibleItemPosition = firstVisibleItem;
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData(page, false);
    }


    @Override
    protected void onDestroy() {
        mAdapter = null;
        mHBStreamlist.clear();
        super.onDestroy();
        ActivityUtil.delect(this);
    }
}
