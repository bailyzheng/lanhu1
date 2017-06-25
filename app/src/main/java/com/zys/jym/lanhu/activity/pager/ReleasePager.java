package com.zys.jym.lanhu.activity.pager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.App;
import com.zys.jym.lanhu.R;
import com.zys.jym.lanhu.activity.PreviewActivity;
import com.zys.jym.lanhu.activity.SelectP2Activity;
import com.zys.jym.lanhu.adapter.FLGvAdapter;
import com.zys.jym.lanhu.bean.RegisterData;
import com.zys.jym.lanhu.bean.UpCodeData;
import com.zys.jym.lanhu.fragment.impl.MainFragment;
import com.zys.jym.lanhu.httpcallback.RegisterCallback;
import com.zys.jym.lanhu.httpcallback.UpCodeCallback;
import com.zys.jym.lanhu.receiver.NetWorkChangeReceiver;
import com.zys.jym.lanhu.utils.DialogOkUtil;
import com.zys.jym.lanhu.utils.LHHttpUrl;
import com.zys.jym.lanhu.utils.MediaUtil;
import com.zys.jym.lanhu.utils.MyUtils;
import com.zys.jym.lanhu.utils.RTDBUtil;
import com.zys.jym.lanhu.utils.RequestCode;
import com.zys.jym.lanhu.utils.TimeUtil;

import java.io.File;
import java.util.Random;

import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by Administrator on 2016/3/29.
 */
