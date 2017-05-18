package com.zys.jym.lanhu.fragment.impl;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.ComplaintActivity;
import com.zys.jym.lanhu.activity.DHZDActivity;
import com.zys.jym.lanhu.activity.HBStreamActivity;
import com.zys.jym.lanhu.activity.KeFu2Activity;
import com.zys.jym.lanhu.activity.Main2Activity;
import com.zys.jym.lanhu.activity.ModifyDataActivity;
import com.zys.jym.lanhu.activity.MsgCenterActivity;
import com.zys.jym.lanhu.activity.OpenVipActivity;
import com.zys.jym.lanhu.activity.RechargeActivity;
import com.zys.jym.lanhu.activity.RedeemCodeActivity;
import com.zys.jym.lanhu.activity.SettingActivity;
import com.zys.jym.lanhu.activity.ShareActivity;
import com.zys.jym.lanhu.activity.ZDActivity;
import com.zys.jym.lanhu.activity.ZDStreamActivity;
import com.zys.jym.lanhu.bean.GetPurseData;
import com.zys.jym.lanhu.bean.PurseData;
import com.zys.jym.lanhu.fragment.BaseFragment;
import com.zys.jym.lanhu.httpcallback.GetPurseCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.RequestCode;
import com.zys.jym.lanhu.utils.SPrefUtil;
import com.zys.jym.lanhu.utils.TimeUtil;
import com.zys.jym.lanhu.view.CircleImageView;
import com.zys.jym.lanhu.view.HeadZoomScrollView;

import okhttp3.Call;


/**
 * Created by Administrator on 2017/1/22.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    String TAG = "TAG--MineFragment";
    private RelativeLayout rl_setting, rl_dhm, rl_kf;
    private HeadZoomScrollView psv_xl;
    static CircleImageView civ_head;
    private static TextView tv_nickname;
    private static TextView tv_vip_time, tv_hbye, tv_zdnum;
    ImageView iv_bg;
    int mine_bg;
    public static boolean isResume = false;
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RequestCode.TO_CHANGEUNICKNAME:
                    tv_nickname.setText(app.getUser().getNickname());
                    break;
            }
        }
    };

    @Override
    public View initView() {
        MyUtils.Loge(TAG, "MineFragment--initView");
        View view = View.inflate(getActivity(), R.layout.fragment_mine, null);
        psv_xl = (HeadZoomScrollView) view.findViewById(R.id.psv_xl);
        rl_setting = (RelativeLayout) view.findViewById(R.id.rl_setting);
        rl_dhm = (RelativeLayout) view.findViewById(R.id.rl_dhm);
        rl_kf = (RelativeLayout) view.findViewById(R.id.rl_kf);
        tv_hbye = (TextView) view.findViewById(R.id.tv_hbye);
        tv_zdnum = (TextView) view.findViewById(R.id.tv_zdnum);
//        view.findViewById(R.id.ll_vip).setOnClickListener(this);
        view.findViewById(R.id.ll_zd).setOnClickListener(this);
        view.findViewById(R.id.ll_lhmoey).setOnClickListener(this);
        view.findViewById(R.id.rl_mygroup).setOnClickListener(this);
        view.findViewById(R.id.rl_vipfw).setOnClickListener(this);
        view.findViewById(R.id.rl_share).setOnClickListener(this);
        view.findViewById(R.id.rl_msg).setOnClickListener(this);
        view.findViewById(R.id.rl_cz).setOnClickListener(this);
        view.findViewById(R.id.rl_dhzd).setOnClickListener(this);
        iv_bg = (ImageView) view.findViewById(R.id.iv_bg);
        iv_bg.setOnClickListener(this);
        civ_head = (CircleImageView) view.findViewById(R.id.civ_head);
        civ_head.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        rl_dhm.setOnClickListener(this);
        rl_kf.setOnClickListener(this);
        tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
        tv_vip_time = (TextView) view.findViewById(R.id.tv_vip_time);
        psv_xl.setZoomView(iv_bg);
        getineBg();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        MyUtils.Loge(TAG, "MineFragment--onResume");
        isResume = true;
        initData();

    }

    @Override
    public void initData() {
        MyUtils.Loge(TAG, "MineFragment--initData");
        MyUtils.Loge(TAG, "getIsLogin=" + app.getIsLogin());
//        if (!app.getIsLogin()){
//           return;
//        }else {
//            MyUtils.Loge(TAG,"getUser="+app.getUser());
//        }
//        if (!TextUtils.isEmpty(app.getUser().getHeadurl())){
//            Picasso.with(mActivity)
//                    .load(LHHttpUrl.IMG_URL+app.getUser().getHeadurl())
//                    .into(civ_head);
//        }
//        if (!TextUtils.isEmpty(app.getUser().getNickname())){
//            tv_nickname.setText(app.getUser().getNickname());
//        }
////        MyUtils.Loge(TAG,"Viptime="+app.getUser().getViptime());
//        if (app.getPurseData()!=null){
//            if (MyUtils.Str2Int(app.getPurseData().getViprest())>0){
//                tv_nickname.setTextColor(mActivity.getResources().getColor(R.color.red));
//                tv_vip_time.setText(TimeUtil.getSurplusDay(TimeUtil.timeStamp2Date(app.getUser().getViptime(),null))+"天");;
//            }else {
//                tv_vip_time.setText("0天");
//                tv_nickname.setTextColor(mActivity.getResources().getColor(R.color.black));
//            }
//        }else {
//            tv_vip_time.setText("0天");
//            tv_nickname.setTextColor(mActivity.getResources().getColor(R.color.black));
//        }
//        if (app.getPurseData()!=null){
//            tv_hbye.setText(MyUtils.Intercept_Int_Point(MyUtils.Str2Double(app.getPurseData().getPursebalance())*0.01+""));
////            tv_hbye.setText((MyUtils.Intercept_Int_Point(MyUtils.Str2Double(app.getPurseData().getPursebalance())*0.01)+""));
//            tv_zdnum.setText(app.getPurseData().getTopbalance()+"次");
//        }
        getCount();
        psv_xl.setOnRefListener(new HeadZoomScrollView.OnRefListener() {
            @Override
            public void onRef() {
//                MyUtils.Loge(TAG,"回弹监听");
                //下拉做点事情。。
                getCount();
            }
        });
    }

    /**
     * 获取账户信息
     */
    private void getCount() {
//        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
        OkHttpUtils
                .post()
                .url(LHHttpUrl.GETPURSEDATA_URL)
//                    .addParams("login_token", app.getUser().getLogin_token())
                .addParams("login_token", SPrefUtil.getString(mActivity, "TOKEN", ""))
                .build()
                .execute(new GetPurseCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                    }

                    @Override
                    public void onResponse(GetPurseData mData) {
                        MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                        if (mData != null) {
                            if (mData.getErrcode() == 40001) {
                                ActivityUtil.exitAll();
                                ActivityUtil.toLogin(mActivity);
                                return;
                            }
                            if (mData.getErrcode() == 1) {
//                                    MyUtils.Loge(TAG, "user=" + mData.getUserDate() + "，账户信息=" + mData.getUserInfo());
                                if (mData.getUserDate() != null && mData.getUserInfo() != null && !TextUtils.isEmpty(mData.getUserInfo().getViptime()) && !TextUtils.isEmpty(mData.getUserDate().getViprest())) {
                                    app.setPurseData(mData.getUserDate());
                                    app.getUser().setViptime(mData.getUserInfo().getViptime());
                                    app.getPurseData().setViprest(mData.getUserDate().getViprest());
                                    setviews(mData);
                                }
                            }
                        }
                    }
                });
