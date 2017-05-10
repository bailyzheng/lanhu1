package com.zys.jym.lanhu.activity.pager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.App;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.CardDetailsActivity;
import com.zys.jym.lanhu.activity.LoginActivity;
import com.zys.jym.lanhu.activity.SearchActivity;
import com.zys.jym.lanhu.activity.SelectP2Activity;
import com.zys.jym.lanhu.activity.ShareActivity;
import com.zys.jym.lanhu.adapter.GroupLvAdapter;
import com.zys.jym.lanhu.bean.MyTopData;
import com.zys.jym.lanhu.bean.TopData;
import com.zys.jym.lanhu.httpcallback.MyTopCallback;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.RequestCode;
import com.zys.jym.lanhu.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by Administrator on 2016/3/29.
 */
public class GroupPager extends MainBasePager implements AdapterView.OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    static String TAG = "TAG--GroupPager";
    private Toolbar index_toolbar;
    private static ImageView iv_share;
    static SwipeRefreshLayout srf_ly;
    static ListView lv_group;
    static Spinner sp_zx;
    private static App app;
    static int page=1;
    static int cataId=0;
    public static GroupLvAdapter mAdapter;
    static TextView tv_dq;
    static String p = "", p_id = "";
    static String c = "", c_id = "";
    private Button btn_totop;
    boolean scrollFlag= false;// 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
