package com.zys.jym.lanhu.fragment.impl;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.Main2Activity;
import com.zys.jym.lanhu.activity.ModifyDataActivity;
import com.zys.jym.lanhu.activity.PreviewActivity;
import com.zys.jym.lanhu.activity.SelectP2Activity;
import com.zys.jym.lanhu.activity.SettingActivity;
import com.zys.jym.lanhu.activity.pager.ReleasePager;
import com.zys.jym.lanhu.activity.pager.release.R_GroupCardPager;
import com.zys.jym.lanhu.activity.pager.release.R_PersonalCardPager;
import com.zys.jym.lanhu.activity.pager.release.ReleaseBasePager;
import com.zys.jym.lanhu.adapter.FLGvAdapter;
import com.zys.jym.lanhu.bean.FabuSuccessData;
import com.zys.jym.lanhu.bean.UpEWMData;
import com.zys.jym.lanhu.bean.UpHeadImgData;
import com.zys.jym.lanhu.fragment.BaseFragment;
import com.zys.jym.lanhu.fragment.FragmentContainer;
import com.zys.jym.lanhu.httpcallback.FabuSuccessCallback;
import com.zys.jym.lanhu.httpcallback.UpEWMCallback;
import com.zys.jym.lanhu.httpcallback.UpHeadImgCallback;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MediaUtil;
import com.zys.jym.lanhu.utils.MySharedPrefrencesUtil;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.RequestCode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by Administrator on 2017/1/22.
 */

public class ReleaseFragment extends BaseFragment implements View.OnClickListener {
    String TAG = "TAG--ReleaseFragment";
    RadioGroup rg_releae;
    RadioButton rb_rpersonalcard, rb_rgroupcard;
    private static ImageView iv_erweima;
    private static TextView selectTV;
    private EditText titleET, miaoshuET;
    private Button fabuBtn,fabuBtn2;
    static String p = "", p_id = "";
    static String c = "", c_id = "";
    String title;
    String miaoshu;
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RequestCode.TO_PC_ACTIVITY:
                    p = msg.getData().getString("p");
                    c = msg.getData().getString("c");
                    p_id = msg.getData().getString("p_id");
                    c_id = msg.getData().getString("c_id");
                    selectTV.setText(p + "-" + c);
                    break;
                case 0x101:
                    p = "";
                    c = "";
                    p_id = "";
                    c_id = "";
                    break;
            }
        }
    };
    public Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    fabuBtn.setText((long)msg.obj + "s后才能发布");
//                    long currentTime=System.currentTimeMillis();
//                    MySharedPrefrencesUtil.setParam(getActivity(),"release_persional_time",currentTime+180*1000);
                    if((long)(msg.obj)==0l){
                        fabuBtn.setClickable(true);
                        fabuBtn.setText("确认发布");
                    }else {
                        fabuBtn.setClickable(false);
                    }
                    break;
            }
        }
    };

    public Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
