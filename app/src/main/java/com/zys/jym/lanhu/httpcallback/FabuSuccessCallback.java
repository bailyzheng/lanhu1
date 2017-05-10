package com.zys.jym.lanhu.httpcallback;

import com.google.gson.GsonBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zys.jym.lanhu.bean.FabuSuccessData;
import com.zys.jym.lanhu.bean.GetPData;
import com.zys.jym.lanhu.utils.MyUtils;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/11.
 */

public abstract class FabuSuccessCallback extends Callback<FabuSuccessData> {
    @Override
    public FabuSuccessData parseNetworkResponse(Response response) throws IOException {
        String jsonStr = response.body().string();
        FabuSuccessData mData = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create().fromJson(jsonStr,  FabuSuccessData.class);
        return mData;
    }
}
