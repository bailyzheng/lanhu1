package com.zys.jym.lanhu.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.pager.ReleasePager;
import com.zys.jym.lanhu.adapter.P_lv_Adapter;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.p_c_d.BaseWheel;


/**
 * Created by Admin on 2016/6/26.
 */
public class SelectCActivity extends BaseWheel implements AdapterView.OnItemClickListener {
    private ListView lv_c;
    private String p;
    private String TAG="TAG--SelectCActivity";

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
        initProvinceDatas();
        lv_c= (ListView) findViewById(R.id.lv_c);
        lv_c.setOnItemClickListener(this);
    }
    private void initData() {
        p=getIntent().getStringExtra("p");
        MyUtils.Loge(TAG,"p="+p);
        if (mCitisDatasMap!=null){
            MyUtils.Loge(TAG,"mCitisDatasMap="+mCitisDatasMap.toString());
            String[] c=mCitisDatasMap.get(p);
//            MyUtils.Loge(TAG,"c="+c.toString());
            if (c!=null){
                lv_c.setAdapter(new P_lv_Adapter(SelectCActivity.this,c));
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String c= (String) parent.getAdapter().getItem(position);
        Message msg= Message.obtain();
        Bundle bundle=new Bundle();
        bundle.putString("p",p);
        bundle.putString("c",c);
        msg.setData(bundle);
        msg.what=0x10;
        ReleasePager.handler.sendMessage(msg);

        SelectPActivity.spa.finish();
        finish();

    }
}
