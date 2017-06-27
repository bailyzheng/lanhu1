package com.zys.jym.lanhu.activity.pager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.App;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.CardDetailsActivity;
import com.zys.jym.lanhu.activity.DHZDActivity;
import com.zys.jym.lanhu.activity.MyShare2Activity;
import com.zys.jym.lanhu.activity.OpenVipActivity;
import com.zys.jym.lanhu.activity.ShareActivity;
import com.zys.jym.lanhu.adapter.TopLvAdapter;
import com.zys.jym.lanhu.bean.MyTopData;
import com.zys.jym.lanhu.bean.RegisterData;
import com.zys.jym.lanhu.bean.TopData;
import com.zys.jym.lanhu.fragment.impl.MainFragment;
import com.zys.jym.lanhu.httpcallback.MyTopCallback;
import com.zys.jym.lanhu.httpcallback.RegisterCallback;
import com.zys.jym.lanhu.utils.DialogOkUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by Administrator on 2016/3/29.
 */
public class TopPager extends MainBasePager implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static String TAG = "TAG--TopPager";
    private Toolbar index_toolbar;
    private static App app;
    static SwipeRefreshLayout srf_ly;
    private static ListView lv_top;
    private static int page=1;
    public static TopLvAdapter mAdapter;
    private static Dialog pdialog = null;
    private static TextView tv_num,tv_jian,tv_jia,tv_finsh;
    private static int zdNum=1;
    private static String doItemId;
    private static TextView tv_s_zdnum;
    TextView tv_fenxiang,tv_tor;
    private static RelativeLayout rl_nor;
    public static List<TopData> mTopDataList = new ArrayList<TopData>();