public class ReleasePager extends MainBasePager implements View.OnClickListener {
    static String TAG = "TAG--ReleasePager";
    private Toolbar index_toolbar;
    private Dialog pdialog = null;
    TextView tv_fl;
    static TextView tv_select;
    static TextView tv_p, tv_c, tv_success;
    static String p = "", p_id = "";
    static String c = "", c_id = "";
    static ImageView iv_code;
    int cardId = 0;
    String flname = "";
    EditText et_title, et_decsribe;
    Button btn_post;
    static int upCode = 0;
    static String codeId;
    private static App app;
    static boolean isSetImg=false;
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RequestCode.TO_PC_ACTIVITY:
                    p = msg.getData().getString("p");
                    c = msg.getData().getString("c");
                    p_id = msg.getData().getString("p_id");
                    c_id = msg.getData().getString("c_id");
                    tv_p.setText(p);
                    tv_c.setText(c);
                    break;
            }
        }
    };

    public ReleasePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initViews() {
        super.initViews();
        MyUtils.Loge(TAG, "ReleasePager--initViews");
        app = (App) mMainActivity.getApplicationContext();
        View view = View.inflate(mMainActivity, R.layout.pager_release, null);
        tv_fl = (TextView) view.findViewById(R.id.tv_fl);
        tv_p = (TextView) view.findViewById(R.id.tv_p);
        tv_c = (TextView) view.findViewById(R.id.tv_c);
        iv_code = (ImageView) view.findViewById(R.id.iv_code);
        tv_success = (TextView) view.findViewById(R.id.tv_success);
        et_title = (EditText) view.findViewById(R.id.et_title);
        et_decsribe = (EditText) view.findViewById(R.id.et_decsribe);
        tv_select = (TextView) view.findViewById(R.id.tv_select);
        btn_post = (Button) view.findViewById(R.id.btn_post);
        btn_post.setOnClickListener(this);
        tv_fl.setOnClickListener(this);
        tv_p.setOnClickListener(this);
        tv_c.setOnClickListener(this);
        iv_code.setOnClickListener(this);
        view.findViewById(R.id.tv_preview).setOnClickListener(this);
        view.findViewById(R.id.tv_havecode).setOnClickListener(this);

        fl_base_content.addView(view);


    }

    @Override
    public void initData() {
        seeTime();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_havecode:
                DialogOkUtil.show_finsh_Dialog(mMainActivity).show();
                break;
            case R.id.tv_fl:
                showFlDialog();
                break;
            case R.id.tv_p:
            case R.id.tv_c:
                Intent in = new Intent(mMainActivity, SelectP2Activity.class);
                in.putExtra("inData", 1);
                mMainActivity.startActivity(in);
                break;
            case R.id.iv_code:
                // 从相册中去获取
                if (ContextCompat.checkSelfPermission(mMainActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    EasyPermissions.requestPermissions(mMainActivity, "请允许申请读取图片权限，否则本应用无法进行图片上传发布",
                            0x13, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    MediaUtil.doPickPhotoFromGallery(mMainActivity);
                }
                break;
            case R.id.btn_post:
                if (cardId == 0) {
                    MyUtils.showToast(mMainActivity, "请选择分类");
                    return;
                }
                if (upCode == 0) {
                    MyUtils.showToast(mMainActivity, "请选择二维码");
                    return;
                }
                if (upCode == 2) {
                    MyUtils.showToast(mMainActivity, "请重新选择二维码");
                    return;
                }
                if (TextUtils.isEmpty(p_id)) {
                    MyUtils.showToast(mMainActivity, "请选择地区");
                    return;
                }
                if (TextUtils.isEmpty(et_title.getText().toString().trim())) {
                    MyUtils.showToast(mMainActivity, "请输入标题");
                    return;
                }
                if (TextUtils.isEmpty(et_decsribe.getText().toString().trim())) {
                    MyUtils.showToast(mMainActivity, "请输入描述");
                    return;
                }
                release();
                break;
            case R.id.tv_preview:
                if (upCode != 1) {
                    MyUtils.showToast(mMainActivity, "请选择二维码");
                    return;
                }
                Intent in1 = new Intent(mMainActivity, PreviewActivity.class);
                mMainActivity.startActivity(in1);
                break;
        }
    }

    private void release() {
        if (!NetWorkChangeReceiver.NET_WORK_TYPE) {
            MyUtils.showToast(mMainActivity, "网络连接不可用，请检查网络");
            return;
        }
        MyUtils.showDialog(mMainActivity, "发布中...");
//        MyUtils.Loge(TAG, "cardId=" + cardId + ",cataId=" + flname +
//                ",login_token=" + app.getUser().getLogin_token() + ",provinceId=" + p_id +
//                ",cityId=" + c_id + ",title=" + et_title.getText().toString().trim() +
//                ",describe=" + et_decsribe.getText().toString().trim());
        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .tag(this)
                    .url(LHHttpUrl.ADDTOPIC_URL)
                    .addParams("login_token", app.getUser().getLogin_token())
                    .addParams("cataId", cardId + "")
                    .addParams("cardId", codeId)
                    .addParams("provinceId", p_id)
                    .addParams("cityId", c_id)
                    .addParams("title", et_title.getText().toString().trim())
                    .addParams("describe", et_decsribe.getText().toString().trim())
                    .build()
                    .execute(new RegisterCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                            MyUtils.showToast(mMainActivity, "连接服务器失败，请稍后再试");
                        }

                        @Override
                        public void onResponse(RegisterData mData) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                            if (mData.errcode == 1) {
                                ref();
                                keepNowTime();
//                            GroupPager.gHaveData=false;
                                MainFragment.check_Group();
//                            if (TopPager.tHaveData){
//                                TopPager.tHaveData=false;
//                            }
                                MyUtils.showToast(mMainActivity, "发布成功!");
                            } else {
                                MyUtils.showToast(mMainActivity, mData.errmsg);
                                MyUtils.Loge(TAG, "失败info=" + mData.errmsg);
                            }
                        }
                    });
        }


    }

    private void showFlDialog() {
        LayoutInflater inflater = LayoutInflater.from(mMainActivity);
        View v = inflater.inflate(R.layout.view_fl_dialog, null);// 得到加载view
        TextView tv_finsh = (TextView) v.findViewById(R.id.tv_finsh);
        GridView gv_fl = (GridView) v.findViewById(R.id.gv_fl);

        gv_fl.setAdapter(new FLGvAdapter(mMainActivity));

        if (pdialog != null) {
            pdialog = null;
        }

        pdialog = new Dialog(mMainActivity, R.style.custom_dialog);// 创建自定义样式dialog
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
                tv_fl.setText(flname);
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                    pdialog = null;
                }
            }
        });
    }

    public static void setImg(Intent data) {
        try {
            MyUtils.Loge(TAG, "FilePath=" + MediaUtil.getRealFilePath(
                    mMainActivity, data.getData()));
            File mCodeFile = new File(MediaUtil.getRealFilePath(
                    mMainActivity, data.getData()));
//            final String pic_path = iconFile.getPath();
//            String targetPath = Environment.getExternalStorageDirectory() + "/lh/YaSuoImg/"+ TimeUtil.getTimeStamp()+"upCode.jpg";
//            //调用压缩图片的方法，返回压缩后的图片path
//            final String compressImage = MediaUtil.compressImage(pic_path, targetPath,100);
//            final File compressedPic = new File(compressImage);
//            MyUtils.Loge(TAG,"原大小="+iconFile.length()+",压缩大小="+compressedPic.length());
//            if (compressedPic.exists()) {
//                MyUtils.Loge(TAG,"图片压缩上传");
//                postImg(compressedPic, data);
//            }else{//直接上传
//                postImg(compressedPic, data);
//            }
            postImg(mCodeFile, data);
        } catch (Exception e) {
            MyUtils.Loge(TAG, "上传二维码异常：" + e);
            MyUtils.showToast(mMainActivity, "图片选择失败");
            upCode = 0;
        }

    }

    private static void postImg(File file, final Intent data) {
        MyUtils.showDialog(mMainActivity, "正在上传...");
        tv_success.setVisibility(View.INVISIBLE);
        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
            OkHttpUtils
                    .post()
                    .tag(mMainActivity)
                    .url(LHHttpUrl.UPLOADQRCODE_URL)
                    .addFile("pic", "pic.png", file)
                    .addParams("login_token", app.getUser().getLogin_token())
                    .build()
                    .connTimeOut(25000L)
                    .readTimeOut(25000L)
                    .writeTimeOut(25000L)
                    .execute(new UpCodeCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.dismssDialog();
                            tv_success.setVisibility(View.VISIBLE);
                            tv_success.setText("上传失败！");
                            upCode = 2;
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                        }

                        @Override
                        public void onResponse(UpCodeData mData) {
                            MyUtils.dismssDialog();
                            tv_success.setVisibility(View.VISIBLE);
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                            if (mData.errcode == 1) {
                                upCode = 1;
                                codeId = mData.getData().getCardId();
                                tv_success.setText("上传成功！");
                                tv_select.setVisibility(View.INVISIBLE);
                                iv_code.setVisibility(View.VISIBLE);
                                iv_code.setImageURI(data.getData());
                            } else {
                                tv_success.setText("上传失败！");
                                upCode = 2;
                                DialogOkUtil.show_Ok_Dialog(mMainActivity, mData.getErrmsg(), new DialogOkUtil.On_OK_ClickListener() {
                                    @Override
                                    public void onOk() {
                                        tv_success.setVisibility(View.INVISIBLE);
                                    }
                                },"").show();
                            }
                        }
                    });
        }
    }

