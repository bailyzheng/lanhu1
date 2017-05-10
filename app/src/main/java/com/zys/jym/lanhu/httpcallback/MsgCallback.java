package com.zys.jym.lanhu.httpcallback;

import com.google.gson.GsonBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zys.jym.lanhu.bean.MsgCenterData;
import com.zys.jym.lanhu.utils.MyUtils;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by Administrator on 2016/6/2.
 */
public abstract class MsgCallback extends Callback<MsgCenterData> {
    @Override
    public MsgCenterData parseNetworkResponse(Response response) throws IOException {
        //response.body().string()只能调用一次 否则报错 ！切记！！！！！
        String jsonStr = response.body().string();
        MyUtils.Loge("TAG--JSON","JSON="+jsonStr);
        MsgCenterData mData = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create().fromJson(jsonStr,  MsgCenterData.class);
        return mData;
    }
}
