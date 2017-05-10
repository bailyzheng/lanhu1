package com.zys.jym.lanhu.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.utils.DialogOkUtil;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.PayUtil;

/**
 * 兑换置顶
 * Created by Administrator on 2016/12/17.
 */

public class DHZDActivity extends BaseActivity implements View.OnClickListener {
    String TAG="TAG--DHZDActivity";
    Toolbar index_toolbar;
    RelativeLayout rl_zd1,rl_zd2,rl_zd3,rl_zd4,rl_zd5,rl_zd6;
    TextView tv_have,tv_needpay;
    int needPay_Money=0;
    int zdNum=0;
    int mPayType=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_dhzd);
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
        rl_zd1= (RelativeLayout) findViewById(R.id.rl_zd1);
        rl_zd1.setOnClickListener(this);
        rl_zd2= (RelativeLayout) findViewById(R.id.rl_zd2);
        rl_zd2.setOnClickListener(this);
        rl_zd3= (RelativeLayout) findViewById(R.id.rl_zd3);
        rl_zd3.setOnClickListener(this);
        rl_zd4= (RelativeLayout) findViewById(R.id.rl_zd4);
        rl_zd4.setOnClickListener(this);
        rl_zd5= (RelativeLayout) findViewById(R.id.rl_zd5);
        rl_zd5.setOnClickListener(this);
        rl_zd6= (RelativeLayout) findViewById(R.id.rl_zd6);
        rl_zd6.setOnClickListener(this);
        tv_have= (TextView) findViewById(R.id.tv_have);
        tv_needpay= (TextView) findViewById(R.id.tv_needpay);
        findViewById(R.id.btn_dh).setOnClickListener(this);
    }

    private void initData() {
        getApplicationContext().setPayActivity(1);
        if (getApplicationContext().getPurseData()!=null){
            tv_have.setText(MyUtils.Intercept_Int_Point(MyUtils.Str2Double(getApplicationContext().getPurseData().getPursebalance())*0.01+"")+"狐币");
        }
        setBackg(1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_zd1:
                setBackg(1);
                break;
            case R.id.rl_zd2:
                setBackg(2);
                break;
            case R.id.rl_zd3:
                setBackg(3);
                break;
            case R.id.rl_zd4:
                setBackg(4);
                break;
            case R.id.rl_zd5:
                setBackg(5);
                break;
            case R.id.rl_zd6:
                setBackg(6);
                break;
            case R.id.btn_dh:
                if (needPay_Money==0){
                    MyUtils.showToast(DHZDActivity.this,"请选择兑换置顶次数");
                    return;
                }
                dh();
                break;
        }
    }

    private void dh() {

        DialogOkUtil.show_PayType_Dialog(DHZDActivity.this, new DialogOkUtil.On_PayType_ClickListener() {
            @Override
            public void onWxPay() {
                mPayType=1;
                PayUtil.toPay(DHZDActivity.this,getApplicationContext(),needPay_Money,1,3,zdNum);
            }

            @Override
            public void onAliPay() {
                mPayType=2;
                PayUtil.toPay(DHZDActivity.this,getApplicationContext(),needPay_Money,2,3,zdNum);
            }

            @Override
            public void onHuBiPay() {
                mPayType=3;
                PayUtil.toPay(DHZDActivity.this,getApplicationContext(),needPay_Money,3,3,zdNum);
            }
        }).show();

    }

    private void toPay(){
        switch (mPayType){
            case 1:
                PayUtil.toPay(DHZDActivity.this,getApplicationContext(),needPay_Money,1,3,zdNum);
                break;
            case 2:
                PayUtil.toPay(DHZDActivity.this,getApplicationContext(),needPay_Money,2,3,zdNum);
                break;
            case 3:
                PayUtil.toPay(DHZDActivity.this,getApplicationContext(),needPay_Money,3,3,zdNum);
                break;
        }
    }

    private void setBackg(int i) {
        switch (i){
            case 1:
                zdNum=1;
                rl_zd1.setBackgroundResource(R.drawable.rl_buy_zd_bg_p);
                rl_zd2.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd3.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd4.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd5.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd6.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                needPay_Money=8;
                tv_needpay.setText(needPay_Money+"狐币");
                break;
            case 2:
                zdNum=5;
                rl_zd2.setBackgroundResource(R.drawable.rl_buy_zd_bg_p);
                rl_zd1.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd3.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd4.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd5.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd6.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                needPay_Money=36;
                tv_needpay.setText(needPay_Money+"狐币");
                break;
            case 3:
                zdNum=10;
                rl_zd3.setBackgroundResource(R.drawable.rl_buy_zd_bg_p);
                rl_zd2.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd1.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd4.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd5.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd6.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                needPay_Money=64;
                tv_needpay.setText(needPay_Money+"狐币");
                break;
            case 4:
                zdNum=20;
                rl_zd4.setBackgroundResource(R.drawable.rl_buy_zd_bg_p);
                rl_zd2.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd3.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd1.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd5.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd6.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                needPay_Money=112;
                tv_needpay.setText(needPay_Money+"狐币");
                break;
            case 5:
                zdNum=30;
                rl_zd5.setBackgroundResource(R.drawable.rl_buy_zd_bg_p);
                rl_zd2.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd3.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd4.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd1.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd6.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                needPay_Money=144;
                tv_needpay.setText(needPay_Money+"狐币");
                break;
            case 6:
                zdNum=50;
                rl_zd6.setBackgroundResource(R.drawable.rl_buy_zd_bg_p);
                rl_zd2.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd3.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd4.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd5.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_zd1.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                needPay_Money=200;
                tv_needpay.setText(needPay_Money+"狐币");
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode){
            case 0x1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(DHZDActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION )
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(DHZDActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                0x2);
                    } else {
                        toPay();
                    }
                } else {
                    MyUtils.showToast(DHZDActivity.this, "权限获取失败");
                }
                break;
            case 0x2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toPay();
                } else {
                    MyUtils.showToast(DHZDActivity.this, "权限获取失败");
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
