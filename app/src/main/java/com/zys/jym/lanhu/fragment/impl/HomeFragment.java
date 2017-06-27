package com.zys.jym.lanhu.fragment.impl;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.ComplaintActivity;
import com.zys.jym.lanhu.activity.DHZDActivity;
import com.zys.jym.lanhu.activity.OpenVipActivity;
import com.zys.jym.lanhu.activity.SelectP2Activity;
import com.zys.jym.lanhu.activity.UseInstructions2Activity;
import com.zys.jym.lanhu.adapter.HomeRvAdapter;
import com.zys.jym.lanhu.bean.HomeListData;
import com.zys.jym.lanhu.bean.HomeZDData;
import com.zys.jym.lanhu.bean.IsAddBean;
import com.zys.jym.lanhu.bean.KeepPhoneData;
import com.zys.jym.lanhu.bean.UpdateLocationData;
import com.zys.jym.lanhu.fragment.BaseFragment;
import com.zys.jym.lanhu.httpcallback.HaddCallback;
import com.zys.jym.lanhu.httpcallback.HomeListCallback;
import com.zys.jym.lanhu.httpcallback.HomeZDCallback;
import com.zys.jym.lanhu.httpcallback.KeepPhoneCallback;
import com.zys.jym.lanhu.httpcallback.UpdateLocationCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.DialogOkUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.LXRUtil;
import com.zys.jym.lanhu.utils.MySharedPrefrencesUtil;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.RequestCode;
import com.zys.jym.lanhu.utils.SPrefUtil;
import com.zys.jym.lanhu.utils.TimeUtil;
import com.zys.jym.lanhu.view.RecycleViewDivider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/1/22.
 */


public class HomeFragment extends BaseFragment implements AMapLocationListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static String TAG = "TAG--HomeFragment";
    private static Dialog pdialog;
    private static String isswitch;
    private static String url;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    // 声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private LinearLayout ll_home_selectview;
    private Spinner sp_home_zx;
    private static TextView tv_home_dq;
    private static SwipeRefreshLayout home_srf_ly;
    private static RecyclerView rcv_home_personal;
    private static Button btn_home_totop;
    private static TextView zhiding_txl_tv;
    private static ImageView iv_home_jf;
    private TextView iv_home_im;
    private static TextView tv_location;
    static int page = 1;
    private static TextView tv_home_addfen;
    private static HomeRvAdapter mAdapter;
    static List<HomeListData.DataBean.ContactListBean> contactList = new ArrayList<>();
    private static List<String> addPhoneList = new ArrayList<>();
    private static List<String> addIDList = new ArrayList<>();
    private static List<String> addNameList = new ArrayList<>();
    private static StringBuilder sb = new StringBuilder();
    private static TextView tv_home_delete;
    static String p = "", p_id = "";
    static String c = "", c_id = "";
    static int num = 1;
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RequestCode.TO_PC_ACTIVITY:
                    p = msg.getData().getString("p");
                    c = msg.getData().getString("c");
                    p_id = msg.getData().getString("p_id");
                    c_id = msg.getData().getString("c_id");
                    tv_location.setText(c);
                    updateLocation(p, c);
                    p = "";
                    c = "";
                    break;
                case 0x101:
                    p = "";
                    c = "";
                    p_id = "";
                    c_id = "";
                    tv_location.setText("--");
                    page = 1;
                    break;
            }
        }
    };

    public static Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RequestCode.TO_PC_ACTIVITY:
                    p = msg.getData().getString("p");
                    c = msg.getData().getString("c");
                    p_id = msg.getData().getString("p_id");
                    c_id = msg.getData().getString("c_id");
                    tv_home_dq.setText(p + "-" + c);
                    page = 1;
                    getData2(page, p, c);
                    break;
                case 0x101:
                    p = "";
                    c = "";
                    p_id = "";
                    c_id = "";
                    tv_home_dq.setText("全部地区");
                    page = 1;
                    break;
            }
        }
    };

    public static Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MyUtils.dismssDialog();
