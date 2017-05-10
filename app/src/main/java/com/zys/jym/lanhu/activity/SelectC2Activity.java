package com.zys.jym.lanhu.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.pager.ReleasePager;
import com.zys.jym.lanhu.activity.pager.release.R_PersonalCardPager;
import com.zys.jym.lanhu.adapter.C2_lv_Adapter;
import com.zys.jym.lanhu.bean.C_Data;
import com.zys.jym.lanhu.bean.GetCData;
import com.zys.jym.lanhu.fragment.impl.GroupFragment;
import com.zys.jym.lanhu.fragment.impl.HomeFragment;
import com.zys.jym.lanhu.fragment.impl.PersonalFragment;
import com.zys.jym.lanhu.fragment.impl.R_PersonalCardFragment;
import com.zys.jym.lanhu.fragment.impl.ReleaseFragment;
import com.zys.jym.lanhu.httpcallback.GetCCallback;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;

import okhttp3.Call;


/**
 * Created by Admin on 2016/6/26.
 */
public class SelectC2Activity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView lv_c;
    private String p,p_id;
    private String TAG="TAG--SelectC2Activity";
    private int inData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_selecec);
        initToolbar();
        initViews();
        initData();
    }

    private void initToolbar() {
        Toolbar index_toolbar = (Toolbar) findViewById(R.id.index_toolbar);
        TextView tv_bar_title = (TextView) findViewById(R.id.tv_bar_title);
        tv_bar_title.setText("选择城市");
        index_toolbar.setNavigationIcon(R.mipmap.backimg);
        index_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        lv_c= (ListView) findViewById(R.id.lv_c);
        lv_c.setOnItemClickListener(this);
    }
    private void initData() {
        p=getIntent().getStringExtra("p");
        p_id=getIntent().getStringExtra("p_id");
        inData=getIntent().getIntExtra("inData",0);
        MyUtils.Loge(TAG,"p="+p+"p_id="+p_id);
        getC_Data();
    }

    private void getC_Data() {
        MyUtils.showDialog(SelectC2Activity.this,"获取信息中...");
        OkHttpUtils
                .post()
                .tag(this)
                .url(LHHttpUrl.GETCITYBYPROID_URL)
                .addParams("provinceId", p_id+"")
                .build()
                .execute(new GetCCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                        MyUtils.showToast(SelectC2Activity.this, "连接服务器失败，请稍后再试");
                    }

                    @Override
                    public void onResponse(GetCData mData) {
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                        if (mData.errcode==1){
                            if (mData.getData().size()!=0){
                                lv_c.setAdapter(new C2_lv_Adapter(SelectC2Activity.this,mData.getData()));
                            }
                        }else {
                            MyUtils.showToast(SelectC2Activity.this,mData.errmsg);
                        }
                    }
                });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        C_Data mdata= (C_Data) parent.getAdapter().getItem(position);
        Message msg= Message.obtain();
        Bundle bundle=new Bundle();
        bundle.putString("p",p);
        bundle.putString("p_id",p_id);
        bundle.putString("c",mdata.getName());
        bundle.putString("c_id",mdata.getId());
        msg.setData(bundle);
        msg.what=0x10;
        if (inData==0){//群聊界面
            GroupFragment.handler.sendMessage(msg);
        }
        if (inData==1){//发布个人
            ReleasePager.handler.sendMessage(msg);
        }
        if (inData==2){//发布群聊
            ReleasePager.handler.sendMessage(msg);
        }
        if (inData==4){//个人界面
            PersonalFragment.handler.sendMessage(msg);
        }
        if(inData==3){//首页定位
            HomeFragment.handler.sendMessage(msg);
        }
        if(inData == 5){
            HomeFragment.handler2.sendMessage(msg);
        }
        if (inData == 6){
            ReleaseFragment.handler.sendMessage(msg);
        }
        SelectP2Activity.spa2.finish();
        finish();

    }
}