//    class dialoglistener extends DialogOkUtil.On_Finish_ClickListener {
//
//        @Override
//        public void onFinish() {
//
//        }
//    }

    private void ref() {
        tv_fl.setText("选择分类");
        flname = "";
        cardId = 0;
        isSetImg=false;
        tv_select.setVisibility(View.VISIBLE);
        iv_code.setVisibility(View.INVISIBLE);
        tv_success.setVisibility(View.INVISIBLE);
        et_title.setEnabled(false);
        et_decsribe.setEnabled(false);
        upCode = 0;
        p_id = "";
        tv_p.setText("      省      ");
        tv_c.setText("      市      ");
        et_title.setText("");
        et_decsribe.setText("");
        btn_post.setText("3分钟内只能发布一次");
        btn_post.setBackgroundResource(R.drawable.bt_not_bg);
        btn_post.setEnabled(false);
    }

    private void canR() {
        Random rand = new Random();
        int randNum = rand.nextInt(7);
        cardId = randNum + 1;
        switch (cardId) {
            case 1:
                flname = "人脉推广";
                tv_fl.setText(flname);
                break;
            case 2:
                flname = "宝妈互动";
                tv_fl.setText(flname);
                break;
            case 3:
                flname = "经验分享";
                tv_fl.setText(flname);
                break;
            case 4:
                flname = "小白学习";
                tv_fl.setText(flname);
                break;
            case 5:
                flname = "兼职信息";
                tv_fl.setText(flname);
                break;
            case 6:
                flname = "代理产品";
                tv_fl.setText(flname);
                break;
            case 7:
                flname = "微商团队";
                tv_fl.setText(flname);
                break;
        }
        et_title.setEnabled(true);
        et_decsribe.setEnabled(true);
        tv_select.setVisibility(View.VISIBLE);
        iv_code.setVisibility(View.VISIBLE);
        tv_success.setVisibility(View.INVISIBLE);
        btn_post.setText("确认发布");
        btn_post.setBackgroundResource(R.drawable.bt_bg_round_dark);
        btn_post.setEnabled(true);
    }

    private void keepNowTime() {
        if (RTDBUtil.selectCountFormUid(mMainActivity, app.getUser().getUID()) == 0) {
            RTDBUtil.insertCode(mMainActivity, app.getUser().getUID(), TimeUtil.getNowTimeYMDHMS(), TimeUtil.getLastTime(3));
        } else {
            RTDBUtil.updateUserTime(mMainActivity, app.getUser().getUID(), TimeUtil.getNowTimeYMDHMS(), TimeUtil.getLastTime(3));
        }
    }

    private void seeTime() {
        try {
            if (RTDBUtil.selectCountFormUid(mMainActivity, app.getUser().getUID()) == 0) {
                canR();
            } else {
                String[] times = RTDBUtil.selectTimeformUid(mMainActivity, app.getUser().getUID());
                MyUtils.Loge(TAG, "times[0]=" + times[0] + "times[1]=" + times[1]);
                int l = TimeUtil.getSurplusTime(times[1]);
                if (l <= 0) {
                    canR();
                } else {
                    ref();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG, e.toString());
            canR();


        }

    }
}