//                    tv_home_delete.setText("清除数据("+addPhoneList.size()+")");
                    MySharedPrefrencesUtil.setParam(mActivity, "my_phone_type", "2");
                    String currentDate = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
                    String hcDate = (String) MySharedPrefrencesUtil.getParam(mActivity, "CLEAN_DATE", "");
                    MySharedPrefrencesUtil.setParam(mActivity, "CLEAN_DATE", currentDate);
                    DialogOkUtil.show_OK_NO_Dialog(mActivity, "加粉成功,请打开微信,稍等片刻,等待新的好友出现后,返回蓝狐继续加粉.", new DialogOkUtil.On_OK_N0_ClickListener() {
                        @Override
                        public void onOk() {
                            try {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setComponent(cmp);
                                mActivity.startActivity(intent);
                            } catch (Exception e) {
                                // TODO: handle exception
                                MyUtils.showToast(mContext, "检查到您手机没有安装微信，请安装后使用该功能");
                            }
                        }

                        @Override
                        public void onNo() {
                        }
                    }, "邀请好友,送三天被加,送置顶").show();
                    for (int i = 0; i < addPhoneList.size(); i++) {
                        sb.append(addPhoneList.get(i) + "^");
                        MyUtils.Loge(TAG, "添加的手机号：" + addPhoneList.get(i));
                        MySharedPrefrencesUtil.setParam(mActivity, "phone_data", MySharedPrefrencesUtil.getParam(app, "phone_data", "") + sb.toString());
                        mAdapter.setData(contactList);
//                        String str = MySharedPrefrencesUtil.getParam(mContext, "phone_data", "").toString();
//                        String[] temp = str.split("\\^");
//                        if (temp.length > 0 && !temp[0].isEmpty()) {
//                            tv_home_delete.setText("清除数据" + "(" + temp.length + ")");
//                        }
                        sb.delete(0, sb.length());
                    }
                    //addPhoneList.clear();
                    addNameList.clear();
                    String str = MySharedPrefrencesUtil.getParam(mActivity, "phone_data", "").toString();
                    String[] temp = str.split("\\^");
                    MyUtils.Loge("abcdef", "缓存中电话号码2：" + temp.length);
                    if (temp.length > 0) {
                        tv_home_delete.setText("清除数据(" + temp.length + ")");
                    }
                    tv_home_addfen.setText("立即加粉");
                    MyUtils.Loge(TAG, "缓存中的电话号码：" + MySharedPrefrencesUtil.getParam(mActivity, "phone_data", ""));
                    setServerData();
                    break;
                case 2:
                    MyUtils.showToast(mContext, "批量删除成功");
//                    MySharedPrefrencesUtil.setParam(mActivity, "phone_data", "");
                    tv_home_delete.setText("清除数据");
                    String str1 = MySharedPrefrencesUtil.getParam(mActivity, "phone_data", "").toString();
                    String[] temp1 = str1.split("\\^");
                    MyUtils.Loge("abcdef", "删除--缓存中电话号码2：" + temp1.length);
//                    MyUtils.Loge("abcdef", "删除--缓存中的电话号码：" + MySharedPrefrencesUtil.getParam(mActivity, "phone_data", ""));
                    break;
                case 3:
                    //添加通讯录拒绝权限回调
                    MyUtils.dismssDialog();
                    MyUtils.showToast(mContext, "请打开手机设置，权限管理，允许蓝狐微商读取、写入和删除联系人信息后再使用立即加粉");