//    public static boolean gHaveData=false;

    public static List<TopData> mTopDataList = new ArrayList<TopData>();

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RequestCode.TO_PC_ACTIVITY:
                    p = msg.getData().getString("p");
                    c = msg.getData().getString("c");
                    p_id = msg.getData().getString("p_id");
                    c_id = msg.getData().getString("c_id");
                    tv_dq.setText(p+"-"+c);
                    page=1;
                    getData(page);
                    break;
                case 0x101:
                    p = "";
                    c = "";
                    p_id = "";
                    c_id = "";
                    tv_dq.setText("全部地区");
                    page=1;
                    getData(page);
                    break;
            }
        }
    };


    public GroupPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initViews() {
        super.initViews();
        MyUtils.Loge(TAG,"GroupPager--initViews");
        app= (App) mMainActivity.getApplicationContext();
        View view = View.inflate(mMainActivity, R.layout.pager_group, null);
        index_toolbar= (Toolbar) view.findViewById(R.id.index_toolbar);
        iv_share= (ImageView) view.findViewById(R.id.iv_share);
        btn_totop= (Button) view.findViewById(R.id.btn_totop);
        btn_totop.setOnClickListener(this);
        srf_ly= (SwipeRefreshLayout) view.findViewById(R.id.srf_ly);
        //改变加载显示的颜色
        srf_ly.setColorSchemeColors(mMainActivity.getResources().getColor(R.color.main_color));
        srf_ly.setOnRefreshListener(this);
        lv_group= (ListView) view.findViewById(R.id.lv_group);
        lv_group.setOnItemClickListener(this);
        sp_zx= (Spinner) view.findViewById(R.id.sp_zx);
        setZXSP(sp_zx);
        tv_dq= (TextView) view.findViewById(R.id.tv_dq);
        tv_dq.setOnClickListener(this);
        initListViewScroll();
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
                Intent in =new Intent(mMainActivity, SearchActivity.class);
                mMainActivity.startActivity(in);
            }
        });
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (app.getIsLogin()){
                    Intent in =new Intent(mMainActivity, ShareActivity.class);
                    mMainActivity.startActivity(in);
                }else {
                    Intent in =new Intent(mMainActivity, LoginActivity.class);
                    mMainActivity.startActivity(in);
                }

            }
        });

    }
    private void setZXSP(Spinner sp_zx) {
        List<String> list = new ArrayList<String>();
        list.add("最新发布");
        list.add("人脉推广");
        list.add("宝妈互动");
        list.add("经验分享");
        list.add("小白学习");
        list.add("兼职信息");
        list.add("代理产品");
        list.add("微商团队");
        ArrayAdapter<String> sp_zxadapter=new ArrayAdapter<String>(mMainActivity, android.R.layout.simple_spinner_item,list);
        sp_zxadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_zx.setAdapter(sp_zxadapter);
        sp_zx.setOnItemSelectedListener(new OnZxItemSelectedListener());
    }

    @Override
    public void initData() {
        MyUtils.Loge(TAG,"GroupPager--initData");
//        if (page!=1&&mTopDataList.size()!=0&&mAdapter!=null){
//            page++;
//        }
//        MyUtils.showToast(mMainActivity,"测试-加载首页数据");
        page=1;
//        if (!gHaveData){
            getData(page);
//        }
        btn_totop.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.tv_dq:
               Intent in =new Intent(mMainActivity, SelectP2Activity.class);
               in.putExtra("inData",0);
               mMainActivity.startActivity(in);
               break;
           case R.id.btn_totop:
               setListViewPos(0);
               break;
       }
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData(page);
    }

    class  OnZxItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
            cataId=i;
            page=1;
            getData(page);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    public static void getData(final int mPage) {
        MyUtils.Loge(TAG,"mpage1="+mPage);
//        if (!NetWorkChangeReceiver.NET_WORK_TYPE){
//            MyUtils.showToast(mMainActivity,"测试-请求网络数据");
//            MyUtils.Loge(TAG,"网络连接不可用");
//            lv_group.onRefreshComplete();
//            return;
//        }
//        if (isOne){
//            MyUtils.showDialog(mMainActivity,"加载中...");
//        }
        String mcid="";
        if (cataId!=0){
            mcid=cataId+"";
        }
        MyUtils.Loge(TAG, "p=" + mPage + "--cataId="+mcid);
        OkHttpUtils
                .post()
                .url(LHHttpUrl.TOPICLIST_URL)
                .addParams("p", mPage+"")
//                .addParams("keyword","")
                .addParams("cataId",mcid)
                .addParams("provinceId",p_id)
                .addParams("cityId",c_id)
                .build()
                .execute(new MyTopCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
//                        gHaveData=false;
                        srf_ly.setRefreshing(false);
//                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
//                        MyUtils.showToast(mMainActivity, "测试--请求失败了");
                    }

                    @Override
                    public void onResponse(MyTopData mData) {
                        srf_ly.setRefreshing(false);
//                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());

                        if (mData.getErrcode()==1){
                            if (mData.getData().getTopicList().size()!=0){
//                                gHaveData=true;
//                                lv_group.setVisibility(View.VISIBLE);
//                                Toast.makeText(mMainActivity, "测试--请求有数据-数量="+mData.getData().getTopicList().size(), Toast.LENGTH_SHORT).show();
//                                MyUtils.showToast(mMainActivity, "测试--请求有数据-数量"+mData.getData().getTopicList().size());
                                if (mPage == 1) {
                                    mTopDataList.clear();
                                    mTopDataList.addAll(mData.getData().getTopicList());
                                } else {
                                    mTopDataList.addAll(mData.getData().getTopicList());
                                }
                                if (mAdapter != null) {
//                                    Toast.makeText(mMainActivity, "测试--适配器刷新", Toast.LENGTH_SHORT).show();
//                                    MyUtils.showToast(mMainActivity, "测试--适配器了刷新了");
                                    mAdapter.notifyDataSetChanged();
                                }else {
//                                    Toast.makeText(mMainActivity, "测试--new适配器", Toast.LENGTH_SHORT).show();
//                                    MyUtils.showToast(mMainActivity, "测试--new适配器了");
                                    mAdapter = new GroupLvAdapter(mMainActivity, mTopDataList);
                                    lv_group.setAdapter(mAdapter);
                                }

                            } else {
                                MyUtils.Loge(TAG, "没有更多数据");
                                MyUtils.Loge(TAG,"mpage1="+mPage);
//                                if (page==1){
////                                    lv_group.setVisibility(View.INVISIBLE);
//                                }
                                if (mTopDataList.size()!=0&&mAdapter!=null){
                                    mAdapter.notifyDataSetChanged();
                                }
                                page--;
                                if (page < 1) {
                                    page = 1;
                                }
                            }
                        }else {
//                            gHaveData=false;
                            MyUtils.Loge(TAG, "page=" + page);
                            if (mPage > 1) {
                                page--;
                                if (page < 1) {
                                    page = 1;
                                }
                                MyUtils.Loge(TAG, "page1=" + page);
                            } else {
//                                lv_group.setVisibility(View.INVISIBLE);
                            }
                            MyUtils.showToast(mMainActivity, mData.getErrmsg());
                        }
                    }
                });


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        TopData mData= (TopData) adapterView.getAdapter().getItem(position);
        Intent in =new Intent(mMainActivity,CardDetailsActivity.class);
        in.putExtra("cardDetail",mData);
        mMainActivity.startActivity(in);
    }





    //上滑出现回到顶部按钮
    private void initListViewScroll() {
        lv_group.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        if (lv_group.getLastVisiblePosition() == (lv_group.getCount() - 1)) {
                            page++;
                            getData(page);
                        }
//                        // 判断滚动到顶部
//                        if (lv_group.getRefreshableView().getFirstVisiblePosition() == 0) {
//                            btn_totop.setVisibility(View.GONE);
//                        }
                        // 判断是否滚动到顶部  否显示  是隐藏
                        if (lv_group.getFirstVisiblePosition() != 0) {
                            btn_totop.setVisibility(View.VISIBLE);
                        }else {
                            btn_totop.setVisibility(View.GONE);
                        }
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
                        && ScreenUtil.getScreenViewBottomHeight(lv_group) >= ScreenUtil
                        .getScreenHeight(mMainActivity)) {
                    if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
                        btn_totop.setVisibility(View.VISIBLE);
                    } else if (firstVisibleItem < lastVisibleItemPosition) {// 下滑
                        btn_totop.setVisibility(View.GONE);
                    } else {
                        return;
                    }
                    lastVisibleItemPosition = firstVisibleItem;
                }
            }
        });
    }




    /**
     * 滚动ListView到指定位置
     *
     * @param pos
     */
    private void setListViewPos(int pos) {
        if (android.os.Build.VERSION.SDK_INT >= 8) {
            lv_group.smoothScrollToPosition(pos);
        } else {
            lv_group.setSelection(pos);
        }
    }

}