//                    long currentTime=System.currentTimeMillis();
//                    MySharedPrefrencesUtil.setParam(getActivity(),"release_group_time",currentTime+180*1000);
                    fabuBtn2.setText((long)msg.obj + "s后才能发布");
                    if((long)(msg.obj)==0l){
                        fabuBtn2.setClickable(true);
                        fabuBtn2.setText("确认发布");
                    }else {
                        fabuBtn2.setClickable(false);
                    }
                    break;
            }
        }
    };
    private static int release_type = 1;
    private LinearLayout ll_release_fl;
    private View v_release_line;
    private TextView tv_release_choose_aear;
    private Dialog pdialog;
    private String flname;
    private int cardId = 0;
    private static String myCardId = "";
    private TextView tv_release_preview;
    private TextView tv_geterm;

    @Override
    public View initView() {
        MyUtils.Loge(TAG, "ReleaseFragment--initView");
        View view = View.inflate(getActivity(), R.layout.fragment_release, null);
        ll_release_fl = (LinearLayout) view.findViewById(R.id.ll_release_fl);
        rg_releae = (RadioGroup) view.findViewById(R.id.rg_releae);
        rb_rpersonalcard = (RadioButton) view.findViewById(R.id.rb_rpersonalcard);
        //rb_rpersonalcard.setOnClickListener(this);
        rb_rgroupcard = (RadioButton) view.findViewById(R.id.rb_rgroupcard);
        //rb_rgroupcard.setOnClickListener(this);
        iv_erweima = (ImageView) view.findViewById(R.id.iv_erweima);
        iv_erweima.setOnClickListener(this);
        selectTV = (TextView) view.findViewById(R.id.personal_address_tv);
        selectTV.setOnClickListener(this);
        titleET = (EditText) view.findViewById(R.id.personal_title_ed);
        miaoshuET = (EditText) view.findViewById(R.id.personal_miaoshu_ed);
        v_release_line = (View) view.findViewById(R.id.v_release_line);
        fabuBtn = (Button) view.findViewById(R.id.sure_fabu_btn);
        fabuBtn.setOnClickListener(this);
        fabuBtn2 = (Button) view.findViewById(R.id.sure_fabu_btn2);
        fabuBtn2.setOnClickListener(this);
        rb_rpersonalcard.setChecked(true);
        tv_release_choose_aear = (TextView) view.findViewById(R.id.tv_release_choose_aear);
        tv_release_choose_aear.setOnClickListener(this);
        tv_release_preview = (TextView) view.findViewById(R.id.tv_release_preview);
        tv_release_preview.setOnClickListener(this);
        rg_releae.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_rpersonalcard:
                        cleanData();
                        fabuBtn.setVisibility(View.VISIBLE);
                        fabuBtn2.setVisibility(View.GONE);
                        release_type = 1;
                        ll_release_fl.setVisibility(View.GONE);
                        v_release_line.setVisibility(View.GONE);
                        break;
                    case R.id.rb_rgroupcard:
                        cleanData();
                        fabuBtn2.setVisibility(View.VISIBLE);
                        fabuBtn.setVisibility(View.GONE);
                        release_type = 2;
                        ll_release_fl.setVisibility(View.VISIBLE);
                        v_release_line.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        tv_geterm=(TextView)view.findViewById(R.id.tv_geterm);
        tv_geterm.setOnClickListener(this);
        return view;
    }
    /**
     * 清空控件上的所有数据
     */
    private void cleanData(){
        Main2Activity.mdata = null;
        tv_release_choose_aear.setText("选择名片分类");
        iv_erweima.setImageResource(R.mipmap.add_pic);
        selectTV.setText("选择地区");
        titleET.setText("");
        miaoshuET.setText("");
    }


    @Override
    public void onResume() {
        super.onResume();
        MyUtils.Loge(TAG, "ReleaseFragment--onResume");

    }

    @Override
    public void initData() {


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_erweima:
                // 从相册中去获取
                MyUtils.Loge(TAG, "进入上传图像的按钮");
                if (ContextCompat.checkSelfPermission(mActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    EasyPermissions.requestPermissions(mActivity, "请允许申请读取图片权限，否则本应用无法进行图片上传发布",
                            0x13, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    MediaUtil.doPickPhotoFromGallery(mActivity);
                }
                break;
            case R.id.personal_address_tv:
                Intent in = new Intent(mActivity, SelectP2Activity.class);
                in.putExtra("inData", 6);
                mActivity.startActivity(in);
                break;
            case R.id.sure_fabu_btn:
                title = titleET.getText().toString();
                miaoshu = miaoshuET.getText().toString();
                if(release_type==2) {
                    if ((tv_release_choose_aear.getText()).equals("选择名片分类")) {
                        MyUtils.showToast(mActivity, "分类不能为空");
                        break;
                    }
                }
                if(selectTV.getText().equals("选择地区")){
                    MyUtils.showToast(mActivity,"地区不能为空");
                    break;
                }
                if(TextUtils.isEmpty(title)){
                    MyUtils.showToast(mActivity,"标题不能为空");
                    break;
                }
                if(titleET.length()<5 || titleET.length() > 10){
                    MyUtils.showToast(mActivity,"标题长度为5-10个字");
                    break;
                }
                if(TextUtils.isEmpty(miaoshu)){
                    MyUtils.showToast(mActivity,"描述不能为空");
                    break;
                }
                if(miaoshuET.length()<10 || miaoshuET.length() > 260){
                    MyUtils.showToast(mActivity,"描述长度为10-260个字");
                    break;
                }
                if(TextUtils.isEmpty(myCardId)){
                    MyUtils.showToast(mActivity,"请先上传二维码");
                    break;
                }

                long currentTime=System.currentTimeMillis();
                MyUtils.Loge("qwer","组的currenttime:"+currentTime);
                MyUtils.Loge("qwer","组的缓存数据:"+MySharedPrefrencesUtil.getParam(getActivity(),"release_persional_time",0l));
                if(currentTime<=(Long)(MySharedPrefrencesUtil.getParam(getActivity(),"release_persional_time",0l))){
                    fabuBtn.setClickable(true);
                    MyUtils.showToast(getActivity(),"三分钟之内不能连续发布个人名片");
                    break;
                }else {
                    fabuBtn.setClickable(true);

                    fabuCard(2);
                    break;
                }
            case R.id.sure_fabu_btn2:
                title = titleET.getText().toString();
                miaoshu = miaoshuET.getText().toString();
                if(release_type==2) {
                    if ((tv_release_choose_aear.getText()).equals("选择名片分类")) {
                        MyUtils.showToast(mActivity, "分类不能为空");
                        break;
                    }
                }
                if(selectTV.getText().equals("选择地区")){
                    MyUtils.showToast(mActivity,"地区不能为空");
                    break;
                }
                if(TextUtils.isEmpty(title)){
                    MyUtils.showToast(mActivity,"标题不能为空");
                    break;
                }
                if(titleET.length()<5 || titleET.length() > 10){
                    MyUtils.showToast(mActivity,"标题长度为5-10个字");
                    break;
                }
                if(TextUtils.isEmpty(miaoshu)){
                    MyUtils.showToast(mActivity,"描述不能为空");
                    break;
                }
                if(miaoshuET.length()<10 || miaoshuET.length() > 260){
                    MyUtils.showToast(mActivity,"描述长度为10-260个字");
                    break;
                }
                if(TextUtils.isEmpty(myCardId)){
                    MyUtils.showToast(mActivity,"请先上传二维码");
                    break;
                }
                long currentTime1=System.currentTimeMillis();
                MyUtils.Loge("qwer","组的currenttime:"+currentTime1);
                MyUtils.Loge("qwer","组的缓存数据:"+MySharedPrefrencesUtil.getParam(getActivity(),"release_group_time",0l));
                if(currentTime1<=(Long)(MySharedPrefrencesUtil.getParam(getActivity(),"release_group_time",0l))){
                    fabuBtn2.setClickable(true);
                    MyUtils.showToast(getActivity(),"三分钟之内不能连续发布群名片");
                    break;
                }else {
                    fabuBtn2.setClickable(true);
                    fabuCard(1);
                    break;
                }
            case R.id.tv_release_choose_aear:
                showFlDialog();
                break;
            case R.id.tv_release_preview:
                if(Main2Activity.mdata!=null) {
                    Intent in1 = new Intent(mActivity, PreviewActivity.class);
                    mActivity.startActivity(in1);
                }else {
                    MyUtils.showToast(mActivity,"请上传二维码");
                }
                break;
            case R.id.rb_rpersonalcard:
                release_type = 1;
                ll_release_fl.setVisibility(View.GONE);
                v_release_line.setVisibility(View.GONE);
                break;
            case R.id.rb_rgroupcard:
                release_type = 2;
                ll_release_fl.setVisibility(View.VISIBLE);
                v_release_line.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_geterm:
                //MyUtils.showToast(mActivity,"点击微信右上方【+】→【发起群聊】→【面对面建群】→随便输入4个数字→群聊创建成功→进入群聊→点击群聊右上角的小人群资料里面有个群二维码→保存群二维码到手机→打开【蓝狐微商】上传即可。");
               showPopWindow();
                break;
        }
    }
    private PopupWindow popWindow;
    private LinearLayout poplinear;
    private void showPopWindow(){
        View view = View.inflate(mContext, R.layout.pop_window_erweima, null);
        popWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        poplinear = (LinearLayout) view.findViewById(R.id.pop_linear);
        TextView sureTV = (TextView) view.findViewById(R.id.pop_window_sure);
        TextView tv_howto_add=(TextView)view.findViewById(R.id.tv_howto_add);
        if(release_type==1){
            tv_howto_add.setText("打开微信，点击【我的】→【个人信息】→【二维码名片】→【保存到手机】→打开【蓝狐微商】上传即可");
        }else {
            tv_howto_add.setText("点击微信右上方【+】→【发起群聊】→【面对面建群】→随便输入4个数字→群聊创建成功→进入群聊→点击群聊右上角的小人群资料里面有个群二维码→保存群二维码到手机→打开【蓝狐微商】上传即可");
        }
        sureTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
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

    /**
     * 发布名片
     */
    private void fabuCard(final int type) {
//        int type = 2;
//        if (release_type == 1){
//            type = 2;
//        } else {
//            type = 1;
//        }
        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .tag(this)
                    .url(LHHttpUrl.ADDTOPIC_URL)
                    .addParams("login_token", app.getUser().getLogin_token())
                    .addParams("type", type + "")
                    .addParams("cataId", cardId + "")
                    .addParams("cardId", myCardId + "")
                    .addParams("provinceId", p_id)
                    .addParams("cityId", c_id)
                    .addParams("title", title)
                    .addParams("describe", miaoshu)
                    .build()
                    .execute(new FabuSuccessCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.showToast(mActivity, "服务器出现问题，请稍后再试");
                            if (type == 1) {
                                fabuBtn2.setClickable(true);
                            }
                            if (type == 2) {
                                fabuBtn.setClickable(true);
                            }

                        }

                        @Override
                        public void onResponse(FabuSuccessData fabuSuccessData) {
                            if (fabuSuccessData != null) {
                                MyUtils.showToast(mActivity, fabuSuccessData.getErrmsg());
                                if (fabuSuccessData.getErrcode() == 1) {
                                    cleanData();
                                    if (release_type == 1) {
                                        setDJS();
                                    } else {
                                        setDJS2();
                                    }
                                    Main2Activity main2Activity = (Main2Activity) getActivity();
                                    switch (release_type) {
                                        case 1:
                                            main2Activity.switchTab(R.id.rb_personal);
                                            main2Activity.rb_personal.setChecked(true);
                                            main2Activity.rb_personal.setSelected(true);
                                            break;
                                        case 2:
                                            main2Activity.switchTab(R.id.rb_group);
                                            main2Activity.rb_group.setChecked(true);
                                            main2Activity.rb_group.setSelected(true);
                                            break;
                                    }
                                } else {
                                    if (type == 1) {
                                        fabuBtn2.setClickable(true);
                                    }
                                    if (type == 2) {
                                        fabuBtn.setClickable(true);
                                    }
                                }
                            }
                        }
                    });
        }
    }

    /**
     * 发布倒计时
     */
    private void setDJS() {
        final Timer timer = new Timer();
        final long[] s = {180};
        long currentTime=System.currentTimeMillis();
        MySharedPrefrencesUtil.setParam(getActivity(),"release_persional_time",currentTime+180*1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(s[0]>0){
                    s[0]--;
                    MyUtils.Loge(TAG,"秒数："+ s[0]);
                    Message message = handler1.obtainMessage();
                    message.what = 1;
                    message.obj = s[0];
                    handler1.sendMessage(message);
                }else {
                    timer.cancel();
                    fabuBtn.setClickable(true);

                }
            }
        }, 0, 1000);
    }

    /**
     * 发布倒计时
     */
    private void setDJS2() {
        final Timer timer = new Timer();
        final long[] s = {180};
        long currentTime=System.currentTimeMillis();
        MySharedPrefrencesUtil.setParam(getActivity(),"release_group_time",currentTime+180*1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(s[0]>0){
                    s[0]--;
                    MyUtils.Loge(TAG,"秒数："+ s[0]);
                    Message message = handler2.obtainMessage();
                    message.what = 1;
                    message.obj = s[0];
                    handler2.sendMessage(message);
                }else {
                    timer.cancel();
                    fabuBtn2.setClickable(true);

                }
            }
        }, 0, 1000);
    }

    /**
     * 选择分类
     */
    private void showFlDialog() {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View v = inflater.inflate(R.layout.view_fl_dialog, null);// 得到加载view
        TextView tv_finsh = (TextView) v.findViewById(R.id.tv_finsh);
        GridView gv_fl = (GridView) v.findViewById(R.id.gv_fl);

        gv_fl.setAdapter(new FLGvAdapter(mActivity));

        if (pdialog != null) {
            pdialog = null;
        }

        pdialog = new Dialog(mActivity, R.style.custom_dialog);// 创建自定义样式dialog
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.setCancelable(false);
        pdialog.setContentView(v);

        if (!pdialog.isShowing()) {
            pdialog.show();
        }
        tv_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                    pdialog = null;
                }
            }
        });
        gv_fl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                flname = (String) parent.getAdapter().getItem(i);
                cardId = i + 1;
                tv_release_choose_aear.setText(flname);
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                    pdialog = null;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case MediaUtil.SELECT_PHOTO_CODE:// 相册
                case MediaUtil.CAMERA_REQUEST_CODE:// 相机
