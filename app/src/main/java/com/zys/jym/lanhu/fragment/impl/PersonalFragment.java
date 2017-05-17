package com.zys.jym.lanhu.fragment.impl;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.CardDetailsActivity;
import com.zys.jym.lanhu.activity.ComplaintActivity;
import com.zys.jym.lanhu.activity.Main2Activity;
import com.zys.jym.lanhu.activity.SelectP2Activity;
import com.zys.jym.lanhu.activity.ZDActivity;
import com.zys.jym.lanhu.adapter.PersonalRvAdapter;
import com.zys.jym.lanhu.bean.MyTopData;
import com.zys.jym.lanhu.bean.TopData;
import com.zys.jym.lanhu.fragment.BaseFragment;
import com.zys.jym.lanhu.httpcallback.MyTopCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
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

public class PersonalFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener ,GestureDetector.OnGestureListener{



    private GestureDetector mGestureDetector;
    Main2Activity.MyOnTouchListener myOnTouchListener;



    static String TAG="TAG--PersonalFragment";
    static SwipeRefreshLayout srf_ly;
    static RecyclerView rcv_personal;
    private static Spinner tv_zxfb;
    View view_status;
    static int page=1;
    static int cataId=0;
    private static EditText search_editText;
    private LinearLayout search_linearLayout;
    public static PersonalRvAdapter mAdapter;
    private static TextView tv_dq;
    private static String keyWord="";
    static String p = "", p_id = "";
    static String c = "", c_id = "";
    private TextView tv_zdmp;
    private Button btn_totop;
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
        MyUtils.Loge(TAG,"PersonalFragment--initView");
        View view = View.inflate(getActivity(), R.layout.fragment_personal, null);

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
        search_editText = (EditText) view.findViewById(R.id.search_edittext);
        search_linearLayout = (LinearLayout) view.findViewById(R.id.search_linearLayout);
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


        tv_zdmp = (TextView) view.findViewById(R.id.tv_zdmp);
        tv_zdmp.setOnClickListener(this);

        //改变加载显示的颜色
        srf_ly.setColorSchemeColors(mContext.getResources().getColor(R.color.main_color));
        srf_ly.setOnRefreshListener(this);
        rcv_personal= (RecyclerView) view.findViewById(R.id.rcv_personal);
        rcv_personal.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
        rcv_personal.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
//        tv_zxfb= (TextView) view.findViewById(R.id.tv_zxfb);
        tv_zxfb= (Spinner) view.findViewById(R.id.tv_zxfb);
//        tv_zxfb.setOnClickListener(this);
        tv_zxfb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                MyUtils.showToast(mActivity,"点击时间按");
                page = 1;
                getData(page);
                MyUtils.Loge(TAG, "最新发布被点击");
                return true;
            }
        });
        tv_dq= (TextView) view.findViewById(R.id.tv_dq);
        tv_dq.setOnClickListener(this);
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        initData();
        getData(page);
    }
    @Override
    public void initData() {
        rcv_personal.addOnScrollListener(new RecyclerViewListener());
    }
    private void setZXSP(Spinner sp_zx) {
        List<String> list = new ArrayList<String>();
        list.add("最新发布");
        //list.add("人脉推广");
        //list.add("宝妈互动");
        //list.add("经验分享");
        //list.add("小白学习");
        //list.add("兼职信息");
        //list.add("代理产品");
        //list.add("微商团队");
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
                in.putExtra("inData",4);
                mActivity.startActivity(in);
                break;
            case R.id.btn_totop:
                rcv_personal.scrollToPosition(0);
                break;
//            case R.id.tv_zxfb:
//                page = 1;
//                getData(page);
//                MyUtils.Loge(TAG, "最新发布被点击");
//                break;
            case R.id.tv_zdmp:
                Intent intent = new Intent(mActivity, ZDActivity.class);
                intent.putExtra("type","2");
                startActivity(intent);
                break;
        }
    }





//手势监听
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

    public static void getData(final int mPage){
        MyUtils.Loge("aaaaa","provinceid:"+p_id+"--cityid:"+c_id);
        OkHttpUtils
                .post()
                .url(LHHttpUrl.TOPICLIST_URL)
                .addParams("p", mPage+"")
                .addParams("keyword",keyWord)
                .addParams("cataId","0")
                .addParams("provinceId",p_id)
                .addParams("cityId",c_id)
                .addParams("type",2+"")
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
                        MyUtils.Loge("aaaaa", "请求成功：mData=" + mData.toString());
                        MyUtils.Loge("aaaaa", "请求成功：toplist.size()---" + mData.getData().getTopicList().size());
                        keyWord = "";
                        search_editText.setText("");
                        if(mData.getErrcode()==40001){
                            ActivityUtil.exitAll();
                            ActivityUtil.toLogin(mActivity);
                            return;
                        }
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
                                    rcv_personal.setLayoutManager(new LinearLayoutManager(mContext));
                                    rcv_personal.setItemAnimator(new DefaultItemAnimator());
                                    mAdapter = new PersonalRvAdapter(mActivity, mTopDataList);
                                    rcv_personal.setAdapter(mAdapter);
                                    setHeader(rcv_personal);
                                    mAdapter.setOnItemClickListener(new PersonalRvAdapter.OnItemClickListener() {
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
                                if(mPage==1&&mAdapter!=null){
                                    mTopDataList.clear();
                                    mAdapter.notifyDataSetChanged();
                                }
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
            LinearLayoutManager manager = (LinearLayoutManager) rcv_personal.getLayoutManager();
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
