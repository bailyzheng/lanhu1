package com.zys.jym.lanhu.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.bean.NormalData;
import com.zys.jym.lanhu.httpcallback.NormalCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.DialogOkUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;

import okhttp3.Call;

/**
 * 兑换码
 * Created by Administrator on 2016/12/14.
 */

public class RedeemCodeActivity extends BaseActivity implements View.OnClickListener {
    String TAG="TAG--RedeemCodeActivity";
    Toolbar index_toolbar;
    EditText et_dhcode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_redeemcode);
        ActivityUtil.add(this);
        initViews();
        initToolBar();
    }

    private void initViews() {
        findViewById(R.id.btn_dh).setOnClickListener(this);
        et_dhcode= (EditText) findViewById(R.id.et_dhcode);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_dh:
                if (TextUtils.isEmpty(et_dhcode.getText().toString().trim())){
                    MyUtils.showToast(RedeemCodeActivity.this,"请输入兑换码");
                    return;
                }
                dh();
                break;
        }
    }

    private void dh() {
        MyUtils.showDialog(RedeemCodeActivity.this, "兑换中...");
        if(getApplicationContext()!=null&&getApplicationContext().getUser()!=null&&!TextUtils.isEmpty(getApplicationContext().getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .tag(this)
                    .url(LHHttpUrl.EXCHANGECODE_URL)
                    .addParams("login_token", getApplicationContext().getUser().getLogin_token())
                    .addParams("code", et_dhcode.getText().toString().trim())
                    .build()
                    .execute(new NormalCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                            MyUtils.showToast(RedeemCodeActivity.this, "服务器繁忙，请稍后再试");
                        }

                        @Override
                        public void onResponse(NormalData mData) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());

                            if (mData.getErrcode() == 1) {
                                Main2Activity.getPurseData(false);
                                DialogOkUtil.show_Ok_Dialog(RedeemCodeActivity.this, mData.getErrmsg(), new DialogOkUtil.On_OK_ClickListener() {
                                    @Override
                                    public void onOk() {
                                        finish();
                                    }
                                }).show();
                            } else {
                                DialogOkUtil.show_Ok_Dialog(RedeemCodeActivity.this, mData.getErrmsg(), new DialogOkUtil.On_OK_ClickListener() {
                                    @Override
                                    public void onOk() {
                                    }
                                }).show();
                            }
                        }
                    });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(this);
    }

}
