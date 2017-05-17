package com.zys.jym.lanhu.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.adapter.MsgCenterLvAdapter;
import com.zys.jym.lanhu.bean.MsgCenterData;
import com.zys.jym.lanhu.httpcallback.MsgCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.SPrefUtil;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/12/22.
 */

public class MsgCenterActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    String TAG="TAG--MsgCenterActivity";
    Toolbar index_toolbar;
    ListView lv_msg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_msgcenter);
        ActivityUtil.add(this);
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
        lv_msg= (ListView) findViewById(R.id.lv_msg);
        lv_msg.setOnItemClickListener(this);
    }

    private void initData() {
        getData();
    }

    private void getData() {
        MyUtils.showDialog(MsgCenterActivity.this,"加载中...");
//        if(getApplicationContext()!=null&&getApplicationContext().getUser()!=null&&!TextUtils.isEmpty(getApplicationContext().getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .tag(this)
                    .url(LHHttpUrl.INFORMLIST_URL)
//                    .addParams("login_token", getApplicationContext().getUser().getLogin_token())
                    .addParams("login_token", SPrefUtil.getString(MsgCenterActivity.this,"TOKEN",""))
                    .build()
                    .execute(new MsgCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                            MyUtils.showToast(MsgCenterActivity.this, "连接服务器失败，请稍后再试");
                        }

                        @Override
                        public void onResponse(MsgCenterData mData) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                            if(mData.getErrcode()==40001){
                                ActivityUtil.exitAll();
                                ActivityUtil.toLogin(MsgCenterActivity.this);
                                return;
                            }
                            if (mData.getErrcode() == 1) {
                                if (mData.getData().getLogList().size() != 0) {
                                    MsgCenterLvAdapter mAdapter = new MsgCenterLvAdapter(MsgCenterActivity.this, mData.getData().getLogList());
                                    lv_msg.setAdapter(mAdapter);
                                } else {
                                    MyUtils.showToast(MsgCenterActivity.this, "暂时没有消息哦");
                                }
                            } else {
                                MyUtils.showToast(MsgCenterActivity.this, mData.getErrmsg());
                            }

                        }
                    });
//        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //如果有url 进入
//        Intent in =new Intent(MsgCenterActivity.this,MsgUrlActivity.class);
//        startActivity(in);
    }
}