//    public static boolean tHaveData=false;
boolean scrollFlag= false;// 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置

    public TopPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initViews() {
        super.initViews();
        MyUtils.Loge(TAG,"TopPager--initViews");
        app= (App) mMainActivity.getApplicationContext();
        View view = View.inflate(mMainActivity, R.layout.pager_top, null);
        srf_ly= (SwipeRefreshLayout) view.findViewById(R.id.srf_ly);
        //改变加载显示的颜色
        srf_ly.setColorSchemeColors(mMainActivity.getResources().getColor(R.color.main_color));
        srf_ly.setOnRefreshListener(this);
        lv_top= (ListView) view.findViewById(R.id.lv_top);
        tv_s_zdnum= (TextView) view.findViewById(R.id.tv_s_zdnum);
        view.findViewById(R.id.tv_fenxiang).setOnClickListener(new myOnClick());
        initListViewScroll();
        lv_top.setOnItemClickListener(this);
        rl_nor= (RelativeLayout) view.findViewById(R.id.rl_nor);
        view.findViewById(R.id.tv_tor).setOnClickListener(new myOnClick());
        fl_base_content.addView(view);

    }

    @Override
    public void initData() {
        super.initData();
        if (app.getPurseData()!=null){
            tv_s_zdnum.setText("您当前剩余"+app.getPurseData().getTopbalance()+"次置顶");
        }
        page=1;
//        if (!tHaveData){
            getData(page,false);
//        }
    }

    public static void getData(final int mPage,boolean isV) {
//        if (!NetWorkChangeReceiver.NET_WORK_TYPE){
////            MyUtils.showToast(mMainActivity,"网络连接不可用，请检查网络");
//             srf_ly.setRefreshing(false);
//            MyUtils.Loge(TAG,"网络连接不可用");
//            return;
//       }

        if (isV){
            MyUtils.showDialog(mMainActivity,"加载中...");
        }
//        MyUtils.Loge(TAG, "p=" + mPage + "login_token="
//                + app.getUser().getLogin_token());
        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .url(LHHttpUrl.MYTOPICLIST_URL)
                    .addParams("p", mPage + "")
                    .addParams("login_token", app.getUser().getLogin_token())
//                .addParams("pageSize",1+"")
                    .build()
                    .execute(new MyTopCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
//                        tHaveData=false;
                            srf_ly.setRefreshing(false);
//                        rl_nor.setVisibility(View.VISIBLE);
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
//                        MyUtils.showToast(mMainActivity, "连接服务器失败，请稍后再试");
                        }

                        @Override
                        public void onResponse(MyTopData mData) {
                            srf_ly.setRefreshing(false);
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                            if (mData.getErrcode() == 1) {
                                if (mData.getData().getTopicList().size() != 0) {
//                                tHaveData=true;
                                    lv_top.setVisibility(View.VISIBLE);
                                    rl_nor.setVisibility(View.GONE);
                                    if (mPage == 1) {
                                        mTopDataList.clear();
                                        mTopDataList.addAll(mData.getData().getTopicList());
                                    } else {
                                        mTopDataList.addAll(mData.getData().getTopicList());
                                    }
                                    if (mAdapter != null) {
                                        mAdapter.notifyDataSetChanged();
                                    } else {
                                        mAdapter = new TopLvAdapter(mMainActivity, mTopDataList);
                                        lv_top.setAdapter(mAdapter);
                                    }

                                } else {
                                    MyUtils.Loge(TAG, "没有更多数据");
                                    if (page == 1) {
                                        rl_nor.setVisibility(View.VISIBLE);
                                        lv_top.setVisibility(View.INVISIBLE);
                                    }
                                    page--;
                                    if (page < 1) {
                                        page = 1;
                                    }
                                }
                            } else {
//                            tHaveData=false;
                                MyUtils.Loge(TAG, "page=" + page);
                                if (mPage > 1) {
                                    page--;
                                    if (page < 1) {
                                        page = 1;
                                    }
                                    MyUtils.Loge(TAG, "page1=" + page);
                                } else {
                                    lv_top.setVisibility(View.INVISIBLE);
                                    rl_nor.setVisibility(View.VISIBLE);
                                }
                                MyUtils.showToast(mMainActivity, mData.getErrmsg());
                            }
                        }
                    });
        }


    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        TopData mData= (TopData) adapterView.getAdapter().getItem(position);
        Intent in =new Intent(mMainActivity,CardDetailsActivity.class);
        in.putExtra("cardDetail",mData);
        in.putExtra("FromTop",true);
        mMainActivity.startActivity(in);
    }


    //置顶
    public static void itemZD(String id){
        doItemId=id;
        if (app.getPurseData()!=null){
            if (MyUtils.Str2Int(app.getPurseData().getTopbalance())==0){
                DialogOkUtil.show_Ok_Dialog(mMainActivity, "好友通过您的邀请码注册蓝狐,您和好友各获得半小时置顶,在个人中心发起邀请,点击确定,直接兑换置顶.", new DialogOkUtil.On_OK_ClickListener() {
                    @Override
                    public void onOk() {
                        Intent in =new Intent(mMainActivity, DHZDActivity.class);
                        mMainActivity.startActivity(in);
                    }
                },"邀请好友,送三天被加,送置顶").show();
            }else {
                showZDDialog();
            }
        }else {
            MyUtils.showToast(mMainActivity,"账户信息获取失败，请稍后再试");
        }

    }
    //自动刷新
    public static void itemZDSX(String id){
        doItemId=id;
        //判断是否是蓝狐会员
        if(MyUtils.Str2Int(app.getPurseData().getViprest())<=0){

            DialogOkUtil.show_Ok_Dialog(mMainActivity, "很遗憾，您还不是蓝狐会员，该特权只有蓝狐会员才能使用", new DialogOkUtil.On_OK_ClickListener() {
                @Override
                public void onOk() {
                    Intent in =new Intent(mMainActivity, OpenVipActivity.class);
                    mMainActivity.startActivity(in);
                }
            },"").show();

        }else {
            doZDSX();
        }
    }

    //手动刷新
    public static void itemSDSX(String id){
        MyUtils.showDialog(mMainActivity,"刷新中...");
        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .url(LHHttpUrl.HANDREFRESH_URL)
                    .addParams("id", id)
                    .addParams("login_token", app.getUser().getLogin_token())
                    .build()
                    .execute(new RegisterCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                            MyUtils.showToast(mMainActivity, "连接服务器失败，请稍后再试");
                        }

                        @Override
                        public void onResponse(RegisterData mData) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                            if (mData.errcode == 1) {
                                MyUtils.showToast(mMainActivity, "手动刷新成功!");
                                page = 1;
                                getData(page, true);
                            } else {
                                MyUtils.showToast(mMainActivity, mData.errmsg);
                                MyUtils.Loge(TAG, "失败info=" + mData.errmsg);
                            }
                        }
                    });
        }
    }
    //删除
    public static void itemDelete(final String id){
        DialogOkUtil.show_OK_NO_Dialog(mMainActivity, "是否删除此条群聊？", new DialogOkUtil.On_OK_N0_ClickListener() {
            @Override
            public void onOk() {
                MyUtils.showDialog(mMainActivity,"删除中...");
                if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
                    OkHttpUtils
                            .post()
                            .url(LHHttpUrl.DELTOPIC_URL)
                            .addParams("id", id)
                            .addParams("login_token", app.getUser().getLogin_token())
                            .build()
                            .execute(new RegisterCallback() {
                                @Override
                                public void onError(Call call, Exception e) {
                                    MyUtils.dismssDialog();
                                    MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                                    MyUtils.showToast(mMainActivity, "连接服务器失败，请稍后再试");
                                }

                                @Override
                                public void onResponse(RegisterData mData) {
                                    MyUtils.dismssDialog();
                                    MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                                    if (mData.errcode == 1) {
                                        MyUtils.showToast(mMainActivity, "删除成功!");
//                                    GroupPager.gHaveData=false;
                                        page = 1;
                                        getData(page, true);
                                    } else {
                                        MyUtils.showToast(mMainActivity, mData.errmsg);
                                        MyUtils.Loge(TAG, "失败info=" + mData.errmsg);
                                    }
                                }
                            });
                }
            }

            @Override
            public void onNo() {

            }
        },"").show();


    }






    private static void showZDDialog() {
        LayoutInflater inflater = LayoutInflater.from(mMainActivity);
        View v = inflater.inflate(R.layout.view_zdsj_dialog, null);// 得到加载view
        Button btn_ok= (Button) v.findViewById(R.id.btn_ok);
        tv_num= (TextView) v.findViewById(R.id.tv_num);
        tv_jian=(TextView) v.findViewById(R.id.tv_jian);
        tv_jia=(TextView) v.findViewById(R.id.tv_jia);
        tv_finsh=(TextView) v.findViewById(R.id.tv_finsh);
        tv_jia.setOnClickListener(new myOnClick());
        tv_jian.setOnClickListener(new myOnClick());
        btn_ok.setOnClickListener(new myOnClick());

        tv_finsh.setOnClickListener(new myOnClick());
        if (pdialog!=null){
            pdialog=null;
        }
        pdialog = new Dialog(mMainActivity, R.style.custom_dialog);// 创建自定义样式dialog
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.setCancelable(false);
        pdialog.setContentView(v);

        if (!pdialog.isShowing()) {
            pdialog.show();
        }


    }

    @Override
    public void onRefresh() {
        page = 1;
        getData(page,false);
    }

    private static class myOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_ok:
                    if (pdialog.isShowing()){
                        pdialog.dismiss();
                        pdialog = null;
                    }
                    doZd();
                    break;
                case R.id.tv_finsh:
                    if (pdialog.isShowing()){
                        pdialog.dismiss();
                        pdialog = null;
                    }
                    break;
                case R.id.tv_jia:
                    zdNum++;
                    tv_num.setText(zdNum+"");
                    break;
                case R.id.tv_jian:
                    zdNum--;
                    if (zdNum<1){
                        zdNum=1;
                    }
                    tv_num.setText(zdNum+"");
                    break;
                case R.id.tv_fenxiang:
                    Intent in =new Intent(mMainActivity, MyShare2Activity.class);
                    mMainActivity.startActivity(in);
                    break;
                case R.id.tv_tor:
                    MainFragment.check_Release();
                    break;

            }
        }



    }
    private static void doZd() {
        if (MyUtils.Str2Int(app.getPurseData().getTopbalance())<zdNum){
            DialogOkUtil.show_Ok_Dialog(mMainActivity, "好友通过您的邀请码注册蓝狐,您和好友各获得半小时置顶,在个人中心发起邀请,点击确定,直接兑换置顶.", new DialogOkUtil.On_OK_ClickListener() {
                @Override
                public void onOk() {
                    Intent in =new Intent(mMainActivity, DHZDActivity.class);
                    mMainActivity.startActivity(in);
                }
            },"邀请好友,送三天被加,送置顶").show();
            return;
        }
        MyUtils.showDialog(mMainActivity,"置顶中...");
        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .url(LHHttpUrl.TOPTOPIC_URL)
                    .addParams("id", doItemId)
                    .addParams("login_token", app.getUser().getLogin_token())
                    .addParams("num", zdNum + "")
                    .build()
                    .execute(new RegisterCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                            MyUtils.showToast(mMainActivity, "连接服务器失败，请稍后再试");
                        }

                        @Override
                        public void onResponse(RegisterData mData) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                            if (mData.errcode == 1) {
                                MyUtils.showToast(mMainActivity, "置顶成功!");
                                tv_s_zdnum.setText("您当前剩余" + (MyUtils.Str2Int(app.getPurseData().getTopbalance()) - zdNum) + "次置顶");
                                app.getPurseData().setTopbalance(MyUtils.Str2Int(app.getPurseData().getTopbalance()) - zdNum + "");
                                page = 1;
                                getData(page, true);
                            } else {
                                MyUtils.showToast(mMainActivity, mData.errmsg);
                                MyUtils.Loge(TAG, "失败info=" + mData.errmsg);
                            }
                        }
                    });
        }

    }

    private static void doZDSX() {
        MyUtils.showDialog(mMainActivity,"刷新中...");
        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .url(LHHttpUrl.AUTOREFRESH_URL)
                    .addParams("id", doItemId)
                    .addParams("login_token", app.getUser().getLogin_token())
                    .build()
                    .execute(new RegisterCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                            MyUtils.showToast(mMainActivity, "连接服务器失败，请稍后再试");
                        }

                        @Override
                        public void onResponse(RegisterData mData) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());

                            if (mData.errcode == 1) {
                                MyUtils.showToast(mMainActivity, "自动刷新成功!");
                                page = 1;
                                getData(page, true);
                            } else {
                                MyUtils.showToast(mMainActivity, mData.errmsg);
                                MyUtils.Loge(TAG, "失败info=" + mData.errmsg);
                            }
                        }
                    });
        }
    }


    //最后一条自动加载
    private void initListViewScroll() {
        lv_top.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        if (lv_top.getLastVisiblePosition() == (lv_top.getCount() - 1)) {
                            page++;
                            getData(page,false);
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
                        && ScreenUtil.getScreenViewBottomHeight(lv_top) >= ScreenUtil
                        .getScreenHeight(mMainActivity)) {
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

}
