package com.zys.jym.lanhu.wxapi;

import android.os.Bundle;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.umeng.weixin.callback.WXCallbackActivity;

/**
 * Created by Administrator on 2016/12/20.
 */

public class WXEntryActivity extends WXCallbackActivity {

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;



    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

}
