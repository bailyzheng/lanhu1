package com.zys.jym.lanhu.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.bean.NormalData;
import com.zys.jym.lanhu.fragment.BaseFragment;
import com.zys.jym.lanhu.fragment.FragmentContainer;
import com.zys.jym.lanhu.httpcallback.NormalCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.DataCleanManager;
import com.zys.jym.lanhu.utils.DialogOkUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.SPrefUtil;
import com.zys.jym.lanhu.view.CircleImageView;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/12/14.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    String TAG="TAG--SettingActivity";
    Toolbar index_toolbar;
    TextView tv_data;
    String cache_size;
   static CircleImageView civ_head;
    CheckBox cb_push;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_setting);
        ActivityUtil.add(this);
        initViews();
        initToolBar();
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
        findViewById(R.id.rl_aboutus).setOnClickListener(this);
        findViewById(R.id.rl_modifydata).setOnClickListener(this);
        findViewById(R.id.rl_cleardata).setOnClickListener(this);
        findViewById(R.id.rl_logout).setOnClickListener(this);
        tv_data= (TextView) findViewById(R.id.tv_data);
        civ_head= (CircleImageView) findViewById(R.id.civ_head);
        cb_push= (CheckBox) findViewById(R.id.cb_push);
        cb_push.setOnClickListener(this);
    }

    private void initData() {
        if (!TextUtils.isEmpty(getApplicationContext().getUser().getHeadurl())){
            Picasso.with(SettingActivity.this)
                    .load(LHHttpUrl.IMG_URL+getApplicationContext().getUser().getHeadurl())
                    .into(civ_head);
        }
        try {
            cache_size= DataCleanManager.getTotalCacheSize(SettingActivity.this);
            tv_data.setText(cache_size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (SPrefUtil.getBoolean(this,"PUSH",true)){
            cb_push.setChecked(true);
        }else {
            cb_push.setChecked(false);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cb_push:
                if (cb_push.isChecked()){
                    SPrefUtil.setBoolean(SettingActivity.this,"PUSH",true);
                    JPushInterface.init(SettingActivity.this);
                }else {
                    SPrefUtil.setBoolean(SettingActivity.this,"PUSH",false);
                    JPushInterface.stopPush(SettingActivity.this);
                }
                break;
            case R.id.rl_aboutus:
                Intent in =new Intent(SettingActivity.this,AboutUsActivity.class);
                startActivity(in);
            break;
            case R.id.rl_modifydata:
                Intent in1 =new Intent(SettingActivity.this,ModifyDataActivity.class);
                in1.putExtra("setting",true);
                startActivity(in1);
                break;
            case R.id.rl_cleardata:

                DialogOkUtil.show_OK_NO_Dialog(SettingActivity.this, "是否清除缓存？", new DialogOkUtil.On_OK_N0_ClickListener() {
                    @Override
                    public void onOk() {
                        DataCleanManager.clearAllCache(SettingActivity.this);
                        try {
                            cache_size=DataCleanManager.getTotalCacheSize(SettingActivity.this);
                            tv_data.setText(cache_size);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNo() {

                    }
                },"").show();

                break;
            case R.id.rl_logout:

                showPopWindow();
                break;
        }
    }
    private PopupWindow popWindow;
    private LinearLayout poplinear;
    private void showPopWindow(){
        View view = View.inflate(this, R.layout.pop_window_setting, null);
        popWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        poplinear = (LinearLayout) view.findViewById(R.id.pop_linear);
        TextView sureTV = (TextView) view.findViewById(R.id.pop_window_sure);
        sureTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
                Logout();

            }
        });
        TextView cancleTV = (TextView) view.findViewById(R.id.pop_window_cancle);
        cancleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);
        popWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popWindow.showAtLocation(poplinear, Gravity.CENTER, 0, 0);
    }

    private void Logout() {
//        if(getApplicationContext()!=null&&getApplicationContext().getUser()!=null&&!TextUtils.isEmpty(getApplicationContext().getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .tag(this)
                    .url(LHHttpUrl.LOGOUT_URL)
//                    .addParams("login_token", getApplicationContext().getUser().getLogin_token())
                    .addParams("login_token", SPrefUtil.getString(SettingActivity.this,"TOKEN",""))
                    .build()
                    .execute(new NormalCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                        }

                        @Override
                        public void onResponse(NormalData mData) {
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                            getApplicationContext().setIsLogin(false);
                            SPrefUtil.setString(SettingActivity.this, "PHONE", "");
                            SPrefUtil.setString(SettingActivity.this,"PWD","");
                            Intent in = new Intent(SettingActivity.this, LoginActivity.class);
                            in.putExtra("logout", true);
                            Main2Activity.ma.finish();

                            startActivity(in);

//                        BaseFragment.mRootView=null;
                            finish();
                            System.exit(0);
                        }
                    });
//        }
    }

    public  static  void setHeadImg(Intent data){
        civ_head.setImageURI(data.getData());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.delect(this);
    }


}
