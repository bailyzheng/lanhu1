package com.zys.jym.lanhu.fragment.impl;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.CardDetailsActivity;
import com.zys.jym.lanhu.activity.Main2Activity;
import com.zys.jym.lanhu.activity.SearchActivity;
import com.zys.jym.lanhu.activity.SelectP2Activity;
import com.zys.jym.lanhu.activity.ZDActivity;
import com.zys.jym.lanhu.adapter.GroupRvAdapter;
import com.zys.jym.lanhu.bean.MyTopData;
import com.zys.jym.lanhu.bean.TopData;
import com.zys.jym.lanhu.fragment.BaseFragment;
import com.zys.jym.lanhu.httpcallback.MyTopCallback;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.RequestCode;
import com.zys.jym.lanhu.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by Administrator on 2017/1/22.
 */

public class GroupFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener ,GestureDetector.OnGestureListener {

    private GestureDetector mGestureDetector;
    Main2Activity.MyOnTouchListener myOnTouchListener;

    static String TAG="TAG--GroupFragment";
    static SwipeRefreshLayout srf_ly;
    static RecyclerView lv_group;
    static Spinner sp_zx;
    static int page=1;
    static int cataId=0;
    public static GroupRvAdapter  mAdapter;
    static TextView tv_dq;
    static String p = "", p_id = "";
    static String c = "", c_id = "";
    private Button btn_totop;
    private TextView tv_zdql;
    private static String keyWord="";
    private static EditText search_editText;
    private LinearLayout search_linearLayout;
    boolean move= false;// 标记是否滑动
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


