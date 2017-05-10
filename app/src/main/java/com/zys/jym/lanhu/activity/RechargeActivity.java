package com.zys.jym.lanhu.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zys.jym.lanhu.App;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.bean.ToPayData_Data;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.PayUtil;

/**
 * 充值
 * Created by Administrator on 2016/12/17.
 */

public class RechargeActivity extends BaseActivity implements View.OnClickListener {
    String TAG = "TAG--RechargeActivity";
    Toolbar index_toolbar;
    RelativeLayout rl_cz1, rl_cz2, rl_cz3, rl_cz4, rl_cz5, rl_cz6;
    TextView tv_pay_money, tv_nickname;
    static TextView tv_havemoney;
    CheckBox cb_zfb, cb_wx;
    static int pay_money = 0;
    ImageView iv_head;
    EditText et_num;
    ToPayData_Data mPayData;
    int payType = 2;
    static App app;
    public static  RechargeActivity ra;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_recharge);
        ra=this;
        ActivityUtil.add(ra);
        initToolBar();
        initViews();
        initData();
    }

    private void initToolBar() {
        index_toolbar = (Toolbar) findViewById(R.id.index_toolbar);
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
        iv_head = (ImageView) findViewById(R.id.iv_head);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_havemoney = (TextView) findViewById(R.id.tv_havemoney);
        et_num = (EditText) findViewById(R.id.et_num);
        et_num.addTextChangedListener(new myTextChang());
        rl_cz1 = (RelativeLayout) findViewById(R.id.rl_cz1);
        rl_cz1.setOnClickListener(this);
        rl_cz2 = (RelativeLayout) findViewById(R.id.rl_cz2);
        rl_cz2.setOnClickListener(this);
        rl_cz3 = (RelativeLayout) findViewById(R.id.rl_cz3);
        rl_cz3.setOnClickListener(this);
        rl_cz4 = (RelativeLayout) findViewById(R.id.rl_cz4);
        rl_cz4.setOnClickListener(this);
        rl_cz5 = (RelativeLayout) findViewById(R.id.rl_cz5);
        rl_cz5.setOnClickListener(this);
        rl_cz6 = (RelativeLayout) findViewById(R.id.rl_cz6);
        rl_cz6.setOnClickListener(this);
        tv_pay_money = (TextView) findViewById(R.id.tv_pay_money);
        cb_wx = (CheckBox) findViewById(R.id.cb_wx);
        cb_zfb = (CheckBox) findViewById(R.id.cb_zfb);
        findViewById(R.id.tv_topay).setOnClickListener(this);
        findViewById(R.id.rl_wx).setOnClickListener(this);
        findViewById(R.id.rl_zfb).setOnClickListener(this);
    }

    private void initData() {
        app = getApplicationContext();
        app.setPayActivity(0);
        setBackg(1);
        if (!TextUtils.isEmpty(app.getUser().getHeadurl())) {
            Picasso.with(RechargeActivity.this)
                        .load(LHHttpUrl.IMG_URL + app.getUser().getHeadurl())
                    .into(iv_head);
        }
//        if(MyUtils.Str2Int(app.getUser().getViptime())<=0){
//            tv_nickname.setTextColor(getResources().getColor(R.color.black));
//        }else if (TimeUtil.getSurplusDay(TimeUtil.timeStamp2Date(app.getUser().getViptime(),null))<0){
//            tv_nickname.setTextColor(getResources().getColor(R.color.black));
//        }else {
//            tv_nickname.setTextColor(getResources().getColor(R.color.red));
//        }
        if (app.getPurseData()!=null) {
            if (Long.parseLong(getApplicationContext().getPurseData().getViprest()) > 0) {
                tv_nickname.setTextColor(getResources().getColor(R.color.red));
            } else {
                tv_nickname.setTextColor(getResources().getColor(R.color.black));
            }
        }else {
            tv_nickname.setTextColor(getResources().getColor(R.color.black));
        }

        if (!TextUtils.isEmpty(app.getUser().getNickname())) {
            tv_nickname.setText(app.getUser().getNickname());
        }
        if (app.getPurseData() != null) {
            tv_havemoney.setText("狐币余额：" + MyUtils.Intercept_Int_Point(MyUtils.Str2Double(app.getPurseData().getPursebalance())*0.01+""));
        }
    }


    class myTextChang implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            setBackg(7);
            if (TextUtils.isEmpty(et_num.getText().toString().trim())) {
                et_num.setHint("0");
                pay_money = 0;
                tv_pay_money.setText("总价：" + pay_money);
            } else {
                pay_money = MyUtils.Str2Int(et_num.getText().toString().trim());
                tv_pay_money.setText("总价：" + pay_money);
            }

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_cz1:
                setBackg(1);
                break;
            case R.id.rl_cz2:
                setBackg(2);
                break;
            case R.id.rl_cz3:
                setBackg(3);
                break;
            case R.id.rl_cz4:
                setBackg(4);
                break;
            case R.id.rl_cz5:
                setBackg(5);
                break;
            case R.id.rl_cz6:
                setBackg(6);
                break;
            case R.id.rl_zfb:
                if (!cb_wx.isChecked() && !cb_zfb.isChecked()) {
                    cb_zfb.setChecked(true);
                }
                if (cb_wx.isChecked()) {
                    cb_wx.setChecked(false);
                    cb_zfb.setChecked(true);
                }
                break;
            case R.id.rl_wx:
                if (!cb_wx.isChecked() && !cb_zfb.isChecked()) {
                    cb_wx.setChecked(true);
                }
                if (cb_zfb.isChecked()) {
                    cb_zfb.setChecked(false);
                    cb_wx.setChecked(true);
                }
                break;
            case R.id.tv_topay:
                if (pay_money == 0) {
                    MyUtils.showToast(RechargeActivity.this, "请选择或输入充值狐币金额");
                    return;
                }
                if (!cb_wx.isChecked() && !cb_zfb.isChecked()) {
                    MyUtils.showToast(RechargeActivity.this, "请选择支付方式");
                    return;
                }
                if (cb_zfb.isChecked()) {
                    payType = 2;
                    toPay();
                }
                if (cb_wx.isChecked()) {
                    payType = 1;
                    toPay();
                }
                break;

        }
    }

    private void toPay() {
        PayUtil.toPay(RechargeActivity.this, getApplicationContext(), pay_money, payType, 1, pay_money);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case 0x1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(RechargeActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(RechargeActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                0x2);
                    } else {
                        toPay();
                    }
                } else {
                    MyUtils.showToast(RechargeActivity.this, "权限获取失败");
                }
                break;
            case 0x2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toPay();
                } else {
                    MyUtils.showToast(RechargeActivity.this, "权限获取失败");
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    private void setBackg(int i) {
        switch (i) {
            case 1:
                rl_cz1.setBackgroundResource(R.drawable.rl_buy_zd_bg_p);
                rl_cz2.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz3.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz4.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz5.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz6.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                pay_money = 8;
                tv_pay_money.setText("总价：" + pay_money);
                break;
            case 2:
                rl_cz2.setBackgroundResource(R.drawable.rl_buy_zd_bg_p);
                rl_cz1.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz3.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz4.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz5.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz6.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                pay_money = 36;
                tv_pay_money.setText("总价：" + pay_money);
                break;
            case 3:
                rl_cz3.setBackgroundResource(R.drawable.rl_buy_zd_bg_p);
                rl_cz2.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz1.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz4.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz5.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz6.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                pay_money = 64;
                tv_pay_money.setText("总价：" + pay_money);
                break;
            case 4:
                rl_cz4.setBackgroundResource(R.drawable.rl_buy_zd_bg_p);
                rl_cz2.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz3.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz1.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz5.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz6.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                pay_money = 112;
                tv_pay_money.setText("总价：" + pay_money);
                break;
            case 5:
                rl_cz5.setBackgroundResource(R.drawable.rl_buy_zd_bg_p);
                rl_cz2.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz3.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz4.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz1.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz6.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                pay_money = 144;
                tv_pay_money.setText("总价：" + pay_money);
                break;
            case 6:
                rl_cz6.setBackgroundResource(R.drawable.rl_buy_zd_bg_p);
                rl_cz2.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz3.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz4.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz5.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz1.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                pay_money = 200;
                tv_pay_money.setText("总价：" + pay_money);
                break;
            case 7:
                pay_money = 0;
                tv_pay_money.setText("总价：" + pay_money);
                rl_cz6.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz2.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz3.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz4.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz5.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                rl_cz1.setBackgroundResource(R.drawable.rl_buy_zd_bg_n);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(ra);
    }
}


