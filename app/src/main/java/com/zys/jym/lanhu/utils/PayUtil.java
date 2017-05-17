package com.zys.jym.lanhu.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zys.jym.lanhu.App;
import com.zys.jym.lanhu.activity.ComplaintActivity;
import com.zys.jym.lanhu.activity.Main2Activity;
import com.zys.jym.lanhu.activity.RechargeActivity;
import com.zys.jym.lanhu.activity.ZDActivity;
import com.zys.jym.lanhu.bean.PayResult;
import com.zys.jym.lanhu.bean.ToPayData;
import com.zys.jym.lanhu.httpcallback.ToPayCallback;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/12/22.
 */

public class PayUtil {
    static String TAG = "TAG--PayUtil";

    public static void toPay(final Activity activity, final App app, final int opmoney, final int payType, final int opType,final int opNum) {
//        double opmoney;
//        if (opType!=1&&payType==3){
//            opmoney=pay_money*0.01;
//            MyUtils.Loge(TAG,"opmoney="+opmoney);
//        }else {
//            opmoney=pay_money;
//            MyUtils.Loge(TAG,"opmoney="+opmoney);
//        }

        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    0x1);
            return;
        }
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0x2);
            return;
        }

//        MyUtils.Loge(TAG, "login_token=" + app.getUser().getLogin_token()
//                + ",opmoney=" + opmoney + ",payType=" + payType + ",opType=" + opType + ",opNum=" + opNum);
        MyUtils.showDialog(activity, "下单中...");
//        if(app!=null&&app.getUser()!=null&&!TextUtils.isEmpty(app.getUser().getLogin_token())) {
        if(!TextUtils.isEmpty(SPrefUtil.getString(activity,"TOKEN",""))){
            OkHttpUtils
                    .post()
                    .url(LHHttpUrl.PREPAY_URL)
//                    .addParams("login_token", app.getUser().getLogin_token())
                    .addParams("login_token", SPrefUtil.getString(activity,"TOKEN",""))
                    .addParams("opmoney", (opmoney * 100) + "")//opmoney*100
                    .addParams("payType", payType + "")
                    .addParams("opType", opType + "")
                    .addParams("opNum", opNum + "")
                    .build()
                    .execute(new ToPayCallback() {
                        @Override
                        public void onError(Call call, Exception e) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求失败：call=" + call.toString() + "--e=" + e.toString());
                            MyUtils.showToast(activity, "连接服务器失败，请稍后再试");
                        }

                        @Override
                        public void onResponse(ToPayData mData) {
                            MyUtils.dismssDialog();
                            MyUtils.Loge(TAG, "请求成功：mData=" + mData.toString());
                            if(mData.getErrcode()==40001){
                                ActivityUtil.exitAll();
                                ActivityUtil.toLogin(activity);
                                return;
                            }
                            if (mData.getErrcode() == 1) {
                                if (payType == 1) {
                                    MyUtils.Loge(TAG, "调起微信支付");
                                    WixPay wp = new WixPay(activity);
                                    wp.Pay(mData.getData().getWxdata());
                                }
                                if (payType == 2) {
                                    MyUtils.Loge(TAG, "调起支付宝支付");
                                    AlipayUtil alipayUtil = new AlipayUtil(activity);
                                    alipayUtil.pay(mData.getData().getAlidata());
                                    alipayUtil.setListener(new AlipayUtil.OnAlipayListener() {
                                        @Override
                                        public void onSuccess(PayResult payResult) {
                                            MyUtils.showToast(activity, "支付成功");
                                            if (opType == 1) {//充值狐币
                                                Main2Activity.getPurseData(false);
                                                MyUtils.showToast(activity, "充值成功");
                                                RechargeActivity.ra.finish();
                                            }
                                            if (opType == 2) {//开通VIP
                                                Main2Activity.getPurseData(false);
                                                MyUtils.showToast(activity, "开通成功");
                                            }
                                            if (opType == 3) {
                                                MyUtils.showToast(activity, "兑换成功");
//                                                Intent in =new Intent(activity,ZDActivity.class);
//                                                activity.startActivity(in);
                                                Main2Activity.getPurseData(true);
                                            }
                                            activity.finish();
                                        }

                                        @Override
                                        public void onCancel(String resultStatus) {
                                            MyUtils.showToast(activity, "取消支付");
                                        }

                                        @Override
                                        public void onWait(String resultStatus) {
                                            MyUtils.showToast(activity, "支付失败");
                                        }
                                    });
                                }
                                if (payType == 3) {
                                    if (opType == 2) {//开通VIP
                                        MyUtils.showToast(activity, "开通成功");
                                        Main2Activity.getPurseData(false);
                                    }
                                    if (opType == 3) {
                                        MyUtils.showToast(activity, "兑换成功");
//                                    Intent in =new Intent(activity,ZDActivity.class);
//                                    activity.startActivity(in);
                                        Main2Activity.getPurseData(true);
                                    }
                                    activity.finish();
                                }
                            } else {
                                MyUtils.showToast(activity, mData.getErrmsg());
                            }
                        }
                    });
        }


    }
}
