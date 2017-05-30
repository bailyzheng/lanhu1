package com.zys.jym.lanhu.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.App;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.adapter.ZDLvAdapter;
import com.zys.jym.lanhu.bean.MyTopData;
import com.zys.jym.lanhu.bean.RegisterData;
import com.zys.jym.lanhu.bean.TopData;
import com.zys.jym.lanhu.fragment.impl.MainFragment;
import com.zys.jym.lanhu.httpcallback.MyTopCallback;
import com.zys.jym.lanhu.httpcallback.RegisterCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.DialogOkUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MySharedPrefrencesUtil;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.SPrefUtil;
import com.zys.jym.lanhu.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/1/1.
 */

public class ZDActivity extends BaseActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,View.OnClickListener {
    static String TAG = "TAG--ZDActivity";
    Toolbar index_toolbar;
    private static ListView lv_zd;
    static SwipeRefreshLayout srf_ly;
    private static int page=1;
    private static ZDLvAdapter mAdapter;
    private static Dialog pdialog = null;
    private static TextView tv_num,tv_jian,tv_jia,tv_finsh;
    private static int zdNum=1;
    private static String doItemId;
    private static TextView tv_s_zdnum;
    private static RelativeLayout rl_nor;
    private static ZDActivity zda;
    private static List<TopData> mTopDataList = new ArrayList<TopData>();
    static App app;
    boolean scrollFlag= false;// 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    public static String type;
    private RadioGroup rg_mine_releae;
    private RadioButton rb_mine_rpersonalcard;
    private RadioButton rb_mine_rgroupcard;
    private ImageView iv_mine_back;
    private ImageView iv_mine_share;
    private TextView tv_wd_day;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_zd);
        zda=this;
        ActivityUtil.add(zda);
        initViews();
    }


    private void initViews() {
        srf_ly= (SwipeRefreshLayout) findViewById(R.id.srf_ly);
        //改变加载显示的颜色
        srf_ly.setColorSchemeColors(getResources().getColor(R.color.main_color));
        srf_ly.setOnRefreshListener(this);
        lv_zd= (ListView) findViewById(R.id.lv_zd);
        tv_s_zdnum= (TextView) findViewById(R.id.tv_s_zdnum);
//        findViewById(R.id.tv_fenxiang).setOnClickListener(new myOnClick());
//        setUpdateTime(lv_zd);
        lv_zd.setOnItemClickListener(this);
        rl_nor= (RelativeLayout) findViewById(R.id.rl_nor);
//        findViewById(R.id.tv_tor).setOnClickListener(new myOnClick());
        rg_mine_releae=(RadioGroup)findViewById(R.id.rg_mine_releae);
        rb_mine_rpersonalcard=(RadioButton)findViewById(R.id.rb_mine_rpersonalcard);
        rb_mine_rgroupcard=(RadioButton)findViewById(R.id.rb_mine_rgroupcard);
        iv_mine_back=(ImageView)findViewById(R.id.iv_mine_back);
        iv_mine_share=(ImageView)findViewById(R.id.iv_mine_share);
        iv_mine_back.setOnClickListener(this);
        iv_mine_share.setOnClickListener(this);
        tv_wd_day=(TextView)findViewById(R.id.tv_wd_day);

        initListViewScroll();
        rg_mine_releae.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_mine_rpersonalcard:
                        type="2";
                        tv_wd_day.setVisibility(View.GONE);
                        getData(page,true,type);
                        break;
                    case R.id.rb_mine_rgroupcard:
                        type="1";
                        tv_wd_day.setVisibility(View.VISIBLE);
                        getData(page,true,type);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    private void initData() {
        app=getApplicationContext();
        if (getApplicationContext().getPurseData()!=null){
            tv_s_zdnum.setText("您当前剩余"+getApplicationContext().getPurseData().getTopbalance()+"次置顶");
        }
        type=getIntent().getStringExtra("type");
        if(TextUtils.isEmpty(type)){
            type="2";
        }
        if(type.equals("1")){
            rb_mine_rgroupcard.setChecked(true);
            tv_wd_day.setVisibility(View.VISIBLE);
        }
        if(type.equals("2")){
            rb_mine_rpersonalcard.setChecked(true);
            tv_wd_day.setVisibility(View.GONE);
        }
        getData(page,true,type);
    }
    private static void getData(final int mPage,boolean isV,String type) {
//        if (isV){
//            MyUtils.showDialog(zda,"加载中...");
//        }
//        MyUtils.Loge(TAG, "p=" + mPage + "login_token=" + app.getUser().getLogin_token());
//        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .url(LHHttpUrl.MYTOPICLIST_URL)
                    .addParams("p", mPage + "")
                    .addParams("type", type)
//                    .addParams("login_token", app.getUser().getLogin_token())
                    .addParams("login_token", SPrefUtil.getString(zda,"TOKEN",""))
//                .addParams("pageSize",1+"")
                    .build()
                    .execute(new MyTopCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            srf_ly.setRefreshing(false);
//                        rl_nor.setVisibility(View.VISIBLE);
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                            MyUtils.showToast(zda, "连接服务器失败，请稍后再试");
                        }

                        @Override
                        public void onResponse(MyTopData mData) {
                            srf_ly.setRefreshing(false);
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                            if(mData.getErrcode()==40001){
                                ActivityUtil.exitAll();
                                ActivityUtil.toLogin(zda);
                                return;
                            }
                            if (mData.getErrcode() == 1) {
                                if (mData.getData().getTopicList().size() != 0) {
                                    lv_zd.setVisibility(View.VISIBLE);
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
                                        mAdapter = new ZDLvAdapter(zda, mTopDataList);
                                        lv_zd.setAdapter(mAdapter);
                                    }

                                } else {
                                    MyUtils.Loge(TAG, "没有更多数据");
                                    if (page == 1) {
                                        rl_nor.setVisibility(View.VISIBLE);
                                        lv_zd.setVisibility(View.INVISIBLE);
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
                                    lv_zd.setVisibility(View.INVISIBLE);
                                    rl_nor.setVisibility(View.VISIBLE);
                                }
                                MyUtils.showToast(zda, mData.getErrmsg());
                            }
                        }
                    });
