package com.zys.jym.lanhu.MVP.MVPView;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.zys.jym.lanhu.MVP.HPersonalContract;
import com.zys.jym.lanhu.MVP.MVPPresenter.impl.HPersonalPresenter;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.CardDetailsActivity;
import com.zys.jym.lanhu.activity.SearchActivity;
import com.zys.jym.lanhu.activity.SelectP2Activity;
import com.zys.jym.lanhu.adapter.GroupRvAdapter;
import com.zys.jym.lanhu.bean.MyTopData;
import com.zys.jym.lanhu.bean.TopData;
import com.zys.jym.lanhu.fragment.BaseFragment;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.RequestCode;
import com.zys.jym.lanhu.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/16.
 */

public class HPersonalFragment extends BaseFragment implements HPersonalContract.View, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    String TAG = "TAG--HPersonalFragment";
    static HPersonalContract.Presenter presenter;
    static SwipeRefreshLayout srf_ly;
    static RecyclerView rcv_personal;
    static Spinner sp_zx;
    View view_status;
    static int page = 1;
    static int cataId = 0;
    public static GroupRvAdapter mAdapter;
    static TextView tv_dq;
    static String p = "", p_id = "";
    static String c = "", c_id = "";
    private Button btn_totop;
    TextView tv_seacher;
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
                    presenter.loadData(page,cataId+"",p_id,c_id);
                    break;
                case 0x101:
                    p = "";
                    c = "";
                    p_id = "";
                    c_id = "";
                    tv_dq.setText("全部地区");
                    page=1;
                    presenter.loadData(page,cataId+"",p_id,c_id);
                    break;
            }
        }
    };
    @Override
    public View initView() {
        new HPersonalPresenter(this);
        View view = View.inflate(getActivity(), R.layout.fragment_personal, null);
        btn_totop = (Button) view.findViewById(R.id.btn_totop);
        btn_totop.setOnClickListener(this);
        srf_ly = (SwipeRefreshLayout) view.findViewById(R.id.srf_ly);
        //改变加载显示的颜色
        srf_ly.setColorSchemeColors(mContext.getResources().getColor(R.color.main_color));
        srf_ly.setOnRefreshListener(this);
        rcv_personal = (RecyclerView) view.findViewById(R.id.rcv_personal);
        rcv_personal.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
        sp_zx = (Spinner) view.findViewById(R.id.sp_zx);
        setZXSP(sp_zx);
        tv_dq = (TextView) view.findViewById(R.id.tv_dq);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void initData() {
        rcv_personal.addOnScrollListener(new RecyclerViewListener());
    }

    @Override
    public void setPresenter(HPersonalContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onRefresh() {
        page = 1;
        presenter.loadData(page, cataId + "", p_id, c_id);
    }

    @Override
    public void showProgress() {
        srf_ly.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        srf_ly.setRefreshing(false);
    }


    @Override
    public void showData(MyTopData mData) {
        if (mData.getErrcode() == 1) {
            if (mData.getData().getTopicList().size() != 0) {
                if (page == 1) {
                    mTopDataList.clear();
                    mTopDataList.addAll(mData.getData().getTopicList());
                } else {
                    mTopDataList.addAll(mData.getData().getTopicList());
                }
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                } else {
                    rcv_personal.setLayoutManager(new LinearLayoutManager(mContext));
                    rcv_personal.setItemAnimator(new DefaultItemAnimator());
                    mAdapter = new GroupRvAdapter(mActivity, mTopDataList);
                    rcv_personal.setAdapter(mAdapter);
                    setHeader(rcv_personal);
                    mAdapter.setOnItemClickListener(new GroupRvAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            TopData mData = mTopDataList.get(position);
                            Intent in = new Intent(mActivity, CardDetailsActivity.class);
                            in.putExtra("cardDetail", mData);
                            mActivity.startActivity(in);
                        }
                    });
                }

            } else {
                MyUtils.Loge(TAG, "没有更多数据");
                MyUtils.Loge(TAG, "mpage1=" + page);
                if (mTopDataList.size() != 0 && mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
                page--;
                if (page < 1) {
                    page = 1;
                }
            }
        } else {
//                            gHaveData=false;
            MyUtils.Loge(TAG, "page=" + page);
            if (page > 1) {
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dq:
                Intent in = new Intent(mActivity, SelectP2Activity.class);
                in.putExtra("inData", 4);
                mActivity.startActivity(in);
                break;
            case R.id.btn_totop:
                rcv_personal.scrollToPosition(0);
                btn_totop.setVisibility(View.GONE);
                break;
        }
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
        ArrayAdapter<String> sp_zxadapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, list);
        sp_zxadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_zx.setAdapter(sp_zxadapter);
        sp_zx.setOnItemSelectedListener(new OnZxItemSelectedListener());
    }

    class RecyclerViewListener extends RecyclerView.OnScrollListener {
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
                    presenter.loadData(page, cataId + "", p_id, c_id);
                }
                // 判断是否滑动到顶部
                if (manager.findFirstCompletelyVisibleItemPosition() == 0) {
                    btn_totop.setVisibility(View.GONE);
                } else {
                    btn_totop.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    class OnZxItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
            cataId = i;
            page = 1;
            presenter.loadData(page, cataId + "", p_id, c_id);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    private static void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(mContext).inflate(R.layout.item_rcv_headview, view, false);
        mAdapter.setHeaderView(header);
    }
}
