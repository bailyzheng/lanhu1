package com.zys.jym.lanhu.activity;

import android.content.Intent;
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
import com.zys.jym.lanhu.adapter.P2_lv_Adapter;
import com.zys.jym.lanhu.bean.GetPData;
import com.zys.jym.lanhu.bean.HomeListData;
import com.zys.jym.lanhu.bean.P_Data;
import com.zys.jym.lanhu.fragment.impl.GroupFragment;
import com.zys.jym.lanhu.fragment.impl.HomeFragment;
import com.zys.jym.lanhu.fragment.impl.PersonalFragment;
import com.zys.jym.lanhu.httpcallback.GetPCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by Admin on 2016/6/26.
 */
public class SelectP2Activity extends BaseActivity implements AdapterView.OnItemClickListener {
    String TAG="TAG--SelectP2Activity";
    private ListView lv_p;
    public static SelectP2Activity spa2;
    private int inData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_selecep);
        spa2=this;
        ActivityUtil.add(spa2);
        initToolbar();
        initViews();
        initData();
    }

    private void initToolbar() {
        Toolbar index_toolbar = (Toolbar) findViewById(R.id.index_toolbar);
        TextView tv_bar_title = (TextView) findViewById(R.id.tv_bar_title);
        tv_bar_title.setText("选择省份");
        index_toolbar.setNavigationIcon(R.mipmap.backimg);
        index_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        lv_p= (ListView) findViewById(R.id.lv_p);
        lv_p.setOnItemClickListener(this);
    }

    private void initData() {
        inData=getIntent().getIntExtra("inData",0);
        getP_Data();
    }

    private void getP_Data() {
        MyUtils.showDialog(SelectP2Activity.this,"获取信息中...");
        OkHttpUtils
                .post()
                .tag(this)
                .url(LHHttpUrl.GETPROVICELIST_URL)
                .build()
                .execute(new GetPCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                        MyUtils.showToast(SelectP2Activity.this, "连接服务器失败，请稍后再试");
                    }

                    @Override
                    public void onResponse(GetPData mData) {
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                        if (mData.errcode==1){
//                            MyUtils.showToast(LoginActivity.this,"登陆成功");
                            if (mData.getData().size()!=0){
                                List<P_Data> list=null;
                                if (inData==0||inData==3||inData==4||inData==5){
                                    list=new ArrayList<P_Data>();
                                    P_Data p_data=new P_Data("全部地区","-1");
                                    list.add(p_data);
                                    list.addAll(mData.getData());
                                    lv_p.setAdapter(new P2_lv_Adapter(SelectP2Activity.this,list));
                                }else {
                                    lv_p.setAdapter(new P2_lv_Adapter(SelectP2Activity.this,mData.getData()));
                                }

                            }
                        }else {
                            MyUtils.showToast(SelectP2Activity.this,mData.errmsg);
                        }
                    }
                });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        P_Data mdata= (P_Data) parent.getAdapter().getItem(position);
        if (mdata.getId().equals("-1")){
            Message msg= Message.obtain();
            msg.what=0x101;
            if (inData==0){//群聊界面
                GroupFragment.handler.sendMessage(msg);
            }
            if (inData==4){//个人界面
                PersonalFragment.handler.sendMessage(msg);
            }
            if(inData==3){//首页定位
                HomeFragment.handler.sendMessage(msg);
            }
            if(inData==5){//首页地区界面
                HomeFragment.handler2.sendMessage(msg);
            }
            finish();
            return;
        }

        Intent in=new Intent(spa2,SelectC2Activity.class);
        in.putExtra("p",mdata.getName());
        in.putExtra("p_id",mdata.getId());
        in.putExtra("inData",inData);
        startActivity(in);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(spa2);
    }
}