//        }


    }
    //置顶
    public static void itemZD(String id){
        doItemId=id;
        if (app.getPurseData()!=null){
            if (MyUtils.Str2Int(app.getPurseData().getTopbalance())==0){
                DialogOkUtil.show_Ok_Dialog(zda, "很遗憾，您的置顶次数不足", new DialogOkUtil.On_OK_ClickListener() {
                    @Override
                    public void onOk() {
                        Intent in =new Intent(zda, DHZDActivity.class);
                        zda.startActivity(in);

                    }
                }).show();
            }else {
                showZDDialog();
            }
        }else {
            MyUtils.showToast(zda,"账户信息获取失败，请稍后再试");
        }

    }
    //自动刷新
    public static void itemZDSX(String id){
        doItemId=id;
        //判断是否是蓝狐会员
        if(app!=null&&app.getPurseData()!=null&&Long.parseLong(app.getPurseData().getViprest())<=0){
            DialogOkUtil.show_Ok_Dialog(zda, "很遗憾，您还不是蓝狐会员，该特权只有蓝狐会员才能使用", new DialogOkUtil.On_OK_ClickListener() {
                @Override
                public void onOk() {
                    Intent in =new Intent(zda, OpenVipActivity.class);
                    zda.startActivity(in);
                }
            }).show();

        }else {
            doZDSX();
        }
    }
    private static void doZd() {
        if (MyUtils.Str2Int(app.getPurseData().getTopbalance())<zdNum){
            DialogOkUtil.show_Ok_Dialog(zda, "很遗憾，您的置顶次数不足", new DialogOkUtil.On_OK_ClickListener() {
                @Override
                public void onOk() {
                    Intent in =new Intent(zda, DHZDActivity.class);
                    zda.startActivity(in);
                }
            }).show();
            return;
        }
        MyUtils.showDialog(zda,"置顶中...");
//        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .url(LHHttpUrl.TOPTOPIC_URL)
                    .addParams("id", doItemId)
//                    .addParams("login_token", app.getUser().getLogin_token())
                    .addParams("login_token", SPrefUtil.getString(zda,"TOKEN",""))
                    .addParams("num", zdNum + "")
                    .build()
                    .execute(new RegisterCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                            MyUtils.showToast(zda, "连接服务器失败，请稍后再试");
                        }

                        @Override
                        public void onResponse(RegisterData mData) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                            if(mData.errcode==40001){
                                ActivityUtil.exitAll();
                                ActivityUtil.toLogin(zda);
                                return;
                            }
                            if (mData.errcode == 1) {
                                MyUtils.showToast(zda, "置顶成功!");
                                tv_s_zdnum.setText("您当前剩余" + (MyUtils.Str2Int(app.getPurseData().getTopbalance()) - zdNum) + "次置顶");
                                app.getPurseData().setTopbalance(MyUtils.Str2Int(app.getPurseData().getTopbalance()) - zdNum + "");
                                page = 1;
                                getData(page, true, type);
                            } else {
                                MyUtils.showToast(zda, mData.errmsg);
                                MyUtils.Loge(TAG, "失败info=" + mData.errmsg);
                            }
                        }
                    });
//        }

    }

    private static void doZDSX() {
        MyUtils.showDialog(zda,"刷新中...");
//        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .url(LHHttpUrl.AUTOREFRESH_URL)
                    .addParams("id", doItemId)
//                    .addParams("login_token", app.getUser().getLogin_token())
                    .addParams("login_token", SPrefUtil.getString(zda,"TOKEN",""))
                    .build()
                    .execute(new RegisterCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                            MyUtils.showToast(zda, "连接服务器失败，请稍后再试");
                        }

                        @Override
                        public void onResponse(RegisterData mData) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                            if(mData.errcode==40001){
                                ActivityUtil.exitAll();
                                ActivityUtil.toLogin(zda);
                                return;
                            }
                            if (mData.errcode == 1) {
                                MyUtils.showToast(zda, "自动刷新成功!");
                                page = 1;
                                getData(page, true, type);
                            } else {
                                MyUtils.showToast(zda, mData.errmsg);
                                MyUtils.Loge(TAG, "失败info=" + mData.errmsg);
                            }
                        }
                    });
