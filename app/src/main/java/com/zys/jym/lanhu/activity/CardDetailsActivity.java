package com.zys.jym.lanhu.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.bean.CardDetailData;
import com.zys.jym.lanhu.bean.TopData;
import com.zys.jym.lanhu.httpcallback.CardDetailCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.SaveCodeUtil;
import com.zys.jym.lanhu.utils.SavePicUtil;

import java.io.File;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/12/16.
 */

public class CardDetailsActivity extends BaseActivity implements View.OnClickListener {
    String TAG = "TAG--CardDetailsActivity";
    Toolbar index_toolbar;
    TopData mData;
    ImageView iv_head, iv_ewmcode;
    TextView tv_nickname, tv_dq, tv_user_describe, tv_describe, tv_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_carddetails);
        ActivityUtil.add(this);
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
        findViewById(R.id.tv_com).setOnClickListener(this);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_dq = (TextView) findViewById(R.id.tv_dq);
        tv_user_describe = (TextView) findViewById(R.id.tv_user_describe);
        tv_describe = (TextView) findViewById(R.id.tv_describe);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_ewmcode = (ImageView) findViewById(R.id.iv_code);
        findViewById(R.id.ll_save).setOnClickListener(this);
    }

    private void initData() {
        mData = (TopData) getIntent().getSerializableExtra("cardDetail");
        getData(mData.getId());
        if (!getIntent().getBooleanExtra("FromTop", false)) {

            if (!TextUtils.isEmpty(mData.getViprest())&&MyUtils.Str2Int(mData.getViprest()) > 0) {
                tv_nickname.setTextColor(getResources().getColor(R.color.red));
                tv_title.setTextColor(getResources().getColor(R.color.red));
            }
        } else {
            if (getApplicationContext().getPurseData() != null &&!TextUtils.isEmpty(getApplicationContext().getPurseData().getViprest())&&
                    MyUtils.Str2Int(getApplicationContext().getPurseData().getViprest()) > 0) {
                tv_nickname.setTextColor(getResources().getColor(R.color.red));
                tv_title.setTextColor(getResources().getColor(R.color.red));
            }
        }

    }

    private void getData(String id) {

        MyUtils.showDialog(CardDetailsActivity.this, "加载中...");
        MyUtils.Loge(TAG, "id=" + id);
        OkHttpUtils
                .post()
                .tag(this)
                .url(LHHttpUrl.DETAILTOPIC_URL)
                .addParams("id", id)
                .build()
                .execute(new CardDetailCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        MyUtils.dismssDialog();
//                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                        MyUtils.showToast(CardDetailsActivity.this, "连接服务器失败，请稍后再试");
                    }

                    @Override
                    public void onResponse(CardDetailData mData) {
                        MyUtils.dismssDialog();
//                        MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                        if (mData.getErrcode() == 40001) {
                            ActivityUtil.exitAll();
                            ActivityUtil.toLogin(CardDetailsActivity.this);
                            return;
                        }
                        if (mData.getErrcode() == 1) {
                            if (mData != null && mData.getData() != null && mData.getData().getHeadurl() != null) {
                                Picasso.with(CardDetailsActivity.this)
                                        .load(LHHttpUrl.IMG_URL + mData.getData().getHeadurl())
                                        .into(iv_head);
                            }

                            tv_nickname.setText(mData.getData().getNickname());
                            tv_dq.setText("地区：" + mData.getData().getProvincename() + "-" + mData.getData().getCityname());
                            tv_describe.setText(mData.getData().getDescribe());
                            tv_title.setText(mData.getData().getTitle());
                            if (mData.getData().getUdescribe() != null) {
                                tv_user_describe.setText("描述：" + mData.getData().getUdescribe());
                            }
                            Picasso.with(CardDetailsActivity.this)
                                    .load(LHHttpUrl.IMG_URL + mData.getData().getImgurl())
                                    .into(iv_ewmcode);

                        } else {
                            MyUtils.showToast(CardDetailsActivity.this, mData.getErrmsg());
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_com:
                if (!getApplicationContext().getIsLogin()) {
                    Intent in = new Intent(CardDetailsActivity.this, LoginActivity.class);
                    startActivity(in);
                    return;
                }
                Intent in = new Intent(CardDetailsActivity.this, ComplaintActivity.class);
                in.putExtra("cid", mData.getId());
                startActivity(in);
                break;
            case R.id.ll_save:
                toSave();

                break;
        }
    }

    private void toSave() {
        SavePicUtil.SavePic(CardDetailsActivity.this, mData.getImgurl(), 0, new SavePicUtil.On_Save_ClickListener() {
            @Override
            public void onSuccess(File file, int nextPosition) {
                SaveCodeUtil.insertCode(CardDetailsActivity.this, mData.getId());
                MyUtils.showToast(CardDetailsActivity.this, "已保存至" +
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/lh/CodeImg/"
                        + file.getName());
            }

            @Override
            public void onFail(Call call, Exception e, int nextPosition) {
                MyUtils.showToast(CardDetailsActivity.this, "保存失败");
            }

            @Override
            public void onProgress(float progress, long l) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 调用EasyPermissions的onRequestPermissionsResult方法，参数和系统方法保持一致，然后就不要关心具体的权限申请代码了
//        save(requestCode,grantResults);
        switch (requestCode) {
            case 0x1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toSave();
                } else {
                    MyUtils.showToast(CardDetailsActivity.this, "权限获取失败,不能保存二维码");
                }
                break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(this);
    }
}