//                    permissionDialog();
                    break;
            }
        }
    };
    public static Handler handler3 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x11:
                    String str = MySharedPrefrencesUtil.getParam(mActivity, "phone_data", "").toString();
                    String[] temp = str.split("\\^");
                    MyUtils.Loge("abcdef", "添加--缓存中电话号码：" + temp.length);
                    MyUtils.Loge("abcdef", "添加--缓存中电话号码SHI   ：" + temp[0]);
                    if (temp.length > 1) {
                        tv_home_delete.setText("清除数据(" + temp.length + ")");
                    } else {
                        tv_home_delete.setText("清除数据");
                    }
                    break;
            }
        }
    };


    private int length_phone;
    private PopupWindow popWindow;
    private LinearLayout poplinear;

    @Override
    public View initView() {
        MyUtils.Loge(TAG, "HomeFragment--initView");
        View view = View.inflate(getActivity(), R.layout.fragment_home, null);
        ll_home_selectview = (LinearLayout) view.findViewById(R.id.ll_home_selectview);
//        sp_home_zx = (Spinner) view.findViewById(R.id.sp_home_zx);
        tv_home_dq = (TextView) view.findViewById(R.id.tv_home_dq);
        tv_home_dq.setOnClickListener(this);
        home_srf_ly = (SwipeRefreshLayout) view.findViewById(R.id.home_srf_ly);
        //改变加载显示的颜色
        iv_home_im = (TextView) view.findViewById(R.id.iv_home_im);
        iv_home_im.setOnClickListener(this);
        home_srf_ly.setColorSchemeColors(mContext.getResources().getColor(R.color.main_color));
        home_srf_ly.setOnRefreshListener(this);
        zhiding_txl_tv = (TextView) view.findViewById(R.id.zhiding_txl_tv);
        zhiding_txl_tv.setOnClickListener(this);

        rcv_home_personal = (RecyclerView) view.findViewById(R.id.rcv_home_personal);
        rcv_home_personal.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
        rcv_home_personal.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
                    if (firstItemPosition > 5) {
                        btn_home_totop.setVisibility(View.VISIBLE);
                    } else {
                        btn_home_totop.setVisibility(View.GONE);
                    }

                }
            }
        });
        btn_home_totop = (Button) view.findViewById(R.id.btn_home_totop);
        btn_home_totop.setOnClickListener(this);
        iv_home_jf = (ImageView) view.findViewById(R.id.iv_home_jf);
        iv_home_jf.setOnClickListener(this);
        tv_location = (TextView) view.findViewById(R.id.tv_home_location);
        tv_location.setOnClickListener(this);
        tv_home_addfen = (TextView) view.findViewById(R.id.tv_home_addfen);
        tv_home_addfen.setOnClickListener(this);
        tv_home_delete = (TextView) view.findViewById(R.id.tv_home_delete);
        tv_home_delete.setOnClickListener(this);
        return view;
    }


    private void isZDEnd() {
        if (System.currentTimeMillis() > (Long) (MySharedPrefrencesUtil.getParam(mContext, "zd_time", 0l))) {
            zhiding_txl_tv.setText("置顶通讯录");
            zhiding_txl_tv.setClickable(true);
        } else {
            zhiding_txl_tv.setText(TimeUtil.formatDuring((long) MySharedPrefrencesUtil.getParam(mContext, "zd_time", 0l)) + " 结束置顶");
            zhiding_txl_tv.setClickable(false);
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {//显示
            MyUtils.Loge(TAG, "HomeFragment--显示");
        } else {
            MyUtils.Loge(TAG, "HomeFragment--隐藏");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        isZDEnd();
    }


    /**
     * 高德地图定位
     */
    private void initLocation() {
        //初始化定位参数  
        mLocationOption = new AMapLocationClientOption();
        //初始化定位
        mLocationClient = new AMapLocationClient(mContext);
        //设置定位监听  
        mLocationClient.setLocationListener(this);
        MyUtils.Loge(TAG, "HomeFragment--onResume");
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式  
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms  
        mLocationOption.setInterval(1000);
        //设置是否只定位一次,默认为false  
        mLocationOption.setOnceLocation(true);
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。  
        //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。  
        mLocationOption.setOnceLocationLatest(true);
        //设置定位参数  
        mLocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，  
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求  
        // 在定位结束后，在合适的生命周期调用onDestroy()方法  
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除  
        //启动定位  
        mLocationClient.startLocation();
    }


    public void initData() {
        MyUtils.Loge(TAG, "HomeFragment--initData");

//        if (app != null && app.getUser() != null && !TextUtils.isEmpty(app.getUser().getLogin_token())) {
        if (!TextUtils.isEmpty(SPrefUtil.getString(mActivity, "TOKEN", ""))) {
            Message msg = Message.obtain();
            msg.what = 0x11;
            handler3.sendMessage(msg);
            getData2(page, p, c);
            rcv_home_personal.addOnScrollListener(new RecyclerViewListener());

        } else {
            MyUtils.showToast(mContext, "请先登录");
        }
        initLocation();//定位
    }

    /**
     * 高德定位回调
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (app.isB_location()) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    Double latitude = aMapLocation.getLatitude();
                    Double longitude = aMapLocation.getLongitude();
                    MyUtils.Loge(TAG, "latitude:" + latitude);
                    MyUtils.Loge(TAG, "longitude:" + longitude);
                    MyUtils.Loge(TAG, "地址：" + aMapLocation.getCountry() + aMapLocation.getProvince() + aMapLocation.getCity());
                    tv_location.setText(aMapLocation.getCity() + "");
                    updateLocation(aMapLocation.getProvince(), aMapLocation.getCity());
                } else {
                    MyUtils.Loge("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }

            }
            app.setB_location(false);
        } else {
            MyUtils.Loge(TAG, "b_location:" + app.isB_location());
        }
    }

    /**
     * 告知服务器，保存通讯录成功
     */
    public static void setServerData() {
        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("[");
        for (int i = 0; i < addIDList.size(); i++) {
            if (i == addIDList.size() - 1) {
                stringBuilder.append(addIDList.get(i) + "");
            } else {
                stringBuilder.append(addIDList.get(i) + ",");
            }
        }
        MyUtils.Loge(TAG, "stringBuilder：" + stringBuilder.toString());
//        if (app != null && app.getUser() != null && !TextUtils.isEmpty(app.getUser().getLogin_token())) {
        OkHttpUtils
                .post()
                .url(LHHttpUrl.KEEP_PHONE_URL)
//                .url("http://139.224.194.108/lh/Api/Contact/getPost")
                .addParams("arrexp", stringBuilder.toString())
//                    .addParams("login_token", app.getUser().getLogin_token())   //SPrefUtil.getString(mActivity,"TOKEN","")
                .addParams("login_token", SPrefUtil.getString(mActivity, "TOKEN", ""))
                .build()
                .execute(new KeepPhoneCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                    }

                    @Override
                    public void onResponse(KeepPhoneData keepPhoneData) {
                        MyUtils.Loge(TAG, keepPhoneData.getErrmsg());
                        if (keepPhoneData.getErrcode() == 40001) {
                            ActivityUtil.exitAll();
                            ActivityUtil.toLogin(mActivity);
                            return;
                        }
                        if (keepPhoneData.getErrcode() == 1) {
                            MyUtils.Loge(TAG, keepPhoneData.getErrmsg());
                            page = 1;
                            contactList.clear();
                            addPhoneList.clear();
                            getData2(page, p, c);
                        }
                    }
                });
//        }
    }

    /**
     * 获取列表数据
     *
     * @param mPage
     */
    public static void getData2(final int mPage, final String p, final String c) {
//        MyUtils.Loge(TAG,"token::"+app.getUser().getLogin_token());
//        MyUtils.Loge(TAG, "token::" + app.toString());
//        MyUtils.Loge(TAG, "token::" + app.getUser().toString());
//        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
        OkHttpUtils
                .post()
                .url(LHHttpUrl.HOME_LIST_URL)
                .addParams("p", mPage + "")
                .addParams("province", p)
                .addParams("city", c)
                .addParams("pageSize", "30")
//                    .addParams("login_token", app.getUser().getLogin_token())
                .addParams("login_token", SPrefUtil.getString(mActivity, "TOKEN", ""))
                .build()
                .execute(new HomeListCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.getMessage());
                        home_srf_ly.setRefreshing(false);
                    }

                    @Override
                    public void onResponse(final HomeListData homeListData) {
                        MyUtils.Loge(TAG, "成功：homeListData:" + homeListData.getErrmsg());
                        home_srf_ly.setRefreshing(false);
                        if (homeListData.getErrcode() == 40001) {
                            ActivityUtil.exitAll();
                            ActivityUtil.toLogin(mActivity);
                            return;
                        }
                        if (homeListData.getErrcode() == 1) {
                            isswitch = homeListData.getData().getIsswitch();
                            url = homeListData.getData().getStep();
                            if (isswitch.equals("1")) {
                                //被动加粉开启
                                iv_home_jf.setImageResource(R.mipmap.open_on_light);
                            } else {
                                iv_home_jf.setImageResource(R.mipmap.open_on);
                            }
                            if (mPage == 1) {
                                contactList.clear();
                                addPhoneList.clear();
                                addNameList.clear();
                                if (MySharedPrefrencesUtil.getParam(mActivity, "my_phone_type", "").equals("1")) {
                                    for (int i = 0; i < 5; i++) {
                                        addPhoneList.add(homeListData.getData().getContactList().get(i).getPhone());
                                        addIDList.add(homeListData.getData().getContactList().get(i).getId());
                                        addNameList.add(homeListData.getData().getContactList().get(i).getNickname());
                                        homeListData.getData().getContactList().get(i).setType(2);
                                    }
                                }
                            }
                            tv_home_addfen.setText("立即加粉(" + addPhoneList.size() + ")");
//                            tv_home_delete.setText("清除数据(" + addPhoneList.size() + ")");
                            if (homeListData.getData().getContactList().size() != 0) {//表示有数据
                                if (mPage == 1) {
                                    contactList.clear();
                                }
                                contactList.addAll(homeListData.getData().getContactList());
                                if (mAdapter == null) {
                                    rcv_home_personal.setLayoutManager(new LinearLayoutManager(mContext));
                                    rcv_home_personal.setItemAnimator(new DefaultItemAnimator());
                                    mAdapter = new HomeRvAdapter(mContext);
                                    mAdapter.setData(contactList);
                                    rcv_home_personal.setAdapter(mAdapter);
                                    setHeader(rcv_home_personal);
                                    mAdapter.setOnItemClickListener(new HomeRvAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            MyUtils.Loge(TAG, "点击了：" + position + "条");
                                            if (contactList.get(position).getType() == 2) {
                                                contactList.get(position).setType(1);
                                                mAdapter.setData(contactList);
                                                addPhoneList.remove(contactList.get(position).getPhone());
                                                addNameList.remove(contactList.get(position).getNickname());
                                                tv_home_addfen.setText("立即加粉(" + addPhoneList.size() + ")");
//                                                tv_home_delete.setText("清除数据(" + addPhoneList.size() + ")");
                                                return;
                                            }
                                            if (addPhoneList.size() < 5 && contactList.get(position).getType() == 1) {
                                                contactList.get(position).setType(2);
                                                mAdapter.setData(contactList);
                                                addPhoneList.add(contactList.get(position).getPhone());
                                                addNameList.add(contactList.get(position).getNickname());
                                                tv_home_addfen.setText("立即加粉(" + addPhoneList.size() + ")");
//                                                tv_home_delete.setText("清除数据(" + addPhoneList.size() + ")");
                                                return;
                                            }else {
                                                MyUtils.showToast(mActivity,"为了保证加粉数量, 每次最多勾选5个名片, 分多次加, 加粉更多哦~");
                                            }


                                        }
                                    });

                                } else {
                                    mAdapter.setData(contactList);
                                }
                            } else {
                                MyUtils.Loge(TAG, "没有更多数据");
                                MyUtils.Loge(TAG, "mpage1=" + mPage);
                                page--;
                                if (page < 1) {
                                    page = 1;
//                                    mAdapter.notifyDataSetChanged();
                                    mAdapter.setData(contactList);
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
//                                lv_group.setVisibility(View.INVISIBLE);
                            }
                            MyUtils.showToast(mContext, homeListData.getErrmsg());
                        }

                    }
                });
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.unRegisterLocationListener(this);
        mLocationClient = null;
    }

    /**
     * 保存手机号
     */
    private boolean toSavePhone(final List<String> mTopDataList) {
        String str = MySharedPrefrencesUtil.getParam(mActivity, "phone_data", "").toString();
        String[] temp = str.split("\\^");
        //判断是不是VIP

        if (app.getPurseData() != null && Long.parseLong(app.getPurseData().getViprest()) > 0) {//如果是
            if (temp.length >= 120) {
                MyUtils.showToast(mActivity, "您今天已经添加了120条，请明天再添加吧");
                return false;
            } else {
                if (mTopDataList.size() <= (120 - temp.length)) {
                    length_phone = mTopDataList.size();
                    addMyList();
                } else {
                    length_phone = 120 - temp.length;
                    addMyList();
                }
            }
        } else {
            if (temp.length >= 60) {
                MyUtils.showToast(mActivity, "您今天已经添加了60条，请明天再添加吧");
                return false;
            } else {
                if (mTopDataList.size() <= (60 - temp.length)) {
                    length_phone = mTopDataList.size();
                    addMyList();
                } else {
                    length_phone = 60 - temp.length;
                    MyUtils.showToast(mActivity, "添加选中的" + length_phone + "条");
                    addMyList();
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_home_jf:
//                if (app != null && app.getUser() != null && !TextUtils.isEmpty(app.getUser().getLogin_token()))
                if (!TextUtils.isEmpty(SPrefUtil.getString(mActivity, "TOKEN", ""))) {
                    //判断是不是VIP
                    if (app.getPurseData() != null && Long.parseLong(app.getPurseData().getViprest()) > 0) {//如果是
                        if (MySharedPrefrencesUtil.getParam(mContext, "isAllowAdd", "").equals("open")) {
                            iv_home_jf.setImageResource(R.mipmap.open_on);
                            isAdd("0");
                        } else {
                            DialogOkUtil.show_OK_NO_Dialog(getActivity(), "您是VIP用户，享有全天24小时被动加粉", new DialogOkUtil.On_OK_N0_ClickListener() {
                                @Override
                                public void onOk() {
                                    iv_home_jf.setImageResource(R.mipmap.open_on_light);
                                    isAdd("1");
                                }

                                @Override
                                public void onNo() {
                                }
                            }, "").show();
                        }
                    } else {//如果不是
                        DialogOkUtil.show_OK_NO_Dialog(getActivity(), "邀请好友注册,您和好友各获得三天被动加粉,请到个人中心发起邀请,点击确定.开通VIP解锁被动加粉.", new DialogOkUtil.On_OK_N0_ClickListener() {
                            @Override
                            public void onOk() {
                                Intent in = new Intent(mContext, OpenVipActivity.class);
                                startActivity(in);
                            }

                            @Override
                            public void onNo() {

                            }
                        }, "邀请好友,送三天被加,送置顶").show();
                    }
                } else {
                    MyUtils.showToast(mContext, "请先登录");
                }
                break;
            case R.id.tv_home_addfen:
                if (!toSavePhone(addPhoneList)) {
                    break;
                }
                if (addPhoneList.size() == 0) {
                    MyUtils.dismssDialog();
                    MyUtils.showToast(mContext, "请您至少勾选一个名片");
                }
                break;
            case R.id.tv_home_location:
                Intent intent = new Intent(getActivity(), SelectP2Activity.class);
                intent.putExtra("inData", 3);
                startActivity(intent);
                break;
            case R.id.tv_home_delete:
                deletePhone();
                break;
            case R.id.btn_home_totop:
                rcv_home_personal.scrollToPosition(0);
                break;
            case R.id.tv_home_dq:
                Intent in = new Intent(mActivity, SelectP2Activity.class);
                in.putExtra("inData", 5);
                mActivity.startActivity(in);
                break;
            case R.id.iv_home_im:
                if (!TextUtils.isEmpty(url)) {
                    Intent intent1 = new Intent(mActivity, UseInstructions2Activity.class);
                    intent1.putExtra("url", url);
                    startActivity(intent1);
                } else {
                    MyUtils.showToast(mActivity, "请刷新界面后再试");
                }
                break;
            case R.id.zhiding_txl_tv://置顶通讯录
                if (app.getPurseData() != null) {
                    if (Integer.parseInt(app.getPurseData().getTopbalance()) >= 0) {
                        MyUtils.Loge("AAA", TimeUtil.formatDuring(System.currentTimeMillis()));
                        if (MyUtils.Str2Int(app.getPurseData().getTopbalance()) == 0) {
                            DialogOkUtil.show_Ok_Dialog(mActivity, "好友通过您的邀请码注册蓝狐,您和好友各获得半小时置顶,在个人中心发起邀请,点击确定,直接兑换置顶.", new DialogOkUtil.On_OK_ClickListener() {
                                @Override
                                public void onOk() {
                                    Intent in = new Intent(mActivity, DHZDActivity.class);
                                    mActivity.startActivity(in);
                                }
                            }, "邀请好友,送三天被加,送置顶").show();
                        } else {
                            showZDDialog();
                        }

                    } else {
                        DialogOkUtil.show_Ok_Dialog(mActivity, "好友通过您的邀请码注册蓝狐,您和好友各获得半小时置顶,在个人中心发起邀请,点击确定,直接兑换置顶.", new DialogOkUtil.On_OK_ClickListener() {
                            @Override
                            public void onOk() {
                                Intent in = new Intent(mActivity, DHZDActivity.class);
                                mActivity.startActivity(in);
                            }
                        }, "邀请好友,送三天被加,送置顶").show();
//                        Intent in9 = new Intent(mContext, DHZDActivity.class);
//                        startActivity(in9);
                    }
                }
                break;
        }
    }

    static TextView tv_num;
    static TextView tv_jian;
    static TextView tv_jia;
    static TextView tv_finsh;

    /**
     * 删除通讯录对话框
     */
    private void deletePhone() {
        View view = View.inflate(mContext, R.layout.pop_window_sure, null);
        popWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        poplinear = (LinearLayout) view.findViewById(R.id.pop_linear);
        TextView sureTV = (TextView) view.findViewById(R.id.pop_window_sure);
        sureTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMyList();
                popWindow.dismiss();
            }
        });
        TextView cancleTV = (TextView) view.findViewById(R.id.pop_window_cancle);
        cancleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);
        popWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popWindow.showAtLocation(poplinear, Gravity.CENTER, 0, 0);
    }

    /**
     * 权限提示
     */
    private static void permissionDialog() {
        View view = View.inflate(mContext, R.layout.pop_window_addpromission, null);
        final PopupWindow popWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        LinearLayout poplinear = (LinearLayout) view.findViewById(R.id.pop_linear);
        TextView sureTV = (TextView) view.findViewById(R.id.pop_window_sure1);//pop_window_sure
        sureTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);
        popWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popWindow.showAtLocation(poplinear, Gravity.CENTER, 0, 0);
    }


    /**
     * 置顶对话框
     */
    private static void showZDDialog() {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.view_zdsj_dialog, null);// 得到加载view
        Button btn_ok = (Button) v.findViewById(R.id.btn_ok);
        tv_num = (TextView) v.findViewById(R.id.tv_num);
        tv_num.setText(num + "");
        tv_jian = (TextView) v.findViewById(R.id.tv_jian);
        tv_jia = (TextView) v.findViewById(R.id.tv_jia);
        tv_finsh = (TextView) v.findViewById(R.id.tv_finsh);
        tv_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (num <= Integer.parseInt(app.getPurseData().getTopbalance())) {

                    if (num < 10) {
                        num++;
                        tv_num.setText(num + "");
                    } else {
                        MyUtils.showToast(mContext, "一次置顶不能超过十次");
                    }
                } else {
                    MyUtils.showToast(mContext, "您的置顶次数不够，请去兑换");
                }

            }
        });
        tv_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (num > 0) {
                    num--;
                    tv_num.setText(num + "");
                }
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zdtxl();
                pdialog.dismiss();
                pdialog = null;
            }
        });

        tv_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdialog.dismiss();
                pdialog = null;
            }
        });
        if (pdialog != null) {
            pdialog = null;
        }
        pdialog = new Dialog(mContext, R.style.custom_dialog);// 创建自定义样式dialog
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.setCancelable(false);
        pdialog.setContentView(v);

        if (!pdialog.isShowing()) {
            pdialog.show();
        }
    }

    /**
     * 置顶通讯录
     */
    private static void zdtxl() {
//        if (app != null && app.getUser() != null && !TextUtils.isEmpty(app.getUser().getLogin_token())) {
        OkHttpUtils
                .post()
                .url(LHHttpUrl.HOME_UP_PHONE_URL)
                .addParams("num", num + "")
//                    .addParams("login_token", app.getUser().getLogin_token())
                .addParams("login_token", SPrefUtil.getString(mActivity, "TOKEN", ""))
                .build()
                .execute(new HomeZDCallback() {

                    @Override
                    public void onError(Call call, Exception e) {
                        MyUtils.showToast(mContext, "连接服务器失败，请稍后再试");
                    }

                    @Override
                    public void onResponse(HomeZDData homeZDData) {
                        if (homeZDData.getErrcode() == 40001) {
                            ActivityUtil.exitAll();
                            ActivityUtil.toLogin(mActivity);
                            return;
                        }
                        if (homeZDData.getErrcode() == 1) {
                            Long time = num * 30 * 60 * 1000l;
                            MySharedPrefrencesUtil.setParam(mContext, "zd_time", System.currentTimeMillis() + time);
                            MyUtils.Loge("AAA", TimeUtil.formatDuring(System.currentTimeMillis()));
                            zhiding_txl_tv.setText(TimeUtil.formatDuring(System.currentTimeMillis() + time) + " 结束置顶");
                            zhiding_txl_tv.setClickable(false);
                        } else {
                            MyUtils.showToast(mActivity, homeZDData.getErrmsg());
//                            MyUtils.showToast(mActivity,"置顶失败");
                        }
                    }
                });
//        }

    }

    /**
     * 从通讯录删除
     */
    private void deleteMyList() {
//        final String[] temp = MySharedPrefrencesUtil.getParam(getActivity(), "phone_data", "").toString().split("\\^");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 0x2);
                return;
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LXRUtil.deleteContacts(mActivity);
                    }
                }).start();
            }
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LXRUtil.deleteContacts(mActivity);
                }
            }).start();
        }


    }

    /**
     * 添加到通讯录
     */
    private void addMyList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.v("stones", "greater than android m");
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.v("stones", "permission not granted");
                requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, 0x1);
                return;
            } else {
                Log.v("stones", "permission is granted");
                Log.v("stones", "正在添加通讯录");
                MyUtils.showDialog(getActivity(), "正在添加通讯录...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < addPhoneList.size(); i++) {
                            MyUtils.Loge(TAG, "选中的电话号码：" + addPhoneList.get(i));
                            MyUtils.Loge(TAG, "选中的用户名：" + addNameList.get(i));
                            LXRUtil.addContacts(mActivity, addNameList.get(i), addPhoneList.get(i), false, addNameList.size(), 1);
                        }
                    }
                }).start();
            }
        } else {
            //添加通讯录
            Log.v("stones", "lesster than android m");
            MyUtils.showDialog(getActivity(), "正在添加通讯录...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < addPhoneList.size(); i++) {
//                        MyUtils.Loge(TAG, "选中的电话号码：" + addPhoneList.get(i));
//                        MyUtils.Loge(TAG, "选中的用户名：" + addNameList.get(i));
                        LXRUtil.addContacts(mActivity, addNameList.get(i), addPhoneList.get(i), false, addNameList.size(), 1);
                    }
                }
            }).start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0x1:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.v("stones", "权限回调--获取权限失败");
                    Toast.makeText(getActivity(), "请打开手机设置，权限管理，允许蓝狐微商读取、写入和删除联系人信息后再使用立即加粉", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "权限获取成功", Toast.LENGTH_SHORT).show();
                    Log.v("stones", "权限回调--获取权限成功");
                    MyUtils.showDialog(getActivity(), "正在添加通讯录...");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < addPhoneList.size(); i++) {
                                MyUtils.Loge(TAG, "选中的电话号码：" + addPhoneList.get(i));
                                MyUtils.Loge(TAG, "选中的用户名：" + addNameList.get(i));
                                LXRUtil.addContacts(mActivity, addNameList.get(i), addPhoneList.get(i), false, addNameList.size(), 1);
                            }
                        }
                    }).start();
                }
                break;
            case 0x2:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "请打开手机设置，权限管理，允许蓝狐微商读取、写入和删除联系人信息后再使用立即加粉", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LXRUtil.deleteContacts(mActivity);
                        }
                    }).start();
                }
                break;
        }
    }

    /**
     * 是否允许被加粉
     */
    private void isAdd(final String type) {
//        MyUtils.Loge(TAG, "传的参数----token::" + app.getUser().getLogin_token() + "--type:1");
//        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
        OkHttpUtils
                .post()
                .url(LHHttpUrl.HOME_ADD_URL)
                .addParams("type", type)
//                    .addParams("login_token", app.getUser().getLogin_token())
                .addParams("login_token", SPrefUtil.getString(mActivity, "TOKEN", ""))
                .build()
                .execute(new HaddCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.getMessage());
                    }

                    @Override
                    public void onResponse(IsAddBean isAddBean) {
                        if (isAddBean.getErrcode() == 40001) {
                            ActivityUtil.exitAll();
                            ActivityUtil.toLogin(mActivity);
                            return;
                        }
                        if (isAddBean.getErrcode() == 1) {
                            if (Integer.parseInt(type) == 0) {
                                MySharedPrefrencesUtil.setParam(mContext, "isAllowAdd", "close");
                                MyUtils.Loge(TAG, "返回成功数据：" + isAddBean.getErrmsg());
                            } else {
                                MySharedPrefrencesUtil.setParam(mContext, "isAllowAdd", "open");
                                MyUtils.Loge(TAG, "返回成功数据：" + isAddBean.getErrmsg());
                            }

                        }
                        if (isAddBean.getErrcode() == 0) {
                            if (Integer.parseInt(type) == 0) {
                                MySharedPrefrencesUtil.setParam(mContext, "isAllowAdd", "close");
                                MyUtils.Loge(TAG, "返回成功数据：" + isAddBean.getErrmsg());
                            } else {
                                MySharedPrefrencesUtil.setParam(mContext, "isAllowAdd", "open");
                                MyUtils.Loge(TAG, "返回成功数据：" + isAddBean.getErrmsg());
                            }
                        }

                    }
                });
