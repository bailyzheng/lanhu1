package com.zys.jym.lanhu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.adapter.P_lv_Adapter;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.p_c_d.BaseWheel;


/**
 * Created by Admin on 2016/6/26.
 */
public class SelectPActivity extends BaseWheel implements AdapterView.OnItemClickListener {
    String TAG="TAG--SelectPActivity";
    private ListView lv_p;
    public static  SelectPActivity spa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_selecep);
        spa=this;
        ActivityUtil.add(spa);
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
        initProvinceDatas();
        lv_p= (ListView) findViewById(R.id.lv_p);
        lv_p.setOnItemClickListener(this);
    }

    private void initData() {
        if (mProvinceDatas!=null){
            lv_p.setAdapter(new P_lv_Adapter(spa,mProvinceDatas));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String p= (String) parent.getAdapter().getItem(position);
        Intent in=new Intent(spa,SelectCActivity.class);
        in.putExtra("p",p);
        startActivity(in);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(spa);
    }
}
