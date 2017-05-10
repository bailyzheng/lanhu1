package com.zys.jym.lanhu.httpcallback;

import com.google.gson.GsonBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zys.jym.lanhu.bean.HomeZDData;
import com.zys.jym.lanhu.bean.KeepPhoneData;
import com.zys.jym.lanhu.utils.MyUtils;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/15.
 */

public abstract class KeepPhoneCallback extends Callback<KeepPhoneData> {
    @Override
    public KeepPhoneData parseNetworkResponse(Response response) throws Exception {
        //response.body().string()只能调用一次 否则报错 ！切记！！！！！
        String jsonStr = response.body().string();
        MyUtils.Loge("TAG--JSON","JSON="+jsonStr);
        KeepPhoneData mData = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create().fromJson(jsonStr,  KeepPhoneData.class);
        return mData;
    }
}