//        }
    }

    /**
     * 更新用户地区
     */
    private static void updateLocation(String province, String city) {
//        if (app != null && app.getUser() != null && !TextUtils.isEmpty(app.getUser().getLogin_token())) {
        if (!TextUtils.isEmpty(SPrefUtil.getString(mActivity, "TOKEN", ""))) {
            MyUtils.Loge(TAG, "updateLocation--省：" + province);
            MyUtils.Loge(TAG, "updateLocation--市：" + city);
            OkHttpUtils
                    .post()
                    .url(LHHttpUrl.UPDATA_LOCATION_URL)
//                    .addParams("login_token", app.getUser().getLogin_token())
                    .addParams("login_token", SPrefUtil.getString(mActivity, "TOKEN", ""))
                    .addParams("province", province)
                    .addParams("city", city)
                    .build()
                    .execute(new UpdateLocationCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.Loge(TAG, "e:" + e.getMessage());
                        }

                        @Override
                        public void onResponse(UpdateLocationData updateLocationData) {

                            MyUtils.Loge(TAG, "更新用户成功updateLocationData：" + updateLocationData.toString());
                        }
                    });
        } else {
            MyUtils.showToast(mContext, "请先登录");
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
//        if (app != null && app.getUser() != null && !TextUtils.isEmpty(app.getUser().getLogin_token())) {
        if (!TextUtils.isEmpty(SPrefUtil.getString(mActivity, "TOKEN", ""))) {
            getData2(page, p, c);
            rcv_home_personal.addOnScrollListener(new RecyclerViewListener());
        } else {
            MyUtils.showToast(mContext, "请先登录");
        }
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
            LinearLayoutManager manager = (LinearLayoutManager) rcv_home_personal.getLayoutManager();
            // 当不滚动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                //获取最后一个完全显示的ItemPosition
                int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = manager.getItemCount();

                // 判断是否滚动到底部，并且是向右滚动
                if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                    //加载更多功能的代码
                    page++;
//                    if (app != null && app.getUser() != null && !TextUtils.isEmpty(app.getUser().getLogin_token())) {
                    if (!TextUtils.isEmpty(SPrefUtil.getString(mActivity, "TOKEN", ""))) {
                        getData2(page, p, c);
                        rcv_home_personal.addOnScrollListener(new RecyclerViewListener());
                    } else {
                        MyUtils.showToast(mContext, "请先登录");
                    }
                }
                // 判断是否滑动到顶部
                if (manager.findFirstCompletelyVisibleItemPosition() == 0) {
//                    btn_totop.setVisibility(View.GONE);
                } else {
//                    btn_totop.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    private static void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(mContext).inflate(R.layout.item_rcv_head, view, false);
        mAdapter.setHeaderView(header);
    }

}