//                    MediaUtil.doCropPhotore(mActivity, data);//调用剪裁图片
//                    ReleasePager.setImg(data);
//                    mdata=data;
                    Main2Activity.mdata = data;
                    MyUtils.Loge(TAG, "DATA::" + data.getData());
                    try {
                        MyUtils.Loge(TAG, "FilePath=" + MediaUtil.getRealFilePath(
                                mActivity, data.getData()));
                        File iconFile = new File(MediaUtil.getRealFilePath(
                                mActivity, data.getData()));
                        postIcon(iconFile, data, release_type);
                    } catch (Exception e) {
                        MyUtils.Loge(TAG, "上传头像异常：" + e);
                    }
                    break;
                case MediaUtil.PHOTO_CROP:// 剪裁
//                    Main2Activity.mdata = data;
//                    MyUtils.Loge(TAG, "DATA::" + data.getData());
//                    try {
//                        MyUtils.Loge(TAG, "FilePath=" + MediaUtil.getRealFilePath(
//                                mActivity, data.getData()));
//                        File iconFile = new File(MediaUtil.getRealFilePath(
//                                mActivity, data.getData()));
//                        postIcon(iconFile, data, release_type);
//                    } catch (Exception e) {
//                        MyUtils.Loge(TAG, "上传头像异常：" + e);
//                    }
                    break;
            }
        }
    }

    /**
     * 上传二维码
     *
     * @param iconFile
     * @param data
     */
    private void postIcon(File iconFile, final Intent data, int release_type) {
        MyUtils.showDialog(mActivity, "正在上传二维码...");
        String url = "";
        if (release_type == 1) {//个人二维码
            url = LHHttpUrl.UPLOAD_PERSONAL_QRCODE_URL;
        }
        if (release_type == 2) {//群组二维码
            url = LHHttpUrl.UPLOADQRCODE_URL;
        }
        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .tag(this)
                    .url(url)
                    .addFile("pic", "erweima.png", iconFile)
                    .addParams("login_token", app.getUser().getLogin_token())
                    .build()
                    .execute(new UpEWMCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                            MyUtils.showToast(mActivity, "服务器出现问题，请稍后再试");
                        }

                        @Override
                        public void onResponse(UpEWMData mData) {
                            MyUtils.dismssDialog();
                            MyUtils.showToast(mActivity, mData.getErrmsg());
                            if (mData.getErrcode() == 1) {
                                myCardId = mData.getData().getCardId();
                                MyUtils.Loge(TAG, "myCardId:" + myCardId);
                                if (!TextUtils.isEmpty(mData.getData().getImgurl())) {
                                    Picasso.with(mContext).load(LHHttpUrl.IMG_URL + mData.getData().getImgurl()).into(iv_erweima);
                                }
                            }
                        }
                    });
        }
    }
}
