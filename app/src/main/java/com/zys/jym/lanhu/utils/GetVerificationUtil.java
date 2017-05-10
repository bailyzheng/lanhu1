package com.zys.jym.lanhu.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.bean.SMSData;
import com.zys.jym.lanhu.httpcallback.SMSCallback;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/5/18.
 */
public class GetVerificationUtil {
    private static String TAG = "TAG--GetVerificationUtil";
    private static int countdown = 60;//获取验证码倒计时
    private static AlertDialog.Builder builder;
    private static Handler mhandler = null;

    /***
     * @param context 上下文
     * @param tv_getverification 获取验证码按钮
     * @param phone 手机号
     * @param status 状态值 0注册1忘记密码
     */
    public static void getverification(final Activity context, final TextView tv_getverification, final String phone, final int status) {
        mhandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 0x200:
//                        tv_getverification.setBackgroundColor(context.getResources().getColor(R.color.btn_no_verification));
                        tv_getverification.setText("重新发送(" + countdown + ")");
                        break;
                    case 0x201:
//                        tv_getverification.setBackgroundColor(context.getResources().getColor(R.color.main_color));
                        countdown = 60;
                        tv_getverification.setText("再次获取");
                        tv_getverification.setEnabled(true);
                        break;
                }
            }
        };
        builder = new AlertDialog.Builder(context);
        builder.setTitle("获取验证码");
        builder.setMessage("将向手机号" + phone + "发送验证码");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 1.发送短信验证码接口

                OkHttpUtils
                        .post()
                        .tag(this)
                        .url(LHHttpUrl.SMSCODE_URL)
                        .addParams("phone", phone)
                        .addParams("type", status+"")
                        .build()
                        .execute(new SMSCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                                MyUtils.showToast(context, "连接服务器失败，请稍后再试");
                            }

                            @Override
                            public void onResponse(SMSData mData) {
                                MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                                if (mData.errcode==1){
                                    tv_getverification.setEnabled(false);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (; countdown > 0; countdown--) {
                                                mhandler.sendEmptyMessage(0x200);
                                                if (countdown <= 0) {
                                                    break;
                                                }
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            mhandler.sendEmptyMessage(0x201);
                                        }
                                    }).start();
                                }else {
                                    MyUtils.showToast(context,mData.errmsg);
                                    MyUtils.Loge(TAG,"失败info="+mData.errmsg);
                                }
                            }
                        });
            }
        });
        AlertDialog ad = builder.create();
        if (!ad.isShowing()) {
            ad.show();
            ad.setCanceledOnTouchOutside(false);
        }
    }

}