//        }
    }

    //手动刷新
    public static void itemSDSX(String id){

        MyUtils.showDialog(zda,"刷新中...");
//        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .url(LHHttpUrl.HANDREFRESH_URL)
                    .addParams("id", id)
//                    .addParams("login_token", app.getUser().getLogin_token())
                    .addParams("login_token", SPrefUtil.getString(zda,"TOKEN",""))
                    .build()
                    .execute(new RegisterCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                            MyUtils.showToast(zda, "连接服务器失败，请稍后再试");
                        }

                        @Override
                        public void onResponse(RegisterData mData) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                            if(mData.errcode==40001){
                                ActivityUtil.exitAll();
                                ActivityUtil.toLogin(zda);
                                return;
                            }
                            if (mData.errcode == 1) {
                                MyUtils.showToast(zda, "手动刷新成功!");
                                MySharedPrefrencesUtil.setParam(zda, "sdsx_time", System.currentTimeMillis() + 180 * 1000);
                                page = 1;
                                getData(page, true, type);
                            } else {
                                MyUtils.showToast(zda, mData.errmsg);
                                MyUtils.Loge(TAG, "失败info=" + mData.errmsg);
                            }
                        }
                    });
//        }
    }
    //删除
    public static void itemDelete(final String id){
        DialogOkUtil.show_OK_NO_Dialog(zda, "是否删除此条群聊？", new DialogOkUtil.On_OK_N0_ClickListener() {
            @Override
            public void onOk() {
                MyUtils.showDialog(zda,"删除中...");
//                if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
                    OkHttpUtils
                            .post()
                            .url(LHHttpUrl.DELTOPIC_URL)
                            .addParams("id", id)
//                            .addParams("login_token", app.getUser().getLogin_token())
                            .addParams("login_token", SPrefUtil.getString(zda,"TOKEN",""))
                            .build()
                            .execute(new RegisterCallback() {
                                @Override
                                public void onError(Call call, Exception e) {
                                    MyUtils.dismssDialog();
                                    MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                                    MyUtils.showToast(zda, "连接服务器失败，请稍后再试");
                                }

                                @Override
                                public void onResponse(RegisterData mData) {
                                    MyUtils.dismssDialog();
                                    MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                                    if(mData.errcode==40001){
                                        ActivityUtil.exitAll();
                                        ActivityUtil.toLogin(zda);
                                        return;
                                    }
                                    if (mData.errcode == 1) {
                                        MyUtils.showToast(zda, "删除成功!");
//                                    GroupPager.gHaveData=false;
//                                    TopPager.tHaveData=false;
                                        page = 1;
                                        getData(page, true, type);
                                    } else {
                                        MyUtils.showToast(zda, mData.errmsg);
                                        MyUtils.Loge(TAG, "失败info=" + mData.errmsg);
                                    }
                                }
                            });
//                }
            }

            @Override
            public void onNo() {

            }
        }).show();


    }

    private static void showZDDialog() {
        LayoutInflater inflater = LayoutInflater.from(zda);
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
        pdialog = new Dialog(zda, R.style.custom_dialog);// 创建自定义样式dialog
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
        getData(page,false,type);
        if (app.getPurseData()!=null){
            tv_s_zdnum.setText("您当前剩余"+app.getPurseData().getTopbalance()+"次置顶");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_mine_back:
                finish();
                break;
            case R.id.iv_mine_share:
                Intent in =new Intent(zda, ShareActivity.class);
                zda.startActivity(in);
                break;
            default:
                break;
        }
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
//                case R.id.tv_fenxiang:
//                    Intent in =new Intent(zda, ShareActivity.class);
//                    zda.startActivity(in);
//                    break;
//                case R.id.tv_tor:
//                    Intent intent = new Intent(zda,Main2Activity.class);
//                    intent.putExtra("choose_tab",R.id.rb_release);
//                    zda.startActivity(intent);
//                    zda.finish();
//
//                    break;

            }
        }
    }
    public static void setZDNum(){
        tv_s_zdnum.setText("您当前剩余"+app.getPurseData().getTopbalance()+"次置顶");
    }




    //到最后一条自动加载
    private void initListViewScroll() {
        lv_zd.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;
                        // 判断滚动到底部
                        if (lv_zd.getLastVisiblePosition() == (lv_zd.getCount() - 1)) {
                            page++;
                            getData(page,false,type);
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
                        && ScreenUtil.getScreenViewBottomHeight(lv_zd) >= ScreenUtil
                        .getScreenHeight(ZDActivity.this)) {
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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        TopData mData= (TopData) adapterView.getAdapter().getItem(position);
        Intent in =new Intent(zda,CardDetailsActivity.class);
        in.putExtra("cardDetail",mData);
        in.putExtra("FromTop",true);
        zda.startActivity(in);
    }



    @Override
    protected void onDestroy() {
        mAdapter=null;
        mTopDataList.clear();
        super.onDestroy();
        ActivityUtil.delect(zda);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
