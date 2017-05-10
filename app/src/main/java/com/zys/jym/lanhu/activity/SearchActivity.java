package com.zys.jym.lanhu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.adapter.GroupLvAdapter;
import com.zys.jym.lanhu.bean.MyTopData;
import com.zys.jym.lanhu.bean.TopData;
import com.zys.jym.lanhu.httpcallback.MyTopCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.view.LoginEditText;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/12/15.
 */

public class SearchActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    String TAG="TAG--SearchActivity";
    LoginEditText et_search;
    TextView tv_back;
    ListView lv_search;
    GroupLvAdapter mAdapter;
//    static String p_id="",c_id="",p="",c="";
//
//    public static Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case RequestCode.TO_PC_ACTIVITY:
//                    p = msg.getData().getString("p");
//                    c = msg.getData().getString("c");
//                    p_id = msg.getData().getString("p_id");
//                    c_id = msg.getData().getString("c_id");
//                    break;
//                case 0x101:
//                    p = "";
//                    c = "";
//                    p_id = "";
//                    c_id = "";
//                    break;
//            }
//        }
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_search);
        ActivityUtil.add(this);
        initViews();
        initToolBar();
        initData();
    }
    private void initToolBar() {
        tv_back= (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initViews() {
        et_search= (LoginEditText) findViewById(R.id.et_search);
        lv_search= (ListView) findViewById(R.id.lv_search);
    }

    private void initData() {
        lv_search.setOnItemClickListener(this);
        et_search.addTextChangedListener(new myTextWather());

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        TopData mData= (TopData) adapterView.getAdapter().getItem(position);
        Intent in =new Intent(SearchActivity.this,CardDetailsActivity.class);
        in.putExtra("cardDetail",mData);
        startActivity(in);
    }


    class myTextWather implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (TextUtils.isEmpty(et_search.getText().toString().trim())){
                return;
            }
            search();
        }


    }
    private void search() {
        OkHttpUtils
                .post()
                .url(LHHttpUrl.TOPICLIST_URL)
                .addParams("p", 1+"")
                .addParams("keyword",et_search.getText().toString().trim())
//                .addParams("provinceId",p_id)
//                .addParams("cityId",c_id)
                .build()
                .execute(new MyTopCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        MyUtils.dismssDialog();
                        lv_search.setVisibility(View.INVISIBLE);
                        MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                        MyUtils.showToast(SearchActivity.this, "连接服务器失败，请稍后再试");
                    }

                    @Override
                    public void onResponse(MyTopData mData) {
                        MyUtils.dismssDialog();
                        MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                        if (mData.getErrcode()==1){
                            if (mData.getData().getTopicList().size()!=0){
                                lv_search.setVisibility(View.VISIBLE);
                                if (mAdapter!=null){
                                    mAdapter=null;
                                }
                                mAdapter = new GroupLvAdapter(SearchActivity.this, mData.getData().getTopicList());
                                lv_search.setAdapter(mAdapter);
                            }else{
                                MyUtils.showToast(SearchActivity.this,"没有此内容");
                                lv_search.setVisibility(View.INVISIBLE);
                            }
                        }else {
                            lv_search.setVisibility(View.INVISIBLE);
                            MyUtils.showToast(SearchActivity.this, mData.getErrmsg());
                        }
                    }
                });


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(this);
    }
}
