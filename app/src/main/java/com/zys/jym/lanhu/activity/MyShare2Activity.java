package com.zys.jym.lanhu.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.umeng.weixin.umengwx.WeChat;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.BaseActivity;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.bean.GetPurseData;
import com.zys.jym.lanhu.bean.ShareDataBean;
import com.zys.jym.lanhu.fragment.impl.MineFragment;
import com.zys.jym.lanhu.httpcallback.GetPurseCallback;
import com.zys.jym.lanhu.utils.ActivityUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.SPrefUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.Call;

public class MyShare2Activity extends BaseActivity implements View.OnClickListener {

    private TextView tv_share2_qqk;
    private TextView tv_share2_qq;
    private TextView tv_share2_wx;
    private TextView tv_share2_wxq;
    private Platform plat;
    private String TAG = "MyShare2Activity";
    private String title;
    private String content;
    private TextView tv_share2_regcode;
    private Toolbar index_toolbar;
    private RequestQueue volleyQueue;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_share2);
        volleyQueue = Volley.newRequestQueue(getApplicationContext());
        initViews();
        initData();
        initToolBar();
    }

    private void initData() {
        getShareData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 获取分享状态
     */
    private void getShareStatus() {
        String url = LHHttpUrl.GET_SHARE_STATUS_URL;
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int errcode = jsonObject.getInt("errcode");
                    String errmsg = jsonObject.getString("errmsg");
                    MyUtils.showToast(MyShare2Activity.this,errmsg);
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.showToast(MyShare2Activity.this, "网络有问题");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> map=new HashMap<>();
                map.put("login_token",SPrefUtil.getString(MyShare2Activity.this,"TOKEN",""));
                map.put("type",type);
                return map;
            }
        };
        volleyQueue.add(stringRequest);
    }

    /**
     * 获取分享数据
     */
    private void getShareData() {

        String url = LHHttpUrl.GET_SHARE_URL;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.Loge(TAG, "response:" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int errcode = jsonObject.getInt("errcode");
                    String errmsg = jsonObject.getString("errmsg");
                    if (errcode == 1) {
                        Gson gson = new Gson();
                        ShareDataBean shareDataBean = gson.fromJson(response, ShareDataBean.class);
                        if (shareDataBean != null) {
                            setViews(shareDataBean);
                        }
                    } else {
                        MyUtils.showToast(MyShare2Activity.this, errmsg);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.showToast(MyShare2Activity.this, "网络有问题");
            }
        });
        volleyQueue.add(stringRequest);
    }

    private void setViews(ShareDataBean shareDataBean) {
        if (shareDataBean.getData() != null && !TextUtils.isEmpty(shareDataBean.getData().getTitle()))
            title = shareDataBean.getData().getTitle();
        if (shareDataBean.getData() != null && !TextUtils.isEmpty(shareDataBean.getData().getContent()))
            content = shareDataBean.getData().getContent();
        if (getApplicationContext().getUser() != null && !TextUtils.isEmpty(getApplicationContext().getUser().getAppurl()) && !TextUtils.isEmpty(getApplicationContext().getUser().getRegcode())) {
            tv_share2_regcode.setText(getApplicationContext().getUser().getRegcode());
        }
    }

    private void initViews() {
        tv_share2_qqk = (TextView) findViewById(R.id.tv_share2_qqk);
        tv_share2_qqk.setOnClickListener(this);
        tv_share2_qq = (TextView) findViewById(R.id.tv_share2_qq);
        tv_share2_qq.setOnClickListener(this);
        tv_share2_wx = (TextView) findViewById(R.id.tv_share2_wx);
        tv_share2_wx.setOnClickListener(this);
        tv_share2_wxq = (TextView) findViewById(R.id.tv_share2_wxq);
        tv_share2_wxq.setOnClickListener(this);
        tv_share2_regcode=(TextView)findViewById(R.id.tv_share2_regcode);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_share2_qqk:
                type="4";
                plat = ShareSDK.getPlatform(QZone.NAME);
                myShare(plat.getName());
                break;
            case R.id.tv_share2_qq:
                type="3";
                plat = ShareSDK.getPlatform(QQ.NAME);
                myShare(plat.getName());
                break;
            case R.id.tv_share2_wx:
                type="1";
                plat = ShareSDK.getPlatform(Wechat.NAME);
                myShare(plat.getName());
                break;
            case R.id.tv_share2_wxq:
                type="2";
                plat = ShareSDK.getPlatform(WechatMoments.NAME);
                myShare(plat.getName());
                break;
        }
    }

    /**
     * 分享
     */
    private void myShare(String platform) {
        MyUtils.Loge(TAG,"Regcode:"+getApplicationContext().getUser().getRegcode());
        MyUtils.Loge(TAG,"url:"+getApplicationContext().getUser().getAppurl());
        if(!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(content)&&getApplicationContext().getUser()!=null&&!TextUtils.isEmpty(getApplicationContext().getUser().getAppurl())&&!TextUtils.isEmpty(getApplicationContext().getUser().getRegcode())) {
            OnekeyShare oks = new OnekeyShare();
            if (platform != null)
                oks.setPlatform(platform);
            //关闭sso授权
            oks.disableSSOWhenAuthorize();

            // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
            //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));

            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            oks.setCallback(new MyCallBalk());

            oks.setTitle(title+getApplicationContext().getUser().getRegcode());

            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            oks.setTitleUrl(getApplicationContext().getUser().getAppurl());

            // text是分享文本，所有平台都需要这个字段
            oks.setText(content);

            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            oks.setImagePath("file:///android_asset/icon_launcher.png");//确保SDcard下面存在此张图片
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl(getApplicationContext().getUser().getAppurl());
            oks.setImageUrl("file:///android_asset/icon_launcher.png");
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//            oks.setComment("我是测试评论文本");
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(getString(R.string.app_name));

            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl(getApplicationContext().getUser().getAppurl());

            // 启动分享GUI
            oks.show(this);
        }

    }
    class MyCallBalk implements PlatformActionListener{

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            getShareStatus();
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(Platform platform, int i) {

        }
    }
}
