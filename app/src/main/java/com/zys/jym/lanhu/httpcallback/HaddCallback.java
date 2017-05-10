package com.zys.jym.lanhu.httpcallback;

import com.google.gson.GsonBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zys.jym.lanhu.bean.IsAddBean;
import com.zys.jym.lanhu.utils.MyUtils;

import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/1.
 */

public abstract class HaddCallback extends Callback<IsAddBean> {
    @Override
    public IsAddBean parseNetworkResponse(Response response) throws Exception {
        //response.body().string()只能调用一次 否则报错 ！切记！！！！！
        String jsonStr = response.body().string();
        MyUtils.Loge("TAG--JSON", "JSON=" + jsonStr);
        IsAddBean mData = new GsonBuilder().
                setPrettyPrinting()
                .disableHtmlEscaping()
                .create().fromJson(jsonStr, IsAddBean.class);
        return mData;
    }
}