//        }
    }

    /**
     * 设置账户
     *
     * @param mData
     */
    private void setviews(GetPurseData mData) {
        MyUtils.Loge("aaa", "touxiang:" + mData.getUserInfo().getHeadurl());
        Picasso.with(mActivity)
                .load(LHHttpUrl.IMG_URL + mData.getUserInfo().getHeadurl())
                .into(civ_head);
        tv_nickname.setText(mData.getUserInfo().getNickname());
        if (MyUtils.Str2Int(mData.getUserDate().getViprest()) > 0) {
            tv_nickname.setTextColor(mActivity.getResources().getColor(R.color.red));
            tv_vip_time.setText(TimeUtil.getSurplusDay(TimeUtil.timeStamp2Date(mData.getUserInfo().getViptime(), null)) + "天");
            ;
        } else {
            tv_vip_time.setText("0天");
            tv_nickname.setTextColor(mActivity.getResources().getColor(R.color.black));
        }
        tv_hbye.setText(MyUtils.Intercept_Int_Point(MyUtils.Str2Double(mData.getUserDate().getPursebalance()) * 0.01 + ""));
//            tv_hbye.setText((MyUtils.Intercept_Int_Point(MyUtils.Str2Double(app.getPurseData().getPursebalance())*0.01)+""));
        tv_zdnum.setText(mData.getUserDate().getTopbalance() + "次");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.civ_head:
                Intent in = new Intent(mActivity, ModifyDataActivity.class);
                mActivity.startActivity(in);
                break;
            case R.id.rl_dhm:
                Intent in1 = new Intent(mActivity, RedeemCodeActivity.class);
                mActivity.startActivity(in1);
                break;
            case R.id.rl_vipfw://购买VIP
                Intent in2 = new Intent(mActivity, OpenVipActivity.class);
                mActivity.startActivity(in2);
                break;
            case R.id.ll_zd:
                Intent in3 = new Intent(mActivity, ZDStreamActivity.class);
                mActivity.startActivity(in3);
                break;
            case R.id.ll_lhmoey://狐币流水
                Intent in4 = new Intent(mActivity, HBStreamActivity.class);
                mActivity.startActivity(in4);
                break;

            case R.id.rl_setting:
                Intent in5 = new Intent(mActivity, SettingActivity.class);
                mActivity.startActivity(in5);
                break;
            case R.id.rl_share:
                Intent in6 = new Intent(mActivity, ShareActivity.class);
                mActivity.startActivity(in6);
                break;
            case R.id.rl_msg:
                Intent in7 = new Intent(mActivity, MsgCenterActivity.class);
                mActivity.startActivity(in7);
                break;
            case R.id.rl_cz://充值
                Intent in8 = new Intent(mActivity, RechargeActivity.class);
                mActivity.startActivity(in8);
                break;
            case R.id.rl_dhzd://兑换置顶
                Intent in9 = new Intent(mActivity, DHZDActivity.class);
                mActivity.startActivity(in9);
                break;
            case R.id.rl_kf:
                Intent in10 = new Intent(mActivity, KeFu2Activity.class);
                mActivity.startActivity(in10);
                break;
            case R.id.rl_mygroup:// 我的群聊
                Intent in11 = new Intent(mActivity, ZDActivity.class);
                in11.putExtra("type", "2");
                mActivity.startActivity(in11);
                break;
            case R.id.iv_bg:
                MyUtils.Loge(TAG, "点击mine_bg=" + mine_bg);
                setMineBg();
                break;
        }
    }

    private void getineBg() {
        mine_bg = SPrefUtil.getInt(mActivity, "MINE_BG", 1);
        if (mine_bg == 2) {
            iv_bg.setImageResource(R.mipmap.ic_mine_bg2);
        }
        if (mine_bg == 3) {
            iv_bg.setImageResource(R.mipmap.ic_mine_bg3);
        }
        if (mine_bg == 4) {
            iv_bg.setImageResource(R.mipmap.ic_mine_bg4);
        }
        if (mine_bg == 5) {
            iv_bg.setImageResource(R.mipmap.ic_mine_bg5);
        }
        if (mine_bg == 6) {
            iv_bg.setImageResource(R.mipmap.ic_mine_bg6);
        }
        if (mine_bg == 7) {
            iv_bg.setImageResource(R.mipmap.ic_mine_bg7);
        }
    }

    private void setMineBg() {
        switch (mine_bg) {
            case 1:
                mine_bg = 2;
                SPrefUtil.setInt(mActivity, "MINE_BG", mine_bg);
                iv_bg.setImageResource(R.mipmap.ic_mine_bg2);
                break;
            case 2:
                mine_bg = 3;
                SPrefUtil.setInt(mActivity, "MINE_BG", mine_bg);
                iv_bg.setImageResource(R.mipmap.ic_mine_bg3);
                break;
            case 3:
                mine_bg = 4;
                SPrefUtil.setInt(mActivity, "MINE_BG", mine_bg);
                iv_bg.setImageResource(R.mipmap.ic_mine_bg4);
                break;
            case 4:
                mine_bg = 5;
                SPrefUtil.setInt(mActivity, "MINE_BG", mine_bg);
                iv_bg.setImageResource(R.mipmap.ic_mine_bg5);
                break;
            case 5:
                mine_bg = 6;
                SPrefUtil.setInt(mActivity, "MINE_BG", mine_bg);
                iv_bg.setImageResource(R.mipmap.ic_mine_bg6);
                break;
            case 6:
                mine_bg = 7;
                SPrefUtil.setInt(mActivity, "MINE_BG", mine_bg);
                iv_bg.setImageResource(R.mipmap.ic_mine_bg7);
                break;
            case 7:
                mine_bg = 1;
                SPrefUtil.setInt(mActivity, "MINE_BG", mine_bg);
                iv_bg.setImageResource(R.mipmap.ic_mine_bg1);
                break;
        }
    }

    public static void setHeadImg(Intent data) {
        civ_head.setImageURI(data.getData());
    }

    public static void setPData(PurseData data) {
//        MyUtils.Loge(TAG,"d="+MyUtils.Str2Double(data.getPursebalance()));
//        MyUtils.Loge(TAG,"d="+(MyUtils.Str2Double(data.getPursebalance())*0.01));
        tv_hbye.setText(MyUtils.Intercept_Int_Point(MyUtils.Str2Double(app.getPurseData().getPursebalance()) * 0.01 + ""));
        tv_zdnum.setText(data.getTopbalance() + "次");
        if (MyUtils.Str2Int(app.getPurseData().getViprest()) > 0) {
            tv_nickname.setTextColor(mActivity.getResources().getColor(R.color.red));
            tv_vip_time.setText(TimeUtil.getSurplusDay(TimeUtil.timeStamp2Date(app.getUser().getViptime(), null)) + "天");
            ;
        } else {
            tv_vip_time.setText("0天");
            tv_nickname.setTextColor(mActivity.getResources().getColor(R.color.black));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isResume = false;
    }
}
