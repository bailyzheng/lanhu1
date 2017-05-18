package com.zys.jym.lanhu.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.DialogOkUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.PayUtil;
import com.zys.jym.lanhu.utils.TimeUtil;

/**
 * Created by Administrator on 2016/12/17.
 */

public class OpenVipActivity extends BaseActivity implements View.OnClickListener {
    String TAG="TAG--OpenVipActivity";
    Toolbar index_toolbar;
    TextView tv_needpay,tv_have,tv_nickname,tv_lasttime;
    RelativeLayout rl_v1,rl_v2,rl_v3,rl_v4,rl_v5,rl_v6;
    int needPay_Money=0;
    ImageView iv_head;
    int vipNum=0;
    int mPayType=0;
    public static OpenVipActivity ova;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_openvip);
        ova=this;
        ActivityUtil.add(ova);
        initToolBar();
        initViews();
        initData();
    }

    private void initToolBar() {
        index_toolbar= (Toolbar) findViewById(R.id.index_toolbar);
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
        iv_head= (ImageView) findViewById(R.id.iv_head);
        tv_nickname= (TextView) findViewById(R.id.tv_nickname);
        tv_lasttime= (TextView) findViewById(R.id.tv_lasttime);
        rl_v1= (RelativeLayout) findViewById(R.id.rl_v1);
        rl_v1.setOnClickListener(this);
        rl_v2= (RelativeLayout) findViewById(R.id.rl_v2);
        rl_v2.setOnClickListener(this);
        rl_v3= (RelativeLayout) findViewById(R.id.rl_v3);
        rl_v3.setOnClickListener(this);
        rl_v4= (RelativeLayout) findViewById(R.id.rl_v4);
        rl_v4.setOnClickListener(this);
        rl_v5= (RelativeLayout) findViewById(R.id.rl_v5);
        rl_v5.setOnClickListener(this);
        rl_v6= (RelativeLayout) findViewById(R.id.rl_v6);
        rl_v6.setOnClickListener(this);
        tv_have= (TextView) findViewById(R.id.tv_have);
        tv_needpay= (TextView) findViewById(R.id.tv_needpay);
        findViewById(R.id.btn_open).setOnClickListener(this);
        findViewById(R.id.tv_vipclub).setOnClickListener(this);
    }

    private void initData() {
        getApplicationContext().setPayActivity(2);
        setBackg(1);
        if (!TextUtils.isEmpty(getApplicationContext().getUser().getHeadurl())){
            Picasso.with(OpenVipActivity.this)
                    .load(LHHttpUrl.IMG_URL+getApplicationContext().getUser().getHeadurl())
                    .into(iv_head);
        }
        if (!TextUtils.isEmpty(getApplicationContext().getUser().getNickname())){
            tv_nickname.setText(getApplicationContext().getUser().getNickname());
        }
//        if(MyUtils.Str2Int(getApplicationContext().getUser().getViptime())<=0){
//            tv_nickname.setTextColor(getResources().getColor(R.color.black));
//            tv_lasttime.setVisibility(View.INVISIBLE);
//        }else if (TimeUtil.getSurplusDay(TimeUtil.timeStamp2Date(getApplicationContext().getUser().getViptime(),null))<0){
//            tv_nickname.setTextColor(getResources().getColor(R.color.black));
//            tv_lasttime.setVisibility(View.INVISIBLE);
//        }else {
//            tv_nickname.setTextColor(getResources().getColor(R.color.red));
//            tv_lasttime.setText(MyUtils.Intercept_YMDHM(TimeUtil.timeStamp2Date(
//                    getApplicationContext().getUser().getViptime(),null))+"到期");
//        }
        if (getApplicationContext().getPurseData()!=null) {
            if (!TextUtils.isEmpty(getApplicationContext().getPurseData().getViprest())&&MyUtils.Str2Int(getApplicationContext().getPurseData().getViprest()) > 0) {
                tv_nickname.setTextColor(getResources().getColor(R.color.red));
                tv_lasttime.setText(MyUtils.Intercept_YMDHM(TimeUtil.timeStamp2Date(
                        getApplicationContext().getUser().getViptime(), null)) + "到期");
            } else {
                tv_nickname.setTextColor(getResources().getColor(R.color.black));
                tv_lasttime.setVisibility(View.INVISIBLE);
            }
        }else {
            tv_nickname.setTextColor(getResources().getColor(R.color.black));
            tv_lasttime.setVisibility(View.INVISIBLE);
        }
        if (getApplicationContext().getPurseData()!=null){
            tv_have.setText(MyUtils.Intercept_Int_Point(MyUtils.Str2Double(getApplicationContext().getPurseData().getPursebalance())*0.01+"")+"狐币");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_v1:
                setBackg(1);
                break;
            case R.id.rl_v2:
                setBackg(2);
                break;
            case R.id.rl_v3:
                setBackg(3);
                break;
            case R.id.rl_v4:
                setBackg(4);
                break;
            case R.id.rl_v5:
                setBackg(5);
                break;
            case R.id.rl_v6:
                setBackg(6);
                break;
            case R.id.btn_open:
                if (needPay_Money==0){
                    MyUtils.showToast(OpenVipActivity.this,"请选择需要开通VIP的月数");
                    return;
                }
                kt();
                break;
            case R.id.tv_vipclub:
                Intent in =new Intent(OpenVipActivity.this,VipClubActivity.class);
                startActivity(in);
                break;
        }
    }

    private void kt() {
        DialogOkUtil.show_PayType_Dialog(OpenVipActivity.this, new DialogOkUtil.On_PayType_ClickListener() {
            @Override
            public void onWxPay() {
                mPayType=1;
                PayUtil.toPay(OpenVipActivity.this,getApplicationContext(),needPay_Money,1,2,vipNum);
            }

            @Override
            public void onAliPay() {
                mPayType=2;
                PayUtil.toPay(OpenVipActivity.this,getApplicationContext(),needPay_Money,2,2,vipNum);
            }

            @Override
            public void onHuBiPay() {
                mPayType=3;
                PayUtil.toPay(OpenVipActivity.this,getApplicationContext(),needPay_Money,3,2,vipNum);
            }
        }).show();

    }
    private void toPay(){
        switch (mPayType){
            case 1:
                PayUtil.toPay(OpenVipActivity.this,getApplicationContext(),needPay_Money,1,2,vipNum);
                break;
            case 2:
                PayUtil.toPay(OpenVipActivity.this,getApplicationContext(),needPay_Money,2,2,vipNum);
                break;
            case 3:
                PayUtil.toPay(OpenVipActivity.this,getApplicationContext(),needPay_Money,3,2,vipNum);
                break;
        }

    }


    private void setBackg(int i) {
        switch (i){
            case 1:
                vipNum=1;
                rl_v1.setBackgroundColor(getResources().getColor(R.color.buy_vip_p));
                rl_v2.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v3.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v4.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v5.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v6.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                needPay_Money=20;
                tv_needpay.setText(needPay_Money+"狐币");
                break;
            case 2:
                vipNum=2;
                rl_v2.setBackgroundColor(getResources().getColor(R.color.buy_vip_p));
                rl_v1.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v3.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v4.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v5.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v6.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                needPay_Money=40;
                tv_needpay.setText(needPay_Money+"狐币");
                break;
            case 3:
                vipNum=3;
                rl_v3.setBackgroundColor(getResources().getColor(R.color.buy_vip_p));
                rl_v2.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v1.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v4.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v5.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v6.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                needPay_Money=60;
                tv_needpay.setText(needPay_Money+"狐币");
                break;
            case 4:
                vipNum=6;
                rl_v4.setBackgroundColor(getResources().getColor(R.color.buy_vip_p));
                rl_v2.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v3.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v1.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v5.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v6.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                needPay_Money=120;
                tv_needpay.setText(needPay_Money+"狐币");
                break;
            case 5:
                vipNum=9;
                rl_v5.setBackgroundColor(getResources().getColor(R.color.buy_vip_p));
                rl_v2.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v3.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v4.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v1.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v6.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                needPay_Money=180;
                tv_needpay.setText(needPay_Money+"狐币");
                break;
            case 6:
                vipNum=12;
                rl_v6.setBackgroundColor(getResources().getColor(R.color.buy_vip_p));
                rl_v2.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v3.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v4.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v5.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                rl_v1.setBackgroundColor(getResources().getColor(R.color.buy_vip_n));
                needPay_Money=240;
                tv_needpay.setText(needPay_Money+"狐币");
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode){
            case 0x1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(OpenVipActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION )
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(OpenVipActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                0x2);
                    } else {
                        toPay();
                    }
                } else {
                    MyUtils.showToast(OpenVipActivity.this, "权限获取失败");
                }
                break;
            case 0x2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toPay();
                } else {
                    MyUtils.showToast(OpenVipActivity.this, "权限获取失败");
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(ova);
    }
}
