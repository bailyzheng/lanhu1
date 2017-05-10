package com.zys.jym.lanhu.utils;

import android.app.Activity;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zys.jym.lanhu.bean.WXPayData;

public class WixPay {

    private Activity activity;
    private IWXAPI api;

    public WixPay(Activity activity) {
        this.activity = activity;
        api = WXAPIFactory.createWXAPI(activity,Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
    }

    public void Pay(WXPayData mWXPayData) {
        PayReq request = new PayReq();
        request.appId =Constants.APP_ID;       //开放平台appID
        request.partnerId = mWXPayData.getMch_id();           //支付商户号
        request.prepayId= mWXPayData.getPrepay_id();       //
        request.packageValue = "Sign=WXPay";       //固定值
        request.nonceStr= mWXPayData.getNonce_str();       //
        request.timeStamp=mWXPayData.getTimeStamp()+"";          //时间戳
        request.sign=mWXPayData.getSign();

        api.sendReq(request);
    }


}