    @Override
    public View initView() {
        MyUtils.Loge(TAG,"GroupFragment--initView");
        View view = View.inflate(mActivity, R.layout.fragment_group, null);

        mGestureDetector = new GestureDetector(mActivity,this);
        myOnTouchListener = new Main2Activity.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                boolean result = mGestureDetector.onTouchEvent(ev);
                return result;
            }
        };
        ((Main2Activity) getActivity()).registerMyOnTouchListener(myOnTouchListener);
        
        btn_totop= (Button) view.findViewById(R.id.btn_totop);
        btn_totop.setOnClickListener(this);
        srf_ly= (SwipeRefreshLayout) view.findViewById(R.id.srf_ly);
        //改变加载显示的颜色
        srf_ly.setColorSchemeColors(mContext.getResources().getColor(R.color.main_color));
        srf_ly.setOnRefreshListener(this);
        lv_group= (RecyclerView) view.findViewById(R.id.lv_group);
        lv_group.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
        lv_group.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    if(firstItemPosition >5){
                        btn_totop.setVisibility(View.VISIBLE);
                    }else{
                        btn_totop.setVisibility(View.GONE);
                    }

                }
            }});
        tv_zdql = (TextView) view.findViewById(R.id.tv_zdql);
        tv_zdql.setOnClickListener(this);

        search_editText = (EditText) view.findViewById(R.id.group_search_edittext);
        search_linearLayout = (LinearLayout) view.findViewById(R.id.group_search_linearLayout);
        search_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){//获得焦点
                    search_linearLayout.setVisibility(View.GONE);
                }else{//失去焦点
                    search_linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        search_editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        if(!search_editText.getText().toString().isEmpty()){
                            keyWord = search_editText.getText().toString();
                            page = 1;
                            getData(page);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        sp_zx= (Spinner) view.findViewById(R.id.sp_zx);
        setZXSP(sp_zx);
        tv_dq= (TextView) view.findViewById(R.id.tv_dq);
        tv_dq.setOnClickListener(this);

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
    @Override
    public void initData() {
        lv_group.addOnScrollListener(new RecyclerViewListener());
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
        ArrayAdapter<String> sp_zxadapter=new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item,list);
        sp_zxadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_zx.setAdapter(sp_zxadapter);
        sp_zx.setOnItemSelectedListener(new OnZxItemSelectedListener());
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData(page);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_dq:
                Intent in =new Intent(mActivity, SelectP2Activity.class);
                in.putExtra("inData",0);
                mActivity.startActivity(in);
                break;
            case R.id.btn_totop:
                lv_group.scrollToPosition(0);
                break;
            case R.id.tv_zdql:
                Intent intent = new Intent(mActivity, ZDActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float v, float v1) {
        try{
            if(e1.getY() - e2.getY() > 0){

                ((Main2Activity)getActivity()).changeBottomView(1);
            }else{
                ((Main2Activity)getActivity()).changeBottomView(2);
            }
        }catch (Exception e){

        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
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
        String mcid="";
        if (cataId!=0){
            mcid=cataId+"";
        }
        MyUtils.Loge(TAG, "p=" + mPage + "--cataId="+mcid);
        OkHttpUtils
                .post()
                .url(LHHttpUrl.TOPICLIST_URL)
                .addParams("p", mPage+"")
                .addParams("keyword",keyWord)
                .addParams("cataId",mcid)
                .addParams("provinceId",p_id)
                .addParams("cityId",c_id)
                .addParams("type",1+"")
                .build()
                .execute(new MyTopCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        srf_ly.setRefreshing(false);
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                    }

                    @Override
                    public void onResponse(MyTopData mData) {
                        srf_ly.setRefreshing(false);
                        MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());

                        keyWord = "";
                        search_editText.setText("");

                        if (mData.getErrcode()==1){
                            if (mData.getData().getTopicList().size()!=0){
                                if (mPage == 1) {
                                    mTopDataList.clear();
                                    mTopDataList.addAll(mData.getData().getTopicList());
                                } else {
                                    mTopDataList.addAll(mData.getData().getTopicList());
                                }
                                if (mAdapter != null) {
                                    mAdapter.notifyDataSetChanged();
                                }else {
                                    lv_group.setLayoutManager(new LinearLayoutManager(mContext));
                                    lv_group.setItemAnimator(new DefaultItemAnimator());
                                    mAdapter = new GroupRvAdapter(mActivity, mTopDataList);
                                    lv_group.setAdapter(mAdapter);
                                    setHeader(lv_group);
                                    mAdapter.setOnItemClickListener(new GroupRvAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            TopData mData= mTopDataList.get(position);
                                            Intent in =new Intent(mActivity,CardDetailsActivity.class);
                                            in.putExtra("cardDetail",mData);
                                            mActivity.startActivity(in);
                                        }
                                    } );
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
                            MyUtils.showToast(mContext, mData.getErrmsg());
                        }
                    }
                });


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        TopData mData= (TopData) adapterView.getAdapter().getItem(position);
        Intent in =new Intent(mActivity,CardDetailsActivity.class);
        in.putExtra("cardDetail",mData);
        mActivity.startActivity(in);
    }




    class RecyclerViewListener extends RecyclerView.OnScrollListener{
        //用来标记是否正在向最后一个滑动
        boolean isSlidingToLast = false;
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0) {
                //大于0表示正在滚动
                isSlidingToLast = true;
            } else {
                //小于等于0表示停止或向左滚动
                isSlidingToLast = false;
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            LinearLayoutManager manager = (LinearLayoutManager) lv_group.getLayoutManager();
            // 当不滚动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                //获取最后一个完全显示的ItemPosition
                int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = manager.getItemCount();

                // 判断是否滚动到底部，并且是向右滚动
                if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                    //加载更多功能的代码
                    page++;
                    getData(page);
                }
                // 判断是否滑动到顶部
                if (manager.findFirstCompletelyVisibleItemPosition()==0){
                    btn_totop.setVisibility(View.GONE);
                }else {
                    btn_totop.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private static void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(mContext).inflate(R.layout.item_rcv_headview, view, false);
        mAdapter.setHeaderView(header);
    }
}
